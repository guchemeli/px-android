package com.mercadopago.android.px.internal.features.installments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.base.BasePresenter;
import com.mercadopago.android.px.internal.callbacks.FailureRecovery;
import com.mercadopago.android.px.internal.controllers.PaymentMethodGuessingController;
import com.mercadopago.android.px.internal.features.express.installments.InstallmentsAdapter;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.PayerCostRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.SummaryAmountRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.CountyInstallmentsUtils;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.AmountView;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.CardInfo;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.SummaryAmount;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.views.InstallmentsViewTrack;
import java.util.List;

public class InstallmentsPresenter extends BasePresenter<InstallmentsView> implements
    AmountView.OnClick, InstallmentsAdapter.ItemListener, PayerCostListener {

    @NonNull private final SummaryAmountRepository summaryAmountRepository;
    @NonNull private final PayerCostRepository payerCostRepository;
    @NonNull /* default */ final AmountRepository amountRepository;
    @NonNull /* default */ final PaymentSettingRepository configuration;
    @NonNull /* default */ final UserSelectionRepository userSelectionRepository;
    @NonNull /* default */ final PayerCostSolver payerCostSolver;
    @NonNull /* default */ final DiscountRepository discountRepository;

    private FailureRecovery failureRecovery;

    //Card Info
    private String bin = TextUtil.EMPTY;

    @Nullable private CardInfo cardInfo;

    public InstallmentsPresenter(@NonNull final AmountRepository amountRepository,
        @NonNull final PaymentSettingRepository configuration,
        @NonNull final UserSelectionRepository userSelectionRepository,
        @NonNull final DiscountRepository discountRepository,
        @NonNull final SummaryAmountRepository summaryAmountRepository,
        @NonNull final PayerCostRepository payerCostRepository,
        @NonNull final PayerCostSolver payerCostSolver) {
        this.amountRepository = amountRepository;
        this.configuration = configuration;
        this.userSelectionRepository = userSelectionRepository;
        this.discountRepository = discountRepository;
        this.summaryAmountRepository = summaryAmountRepository;
        this.payerCostRepository = payerCostRepository;
        this.payerCostSolver = payerCostSolver;
    }

    public void initialize() {
        showSiteRelatedInformation();
        resolvePayerCosts();
    }

    private void showSiteRelatedInformation() {
        if (CountyInstallmentsUtils
            .shouldWarnAboutBankInterests(configuration.getCheckoutPreference().getSite().getId())) {
            getView().warnAboutBankInterests();
        }
    }

    private void resolvePayerCosts() {
        if (userSelectionRepository.hasCardSelected()) {
            resolvePayerCostsForSavedCard();
            getView().showAmount(discountRepository.getCurrentConfiguration(),
                amountRepository.getItemsPlusCharges(), configuration.getCheckoutPreference().getSite());
        } else {
            resolvePayerCostsForGuessedCard();
        }
    }

    /* default */ void resolvePayerCostsForSavedCard() {
        getView().hideLoadingView();
        payerCostSolver.solve(this, payerCostRepository.getCurrentConfiguration().getPayerCosts());
    }

    /* default */ void resolvePayerCostsForGuessedCard() {
        getView().showLoadingView();
        summaryAmountRepository.getSummaryAmount(bin).enqueue(new Callback<SummaryAmount>() {
            @Override
            public void success(final SummaryAmount summaryAmount) {
                final AmountConfiguration amountConfiguration =
                    summaryAmount.getAmountConfiguration(summaryAmount.getDefaultAmountConfiguration());
                discountRepository.addConfigurations(summaryAmount);
                payerCostSolver.solve(InstallmentsPresenter.this, amountConfiguration.getPayerCosts());

                if (isViewAttached()) {
                    getView().showAmount(discountRepository.getCurrentConfiguration(),
                        amountRepository.getItemsPlusCharges(), configuration.getCheckoutPreference().getSite());
                    getView().hideLoadingView();
                }
            }

            @Override
            public void failure(final ApiException apiException) {
                if (isViewAttached()) {
                    getView().hideLoadingView();
                    getView().showApiErrorScreen(apiException, ApiUtil.RequestOrigin.POST_SUMMARY_AMOUNT);
                    setFailureRecovery(new FailureRecovery() {
                        @Override
                        public void recover() {
                            resolvePayerCostsForGuessedCard();
                        }
                    });
                }
            }
        });
    }

    public void setCardInfo(@Nullable final CardInfo cardInfo) {
        this.cardInfo = cardInfo;
        if (this.cardInfo != null) {
            bin = this.cardInfo.getFirstSixDigits();
        }
    }

    @Nullable
    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public int getCardNumberLength() {
        return PaymentMethodGuessingController.getCardNumberLength(userSelectionRepository.getPaymentMethod(), bin);
    }

    public void setFailureRecovery(final FailureRecovery failureRecovery) {
        this.failureRecovery = failureRecovery;
    }

    public FailureRecovery getFailureRecovery() {
        return failureRecovery;
    }

    @Nullable
    public PaymentMethod getPaymentMethod() {
        return userSelectionRepository.getPaymentMethod();
    }

    public String getBin() {
        return bin;
    }

    public void recoverFromFailure() {
        if (failureRecovery != null) {
            failureRecovery.recover();
        }
    }

    @Override
    public void onDetailClicked(@NonNull final DiscountConfigurationModel discountModel) {
        getView().showDetailDialog(discountModel);
    }

    @Override
    public void onClick(final PayerCost payerCostSelected) {
        userSelectionRepository.select(payerCostSelected);
        getView().finishWithResult();
    }

    @Override
    public void onEmptyOptions() {
        getView().showErrorNoPayerCost();
    }

    @Override
    public void onSelectedPayerCost() {
        getView().finishWithResult();
    }

    @Override
    public void displayInstallments(final List<PayerCost> payerCosts) {
        final InstallmentsViewTrack installmentsViewTrack =
            new InstallmentsViewTrack(payerCosts, userSelectionRepository);

        setCurrentViewTracker(installmentsViewTrack);

        getView().showInstallments(payerCosts);
    }

    public void removeUserSelection() {
        userSelectionRepository.reset();
    }
}
