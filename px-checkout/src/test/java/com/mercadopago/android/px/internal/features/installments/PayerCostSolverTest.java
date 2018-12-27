package com.mercadopago.android.px.internal.features.installments;

import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.PaymentPreference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PayerCostSolverTest {

    @Mock private PaymentPreference paymentPreference;
    @Mock private UserSelectionRepository userSelectionRepository;

    @Mock private PayerCostListener listener;
    @Mock private List<PayerCost> payerCosts;

    private PayerCostSolver solver;

    @Before
    public void setUp() {
        solver = new PayerCostSolver(paymentPreference, userSelectionRepository);
    }

    @Test
    public void whenDefaultPayerCostIsAvailableThenAutoSelectIt() {
        final PayerCost payerCostMock = mock(PayerCost.class);
        when(paymentPreference.getDefaultInstallments(payerCosts)).thenReturn(payerCostMock);

        solver.solve(listener, payerCosts);

        verify(userSelectionRepository).select(payerCostMock);
        verify(listener).onSelectedPayerCost();
    }

    @Test
    public void whenNonePayerCostsAreAvailableThenReturnEmptyOptions() {
        when(paymentPreference.getDefaultInstallments(payerCosts)).thenReturn(null);

        solver.solve(listener, payerCosts);

        verify(listener).onEmptyOptions();
    }

    @Test
    public void whenOnlyOnePayerCostIsAvailableThenAutoSelectIt() {
        final PayerCost payerCostMock = mock(PayerCost.class);
        when(paymentPreference.getInstallmentsBelowMax(payerCosts))
            .thenReturn(Collections.singletonList(payerCostMock));

        solver.solve(listener, payerCosts);

        verify(userSelectionRepository).select(payerCostMock);
        verify(listener).onSelectedPayerCost();
    }

    @Test
    public void whenMoreThanOnePayerCostIsAvailableThenAskForChoosing() {
        final List<PayerCost> filteredPayerCostsMock = new ArrayList<>();
        filteredPayerCostsMock.add(mock(PayerCost.class));
        filteredPayerCostsMock.add(mock(PayerCost.class));
        filteredPayerCostsMock.add(mock(PayerCost.class));
        when(paymentPreference.getDefaultInstallments(payerCosts)).thenReturn(null);
        when(paymentPreference.getInstallmentsBelowMax(payerCosts)).thenReturn(filteredPayerCostsMock);

        solver.solve(listener, payerCosts);

        verify(listener).displayInstallments(filteredPayerCostsMock);
        verifyNoMoreInteractions(userSelectionRepository);
    }
}