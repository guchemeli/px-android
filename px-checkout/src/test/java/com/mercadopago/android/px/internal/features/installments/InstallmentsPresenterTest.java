package com.mercadopago.android.px.internal.features.installments;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.PayerCostRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.SummaryAmountRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.mocks.StubSummaryAmount;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.model.SummaryAmount;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.utils.StubFailMpCall;
import com.mercadopago.android.px.utils.StubSuccessMpCall;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstallmentsPresenterTest {

    private InstallmentsPresenter presenter;

    @Mock private AmountRepository amountRepository;
    @Mock private PaymentSettingRepository configuration;
    @Mock private CheckoutPreference checkoutPreference;
    @Mock private UserSelectionRepository userSelectionRepository;
    @Mock private DiscountRepository discountRepository;
    @Mock private SummaryAmountRepository summaryAmountRepository;
    @Mock private PayerCostRepository payerCostRepository;
    @Mock private PayerCostSolver payerCostSolver;

    @Mock private InstallmentsView view;

    @Before
    public void setUp() {
        when(checkoutPreference.getSite()).thenReturn(Sites.ARGENTINA);
        when(configuration.getCheckoutPreference()).thenReturn(checkoutPreference);
        presenter = new InstallmentsPresenter(amountRepository, configuration, userSelectionRepository,
            discountRepository, summaryAmountRepository, payerCostRepository, payerCostSolver);
        presenter.attachView(view);
    }

    @Test
    public void whenIsGuessingFlowThenCallSummaryAmount() {
        when(userSelectionRepository.hasCardSelected()).thenReturn(false);
        presetApiError();

        presenter.initialize();

        verify(view).showLoadingView();
        verify(summaryAmountRepository).getSummaryAmount(anyString());
    }

    @Test
    public void whenIsGuessingFlowAndApiCallFailedThenShowErrorScreen() {
        when(userSelectionRepository.hasCardSelected()).thenReturn(false);
        final ApiException apiException = presetApiError();

        presenter.initialize();

        verify(view).hideLoadingView();
        verify(view).showApiErrorScreen(apiException, ApiUtil.RequestOrigin.POST_SUMMARY_AMOUNT);
    }

    @Test
    public void whenIsGuessingAndPayerCostIsSelectedFinishWithResult() {
        when(userSelectionRepository.hasCardSelected()).thenReturn(false);
        final SummaryAmount response = StubSummaryAmount.getSummaryAmountTwoPayerCosts();
        when(summaryAmountRepository.getSummaryAmount(anyString())).thenReturn(new StubSuccessMpCall<>(response));

        final String selectedAmountConfiguration = response.getDefaultAmountConfiguration();

        presenter.initialize();
        presenter.onClick(response.getAmountConfiguration(selectedAmountConfiguration).getPayerCosts().get(0));

        verify(view).hideLoadingView();
        verify(view).finishWithResult();
    }

    @Test
    public void whenMCOThenShowBankInterestsNotCoveredWarning() {
        when(checkoutPreference.getSite()).thenReturn(Sites.COLOMBIA);
        when(userSelectionRepository.hasCardSelected()).thenReturn(false);
        final SummaryAmount response = StubSummaryAmount.getSummaryAmountTwoPayerCosts();
        when(summaryAmountRepository.getSummaryAmount(anyString())).thenReturn(new StubSuccessMpCall<>(response));

        presenter.initialize();
        verify(view).warnAboutBankInterests();
    }

    @Test
    public void whenCardSelectedThenResolvePayerCosts() {
        final AmountConfiguration amountConfiguration = mock(AmountConfiguration.class);
        final List<PayerCost> payerCosts = mock(List.class);
        when(userSelectionRepository.hasCardSelected()).thenReturn(true);
        when(payerCostRepository.getCurrentConfiguration()).thenReturn(amountConfiguration);
        when(amountConfiguration.getPayerCosts()).thenReturn(payerCosts);
        when(payerCostRepository.getCurrentConfiguration().getPayerCosts()).thenReturn(payerCosts);

        presenter.initialize();

        verify(view).hideLoadingView();
    }

    @Test
    public void verifyIsGuessingAndSolverIsCalled() {
        when(userSelectionRepository.hasCardSelected()).thenReturn(false);
        final SummaryAmount response = StubSummaryAmount.getSummaryAmountEmptyPayerCosts();
        when(summaryAmountRepository.getSummaryAmount(anyString())).thenReturn(new StubSuccessMpCall<>(response));

        presenter.initialize();

        verify(view).hideLoadingView();
        verify(payerCostSolver).solve(presenter, response.getAmountConfiguration(response.getDefaultAmountConfiguration()).getPayerCosts());
    }

    @Test
    public void verifyIsSavedCardAndSolverIsCalled() {
        when(userSelectionRepository.hasCardSelected()).thenReturn(true);
        when(payerCostRepository.getCurrentConfiguration()).thenReturn(mock(AmountConfiguration.class));

        presenter.initialize();

        verify(payerCostSolver).solve(presenter, payerCostRepository.getCurrentConfiguration().getPayerCosts());
    }

    @Test
    public void verifyResolvesEmptyPayerCostList() {
        presenter.onEmptyOptions();

        verify(view).showErrorNoPayerCost();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void verifyResolvesOnSelectedPayerCostPayerCostList() {
        presenter.onSelectedPayerCost();

        verify(view).finishWithResult();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void verifyResolvesDisplayInstallments() {
        final List<PayerCost> payerCosts = Collections.singletonList(mock(PayerCost.class));

        presenter.displayInstallments(payerCosts);

        verify(view).showInstallments(payerCosts);
        verifyNoMoreInteractions(view);

    }

    @NonNull
    private ApiException presetApiError() {
        final ApiException apiException = new ApiException();
        when(summaryAmountRepository.getSummaryAmount(anyString()))
            .thenReturn(new StubFailMpCall<SummaryAmount>(apiException));
        return apiException;
    }
}
