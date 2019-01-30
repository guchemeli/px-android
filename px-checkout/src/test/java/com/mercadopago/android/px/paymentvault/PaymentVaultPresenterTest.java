package com.mercadopago.android.px.paymentvault;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.core.PaymentMethodPlugin;
import com.mercadopago.android.px.internal.callbacks.OnSelectedCallback;
import com.mercadopago.android.px.internal.datasource.MercadoPagoESC;
import com.mercadopago.android.px.internal.features.PaymentVaultPresenter;
import com.mercadopago.android.px.internal.features.PaymentVaultView;
import com.mercadopago.android.px.internal.features.hooks.Hook;
import com.mercadopago.android.px.internal.features.providers.PaymentVaultProvider;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.PluginRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.mocks.PaymentMethodSearchs;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CustomSearchItem;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.preferences.PaymentPreference;
import com.mercadopago.android.px.utils.StubSuccessMpCall;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentVaultPresenterTest {

    private PaymentVaultPresenter presenter;
    private final StubView stubView = new StubView();

    @Mock private PaymentSettingRepository paymentSettingRepository;
    @Mock private CheckoutPreference checkoutPreference;
    @Mock private UserSelectionRepository userSelectionRepository;
    @Mock private PluginRepository pluginRepository;
    @Mock private DiscountRepository discountRepository;
    @Mock private GroupsRepository groupsRepository;
    @Mock private PaymentVaultView view;

    @Mock private Site mockSite;

    private static final DiscountConfigurationModel WITHOUT_DISCOUNT =
        new DiscountConfigurationModel(null, null, false);

    @Before
    public void setUp() {
        when(checkoutPreference.getTotalAmount()).thenReturn(new BigDecimal(100));
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(checkoutPreference);
        when(checkoutPreference.getPaymentPreference()).thenReturn(new PaymentPreference());
        when(checkoutPreference.getSite()).thenReturn(mockSite);
        presenter = getPresenter();
    }

    @NonNull
    private PaymentVaultPresenter getBasePresenter(
        final PaymentVaultView view) {

        final PaymentVaultPresenter presenter =
            new PaymentVaultPresenter(paymentSettingRepository, userSelectionRepository,
                pluginRepository, discountRepository, groupsRepository, mock(MercadoPagoESC.class));
        presenter.attachView(view);

        return presenter;
    }

    @NonNull
    private PaymentVaultPresenter getPresenter() {
        return getBasePresenter(view);
    }

    @Test
    public void whenItemSelectedAvailableTrackIt() {
        final PaymentVaultView mockView = mock(PaymentVaultView.class);
        final PaymentVaultProvider mockProvider = mock(PaymentVaultProvider.class);
        final PaymentMethodSearchItem mockPaymentOptions = mock(PaymentMethodSearchItem.class);

        presenter.attachView(mockView);
        presenter.setSelectedSearchItem(mockPaymentOptions);
        presenter.trackScreen();
        verifyNoMoreInteractions(mockProvider);
        verifyNoMoreInteractions(mockView);
    }

    @Test
    public void whenNoPaymentMethodsAvailableThenShowError() {
        final PaymentMethodSearch paymentMethodSearch = new PaymentMethodSearch();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        verify(view).showEmptyPaymentMethodsError();
    }

    @Test
    public void whenPaymentMethodSearchHasItemsShowThem() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        verify(view).showSearchItems(eq(paymentMethodSearch.getGroups()), any(OnSelectedCallback.class));
    }

    @Test
    public void whenPaymentMethodSearchHasPayerCustomOptionsShowThem() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        verify(view).showCustomOptions(eq(paymentMethodSearch.getCustomSearchItems()), any(OnSelectedCallback.class));
    }

    //Automatic selections

    @Test
    public void whenOnlyUniqueSearchItemAvailableRestartWithItSelected() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyTicketMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));
        when(discountRepository.getCurrentConfiguration()).thenReturn(WITHOUT_DISCOUNT);

        presenter.initialize();

        verify(view).showSelectedItem(paymentMethodSearch.getGroups().get(0));
    }

    @Test
    public void whenOnlyCardPaymentTypeAvailableStartCardFlow() {
        final PaymentMethodSearch paymentMethodSearch =
            PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardMLA();
        final PaymentVaultPresenter presenter = getPresenter();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));
        when(discountRepository.getCurrentConfiguration()).thenReturn(WITHOUT_DISCOUNT);

        presenter.initialize();

        verify(view).showAmount(discountRepository.getCurrentConfiguration(),
            checkoutPreference.getTotalAmount(), mockSite);
        verify(view).setMainTitle();
        verify(view).startCardFlow(anyBoolean());
        verify(paymentSettingRepository, atLeastOnce()).getCheckoutPreference();
        verify(userSelectionRepository, times(1)).select(PaymentTypes.CREDIT_CARD);
        verifyNoMoreInteractions(view);
        verifyNoMoreInteractions(paymentSettingRepository);
    }

    @Test
    public void whenOnlyCardPaymentTypeAvailableAndCardAvailableThenDoNotSelectAutomatically() {
        final PaymentMethodSearch paymentMethodSearch =
            PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardAndOneCardMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        verifyDoNotSelectCustomOptionAutomatically(paymentMethodSearch);
    }

    @Test
    public void whenOnlyCardPaymentTypeAvailableAndAccountMoneyAvailableThenDoNotSelectAutomatically() {
        final PaymentMethodSearch paymentMethodSearch =
            PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardAndAccountMoneyMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        verifyDoNotSelectCustomOptionAutomatically(paymentMethodSearch);
    }

    @Test
    public void whenOnlyOffPaymentTypeAvailableAndAccountMoneyAvailableThenDoNotSelectAutomatically() {
        final PaymentMethodSearch paymentMethodSearch =
            PaymentMethodSearchs.getPaymentMethodSearchWithOnlyOneOffTypeAndAccountMoneyMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        verifyDoNotSelectCustomOptionAutomatically(paymentMethodSearch);
    }

    //User selections

    @Test
    public void whenItemWithChildrenSelectedThenShowChildren() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        final PaymentMethodSearchItem selectedSearchItem = paymentMethodSearch.getGroups().get(1);
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.setSelectedSearchItem(selectedSearchItem);

        presenter.initialize();

        verify(view).setTitle(selectedSearchItem.getChildrenHeader());
        verify(view).showSearchItems(eq(selectedSearchItem.getChildren()), any(OnSelectedCallback.class));
        verify(view).hideProgress();
    }

    @Test
    public void whenCardPaymentTypeSelectedThenStartCardFlow() {
        final PaymentVaultPresenter presenter = getBasePresenter(stubView);
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();
        stubView.simulateItemSelection(0);

        Assert.assertTrue(stubView.cardFlowStarted);
    }

    @Test
    public void whenSavedCardSelectedThenStartSavedCardFlow() {
        final PaymentVaultPresenter presenter = getBasePresenter(stubView);
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();
        stubView.simulateCustomItemSelection(1);

        Assert.assertTrue(stubView.savedCardFlowStarted);
        Assert.assertEquals(stubView.savedCardSelected, paymentMethodSearch.getCards().get(0));
    }

    //Payment Preference tests
    @Test
    public void whenAllPaymentMethodsExcludedShowError() {
        final PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setExcludedPaymentTypeIds(PaymentTypes.getAllPaymentTypes());

        when(checkoutPreference.getPaymentPreference()).thenReturn(paymentPreference);

        presenter.initialize();

        verify(view).showError(any(MercadoPagoError.class), anyString());
    }

    @Test
    public void whenBoletoSelectedThenCollectPayerInformation() {
        final PaymentVaultPresenter presenter = getPresenter();
        when(groupsRepository.getGroups())
            .thenReturn(new StubSuccessMpCall<>(PaymentMethodSearchs.getPaymentMethodSearchWithOnlyBolbradescoMLB()));
        when(discountRepository.getCurrentConfiguration()).thenReturn(WITHOUT_DISCOUNT);

        presenter.initialize();

        verify(view).collectPayerInformation();
    }

    @Test
    public void whenPayerInformationReceivedThenFinishWithPaymentMethodSelection() {
        final PaymentVaultPresenter presenter = getPresenter();

        presenter.onPayerInformationReceived();

        verify(view).finishPaymentMethodSelection(userSelectionRepository.getPaymentMethod());
    }

    // --------- Helper methods ----------- //

    private void verifyInitializeWithGroups() {
        verify(view, atLeastOnce()).showAmount(discountRepository.getCurrentConfiguration(),
            paymentSettingRepository.getCheckoutPreference().getTotalAmount(),
            paymentSettingRepository.getCheckoutPreference().getSite());
        verify(view, atLeastOnce()).setMainTitle();
        verify(view, atLeastOnce())
            .showPluginOptions(any(Collection.class), any(PaymentMethodPlugin.PluginPosition.class));
        verify(view, atLeastOnce()).hideProgress();
    }

    private void verifyDoNotSelectCustomOptionAutomatically(final PaymentMethodSearch paymentMethodSearch) {
        verifyInitializeWithGroups();
        verify(view).showCustomOptions(eq(paymentMethodSearch.getCustomSearchItems()), any(OnSelectedCallback.class));
        verify(view).showSearchItems(eq(paymentMethodSearch.getGroups()), any(OnSelectedCallback.class));
        verifyNoMoreInteractions(view);
    }

    /*

    @Test
    public void ifInvalidDefaultInstallmentsShowError() {
        final PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setDefaultInstallments(BigDecimal.ONE.negate().intValue());
        when(checkoutPreference.getPaymentPreference()).thenReturn(paymentPreference);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_DEFAULT_INSTALLMENTS, stubView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidMaxInstallmentsShowError() {
        final PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setMaxAcceptedInstallments(BigDecimal.ONE.negate().intValue());
        when(checkoutPreference.getPaymentPreference()).thenReturn(paymentPreference);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_MAX_INSTALLMENTS, stubView.errorShown.getMessage());
    }

    @Test
    public void ifMaxSavedCardNotSetDoNotLimitCardsShown() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        assertEquals(paymentMethodSearch.getCustomSearchItems().size(), stubView.customOptionsShown.size());
    }

    //Discounts
    @Test
    public void ifDiscountsAreNotEnabledNotShowDiscountRow() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();

        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();
        stubView.simulateItemSelection(0);

        Assert.assertTrue(stubView.showedDiscountRow);
    }

    @Test
    public void ifDiscountsAreEnabledGetDirectDiscount() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));
        final Discount discount = Discounts.getDiscountWithAmountOffMLA();
        stubProvider.setDiscountResponse(discount);

        presenter.initialize();

        // no assertions?
    }

    @Test
    public void ifHasNotDirectDiscountsShowDiscountRow() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));
        final ApiException apiException = Discounts.getDoNotFindCampaignApiException();
        final MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        stubProvider.setDiscountResponse(mpException);

        presenter.initialize();
        stubView.simulateItemSelection(0);

        Assert.assertEquals(stubProvider.CAMPAIGN_DOES_NOT_MATCH_ERROR,
            stubProvider.failedResponse.getApiException().getError());
        Assert.assertEquals(true, stubView.showedDiscountRow);
    }

    @Test
    public void ifIsDirectDiscountNotEnabledNotGetDirectDiscount() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();
        stubView.simulateItemSelection(0);

        Assert.assertTrue(stubView.showedDiscountRow);
    }

    @Test
    public void whenGroupsRetrievalReturnsAliExceptionThenShowError() {
        final ApiException apiException = new ApiException();
        apiException.setMessage("Mocked failure");
        when(groupsRepository.getGroups()).thenReturn(new StubFailMpCall<PaymentMethodSearch>(apiException));
        final PaymentVaultView mock = mock(PaymentVaultView.class);
        presenter.attachView(mock);
        presenter.initialize();
        verify(mock).showError(any(MercadoPagoError.class), anyString());
    }

    @Test
    public void whenResourcesRetrievalFailedAndRecoverRequestedThenRepeatRetrieval() {
        final ApiException apiException = new ApiException();
        apiException.setMessage("Mocked failure");
        final MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, "");
        stubProvider.setResponse(mercadoPagoError);
        when(groupsRepository.getGroups())
            .thenReturn(new StubSuccessMpCall<>(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA()));

        presenter.initialize();
        //Presenter gets resources, fails
        presenter.recoverFromFailure();

        Assert.assertFalse(stubView.searchItemsShown.isEmpty());
    }

    @Test
    public void ifPaymentMethodSearchSetAndHasItemsThenShowThem() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        assertEquals(paymentMethodSearch.getGroups(), stubView.searchItemsShown);
    }

    @Test
    public void ifPaymentMethodSearchItemIsNotCardAndDoesNotHaveChildrenThenStartPaymentMethodsSelection() {
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        paymentMethodSearch.getGroups().get(1).getChildren()
            .removeAll(paymentMethodSearch.getGroups().get(1).getChildren());

        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();

        stubView.simulateItemSelection(1);
        Assert.assertTrue(stubView.paymentMethodSelectionStarted);
    }

    @Test
    public void ifPaymentMethodTypeSelectedThenSelectPaymentMethod() {
        final PaymentMethodSearch paymentMethodSearch =
            PaymentMethodSearchs.getPaymentMethodSearchWithPaymentMethodOnTop();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.initialize();
        stubView.simulateItemSelection(1);

        Assert.assertEquals(paymentMethodSearch.getGroups().get(1).getId(), stubView.selectedPaymentMethod.getId());
    }

    @Test
    public void whenHasCustomItemsThenShowThemAll() {
        // 6 Saved Cards + Account Money
        final PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithSavedCardsMLA();
        when(groupsRepository.getGroups()).thenReturn(new StubSuccessMpCall<>(paymentMethodSearch));

        presenter.attachView(stubView);
        presenter.initialize();

        assertEquals(stubView.customOptionsShown.size(), paymentMethodSearch.getCustomSearchItems().size());
    }*/

    private static class StubView implements PaymentVaultView {

        /* default */ List<PaymentMethodSearchItem> searchItemsShown;
        /* default */ MercadoPagoError errorShown;
        /* default */ List<CustomSearchItem> customOptionsShown;
        /* default */ PaymentMethodSearchItem itemShown;
        /* default */ boolean cardFlowStarted = false;
        /* default */ boolean isItemShown;
        /* default */ PaymentMethod selectedPaymentMethod;
        private OnSelectedCallback<PaymentMethodSearchItem> itemSelectionCallback;
        private OnSelectedCallback<CustomSearchItem> customItemSelectionCallback;
        private String title;
        /* default */ boolean savedCardFlowStarted;
        private boolean payerInformationStarted;
        /* default */ Card savedCardSelected;
        /* default */ Boolean showedDiscountRow;

        /* default */ boolean paymentMethodSelectionStarted = false;

        @Override
        public void startSavedCardFlow(final Card card) {
            savedCardFlowStarted = true;
            savedCardSelected = card;
        }

        @Override
        public void showPaymentMethodPluginActivity() {
            //Not yet tested
        }

        @Override
        public void showSelectedItem(final PaymentMethodSearchItem item) {
            itemShown = item;
            isItemShown = true;
            searchItemsShown = item.getChildren();
        }

        @Override
        public void showProgress() {
            //Not yet tested
        }

        @Override
        public void hideProgress() {
            //Not yet tested
        }

        @Override
        public void showCustomOptions(final List<CustomSearchItem> customSearchItems,
            final OnSelectedCallback<CustomSearchItem> customSearchItemOnSelectedCallback) {
            customOptionsShown = customSearchItems;
            customItemSelectionCallback = customSearchItemOnSelectedCallback;
        }

        @Override
        public void showPluginOptions(final Collection<PaymentMethodPlugin> items,
            final PaymentMethodPlugin.PluginPosition position) {

        }

        @Override
        public void showSearchItems(final List<PaymentMethodSearchItem> searchItems,
            final OnSelectedCallback<PaymentMethodSearchItem> paymentMethodSearchItemSelectionCallback) {
            searchItemsShown = searchItems;
            itemSelectionCallback = paymentMethodSearchItemSelectionCallback;
        }

        @Override
        public void showError(final MercadoPagoError mpException, final String requestOrigin) {
            errorShown = mpException;
        }

        @Override
        public void setTitle(final String title) {
            this.title = title;
        }

        @Override
        public void setMainTitle() {

        }

        @Override
        public void startCardFlow(final Boolean automaticallySelection) {
            cardFlowStarted = true;
        }

        @Override
        public void startPaymentMethodsSelection(final PaymentPreference paymentPreference) {
            paymentMethodSelectionStarted = true;
        }

        @Override
        public void finishPaymentMethodSelection(final PaymentMethod selectedPaymentMethod) {
            this.selectedPaymentMethod = selectedPaymentMethod;
        }

        @Override
        public void showAmount(@NonNull final DiscountConfigurationModel discountModel,
            @NonNull final BigDecimal totalAmount,
            @NonNull final Site site) {
            showedDiscountRow = true;
        }

        @Override
        public void collectPayerInformation() {
            payerInformationStarted = true;
        }

        @Override
        public void cleanPaymentMethodOptions() {
            //Not yet tested
        }

        @Override
        public void showHook(final Hook hook, final int code) {
            //Not yet tested
        }

        @Override
        public void showDetailDialog(@NonNull final DiscountConfigurationModel discountModel) {
            //Do nothing
        }

        @Override
        public void showEmptyPaymentMethodsError() {

        }

        @Override
        public void showMismatchingPaymentMethodError() {

        }

        /* default */ void simulateItemSelection(final int index) {
            itemSelectionCallback.onSelected(searchItemsShown.get(index));
        }

        /* default */ void simulateCustomItemSelection(final int index) {
            customItemSelectionCallback.onSelected(customOptionsShown.get(index));
        }
    }
}
