package com.mercadopago.android.px.internal.features.express;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpPresenter;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper;
import com.mercadopago.android.px.internal.features.express.slider.HubAdapter;
import com.mercadopago.android.px.internal.features.express.slider.SplitPaymentHeaderAdapter;
import com.mercadopago.android.px.internal.repository.AmountConfigurationRepository;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.NoConnectivityException;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryView;
import com.mercadopago.android.px.internal.viewmodel.PayerCostSelection;
import com.mercadopago.android.px.internal.viewmodel.mappers.ElementDescriptorMapper;
import com.mercadopago.android.px.internal.viewmodel.mappers.PaymentMethodDescriptorMapper;
import com.mercadopago.android.px.internal.viewmodel.mappers.PaymentMethodDrawableItemMapper;
import com.mercadopago.android.px.internal.viewmodel.mappers.SplitHeaderMapper;
import com.mercadopago.android.px.internal.viewmodel.mappers.SummaryViewModelMapper;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.events.AbortOneTapEventTracker;
import com.mercadopago.android.px.tracking.internal.events.ConfirmEvent;
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker;
import com.mercadopago.android.px.tracking.internal.events.InstallmentsEventTrack;
import com.mercadopago.android.px.tracking.internal.events.SwipeOneTapEventTracker;
import com.mercadopago.android.px.tracking.internal.views.OneTapViewTracker;
import java.util.Collections;
import java.util.List;

/* default */ class ExpressPaymentPresenter extends MvpPresenter<ExpressPayment.View, ResourcesProvider>
    implements ExpressPayment.Actions,
    AmountDescriptorView.OnClickListenerWithDiscount,
    SplitPaymentHeaderAdapter.SplitListener {

    private static final String BUNDLE_STATE_PAYER_COST =
        "com.mercadopago.android.px.internal.features.express.PAYER_COST";
    /* default */ PayerCostSelection payerCostSelection;

    private static final String BUNDLE_STATE_SPLIT_PREF =
        "com.mercadopago.android.px.internal.features.express.SPLIT_PREF";
    /* default */ boolean isSplitUserPreference = false;

    @NonNull private final PaymentRepository paymentRepository;
    @NonNull private final AmountRepository amountRepository;
    @NonNull private final DiscountRepository discountRepository;
    @NonNull private final PaymentSettingRepository paymentConfiguration;
    @NonNull private final AmountConfigurationRepository amountConfigurationRepository;
    @NonNull private final ExplodeDecoratorMapper explodeDecoratorMapper;

    //TODO remove.
    /* default */ List<ExpressMetadata> expressMetadataList;
    private final PaymentMethodDrawableItemMapper paymentMethodDrawableItemMapper;

    /* default */ ExpressPaymentPresenter(@NonNull final PaymentRepository paymentRepository,
        @NonNull final PaymentSettingRepository paymentConfiguration,
        @NonNull final DiscountRepository discountRepository,
        @NonNull final AmountRepository amountRepository,
        @NonNull final GroupsRepository groupsRepository,
        @NonNull final AmountConfigurationRepository amountConfigurationRepository) {

        this.paymentRepository = paymentRepository;
        this.paymentConfiguration = paymentConfiguration;
        this.amountRepository = amountRepository;
        this.discountRepository = discountRepository;
        this.amountConfigurationRepository = amountConfigurationRepository;
        explodeDecoratorMapper = new ExplodeDecoratorMapper();
        paymentMethodDrawableItemMapper = new PaymentMethodDrawableItemMapper();

        groupsRepository.getGroups().execute(new Callback<PaymentMethodSearch>() {
            @Override
            public void success(final PaymentMethodSearch paymentMethodSearch) {
                expressMetadataList = paymentMethodSearch.getExpress();
                //Plus one to compensate for add new payment method
                payerCostSelection = new PayerCostSelection(expressMetadataList.size() + 1);
            }

            @Override
            public void failure(final ApiException apiException) {
                throw new IllegalStateException("groups missing rendering one tap");
            }
        });
    }

    @Override
    public void attachView(final ExpressPayment.View view) {
        super.attachView(view);

        final ElementDescriptorView.Model elementDescriptorModel =
            new ElementDescriptorMapper().map(paymentConfiguration.getCheckoutPreference());

        final List<SummaryView.Model> summaryModels =
            new SummaryViewModelMapper(paymentConfiguration.getCheckoutPreference(), discountRepository,
                amountRepository, elementDescriptorModel, this).map(expressMetadataList);

        final List<PaymentMethodDescriptorView.Model> paymentModels =
            new PaymentMethodDescriptorMapper(paymentConfiguration, amountConfigurationRepository)
                .map(expressMetadataList);

        final List<SplitPaymentHeaderAdapter.Model> splitHeaderModels =
            new SplitHeaderMapper(paymentConfiguration.getCheckoutPreference().getSite().getCurrencyId(),
                amountConfigurationRepository)
                .map(expressMetadataList);

        final HubAdapter.Model model = new HubAdapter.Model(paymentModels, summaryModels, splitHeaderModels);

        getView().showToolbarElementDescriptor(elementDescriptorModel);

        getView().configureAdapters(paymentMethodDrawableItemMapper.map(expressMetadataList),
            paymentConfiguration.getCheckoutPreference().getSite(), model);
    }

    @Override
    public void onViewResumed() {
        // If a payment was attempted, the exploding fragment is still visible when we go back to one tap fragment.
        // Example: call for authorize, after asking for cvv and pressing back, we go back to one tap and need to
        // remove the exploding fragment we had before.
        if (paymentRepository.hasPayment()) {
            cancelLoading();
        }
        paymentRepository.attach(this);
    }

    @Override
    public void trackExpressView() {
        new OneTapViewTracker(expressMetadataList,
            paymentConfiguration.getCheckoutPreference(),
            discountRepository.getCurrentConfiguration())
            .track();
    }

    @Override
    public void confirmPayment(final int paymentMethodSelectedIndex) {
        refreshExplodingState();

        // TODO improve: This was added because onetap can detach this listener on its onDestroy
        paymentRepository.attach(this);

        final ExpressMetadata expressMetadata = expressMetadataList.get(paymentMethodSelectedIndex);

        PayerCost payerCost = null;
        boolean splitPayment = false;

        if (expressMetadata.isCard()) {
            final AmountConfiguration amountConfiguration =
                amountConfigurationRepository.getConfigurationFor(expressMetadata.getCard().getId());
            splitPayment = isSplitUserPreference && amountConfiguration.allowSplit();
            if (splitPayment) {
                payerCost = PayerCost
                    .getPayerCost(amountConfiguration.split.payerCosts,
                        payerCostSelection.get(paymentMethodSelectedIndex),
                        amountConfiguration.split.selectedPayerCostIndex);
            } else {
                payerCost = PayerCost
                    .getPayerCost(amountConfiguration.payerCosts, payerCostSelection.get(paymentMethodSelectedIndex),
                        amountConfiguration.selectedPayerCostIndex);
            }
        }

        //TODO fill cards with esc
        ConfirmEvent.from(Collections.<String>emptySet(), expressMetadata, payerCost).track();

        paymentRepository.startExpressPayment(expressMetadata, payerCost, splitPayment);
    }

    private void refreshExplodingState() {
        if (paymentRepository.isExplodingAnimationCompatible()) {
            getView().startLoadingButton(paymentRepository.getPaymentTimeout());
            getView().disableToolbarBack();
        }
    }

    @Override
    public void cancel() {
        new AbortOneTapEventTracker().track();
        getView().cancel();
    }

    //TODO verify if current item still persist when activity is destroyed.
    @Override
    public void onTokenResolved(final int paymentMethodSelectedIndex) {
        cancelLoading();
        confirmPayment(paymentMethodSelectedIndex);
    }

    @Override
    public void onPaymentFinished(@NonNull final Payment payment) {
        getView().finishLoading(explodeDecoratorMapper.map(payment));
    }

    /**
     * When there is no visual interaction needed this callback is called.
     *
     * @param genericPayment plugin payment.
     */
    @Override
    public void onPaymentFinished(@NonNull final GenericPayment genericPayment) {
        getView().finishLoading(explodeDecoratorMapper.map(genericPayment));
    }

    /**
     * When there is no visual interaction needed this callback is called.
     *
     * @param businessPayment plugin payment.
     */
    @Override
    public void onPaymentFinished(@NonNull final BusinessPayment businessPayment) {
        getView().finishLoading(explodeDecoratorMapper.map(businessPayment));
    }

    @Override
    public void onPaymentError(@NonNull final MercadoPagoError error) {
        cancelLoading();
        if (error.isInternalServerError() || error.isNoConnectivityError()) {
            FrictionEventTracker.with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW,
                FrictionEventTracker.Id.GENERIC, FrictionEventTracker.Style.CUSTOM_COMPONENT,
                error)
                .track();
            getView().showErrorSnackBar(error);
        } else {
            getView().showErrorScreen(error);
        }
    }

    @Override
    public void onVisualPayment() {
        getView().showPaymentProcessor();
    }

    @Override
    public void onCvvRequired(@NonNull final Card card) {
        cancelLoading();
        getView().showCardFlow(card);
    }

    @Override
    public void onRecoverPaymentEscInvalid(final PaymentRecovery recovery) {
        getView().onRecoverPaymentEscInvalid(recovery);
    }

    @Override
    public void updateElementPosition(final int paymentMethodIndex) {
        getView().hideInstallmentsSelection();
        getView().showInstallmentsDescriptionRow(paymentMethodIndex, payerCostSelection.get(paymentMethodIndex));
    }

    private void updateElementPosition(final int paymentMethodIndex, final int selectedPayerCost) {
        payerCostSelection.save(paymentMethodIndex, selectedPayerCost);
        updateElementPosition(paymentMethodIndex);
    }

    @Override
    public void onViewPaused() {
        paymentRepository.detach(this);
    }

    @Override
    public void onInstallmentsRowPressed(final int currentItem) {
        // TODO add split payment modificiations ;;;

        final ExpressMetadata expressMetadata = expressMetadataList.get(currentItem);
        final CardMetadata cardMetadata = expressMetadata.getCard();
        if (currentItem <= expressMetadataList.size() && cardMetadata != null) {
            final AmountConfiguration amountConfiguration =
                amountConfigurationRepository.getConfigurationFor(cardMetadata.getId());
            final List<PayerCost> payerCostList = amountConfiguration.getPayerCosts();
            if (payerCostList.size() > 1) {
                int selectedPayerCostIndex = payerCostSelection.get(currentItem);
                if (selectedPayerCostIndex == PayerCost.NO_SELECTED) {
                    selectedPayerCostIndex = amountConfiguration.getDefaultPayerCostIndex();
                }
                getView().showInstallmentsList(payerCostList, selectedPayerCostIndex);
                new InstallmentsEventTrack(expressMetadata, amountConfiguration).track();
            }
        }
    }

    /**
     * When user cancel the payer cost selection this method will be called with the current payment method position
     *
     * @param position current payment method position.
     */
    @Override
    public void onInstallmentSelectionCanceled(final int position) {
        updateElementPosition(position);
        getView().collapseInstallmentsSelection();
    }

    /**
     * When user selects a new payment method this method will be called with the new current paymentMethodIndex.
     *
     * @param paymentMethodIndex current payment method paymentMethodIndex.
     */
    @Override
    public void onSliderOptionSelected(final int paymentMethodIndex) {
        new SwipeOneTapEventTracker().track();
        updateElementPosition(paymentMethodIndex, payerCostSelection.get(paymentMethodIndex));
    }

    /**
     * When user selects a new payer cost for certain payment method this method will be called.
     *
     * @param paymentMethodIndex current payment method position.
     * @param payerCostSelected user selected payerCost.
     */
    @Override
    public void onPayerCostSelected(final int paymentMethodIndex, final PayerCost payerCostSelected) {
        final CardMetadata cardMetadata = expressMetadataList.get(paymentMethodIndex).getCard();
        final int selected =
            amountConfigurationRepository.getConfigurationFor(cardMetadata.getId()).getPayerCosts()
                .indexOf(payerCostSelected);
        updateElementPosition(paymentMethodIndex, selected);
        getView().collapseInstallmentsSelection();
    }

    @Override
    public void detachView() {
        onViewPaused();
        super.detachView();
    }

    @Override
    public void fromBundle(@NonNull final Bundle bundle) {
        payerCostSelection = bundle.getParcelable(BUNDLE_STATE_SPLIT_PREF);
        isSplitUserPreference = bundle.getBoolean(BUNDLE_STATE_SPLIT_PREF, false);
    }

    @NonNull
    @Override
    public Bundle toBundle(@NonNull final Bundle bundle) {
        bundle.putParcelable(BUNDLE_STATE_PAYER_COST, payerCostSelection);
        bundle.putBoolean(BUNDLE_STATE_SPLIT_PREF, isSplitUserPreference);
        return bundle;
    }

    @Override
    public void hasFinishPaymentAnimation() {
        final IPayment payment = paymentRepository.getPayment();
        if (payment != null) {
            getView().showPaymentResult(payment);
        }
    }

    private void cancelLoading() {
        getView().enableToolbarBack();
        getView().cancelLoading();
    }

    @Override
    public void manageNoConnection() {
        final NoConnectivityException exception = new NoConnectivityException();
        final ApiException apiException = ApiUtil.getApiException(exception);
        final MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, null);
        FrictionEventTracker.with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW,
            FrictionEventTracker.Id.GENERIC, FrictionEventTracker.Style.CUSTOM_COMPONENT,
            mercadoPagoError)
            .track();
        getView().showErrorSnackBar(mercadoPagoError);
    }

    @Override
    public void onAmountDescriptorClicked(@NonNull final DiscountConfigurationModel discountModel) {
        getView().showDiscountDetailDialog(discountModel);
    }

    @Override
    public void onSplitChanged(final boolean isChecked) {
        // Reset payer cost selection.
        payerCostSelection = new PayerCostSelection(expressMetadataList.size() + 1);
        // ver si agregamos 2 tipos de selecciones.
        isSplitUserPreference = isChecked;
    }
}