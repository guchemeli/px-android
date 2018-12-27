package com.mercadopago.android.px.internal.features.installments;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.PaymentPreference;
import java.util.List;

public class PayerCostSolver {

    @NonNull private final PaymentPreference paymentPreference;
    @NonNull private final UserSelectionRepository userSelectionRepository;

    /**
     * Constructor
     *
     * @param paymentPreference The payment configuration.
     * @param userSelectionRepository The user selection repository.
     */
    public PayerCostSolver(@NonNull final PaymentPreference paymentPreference,
        @NonNull final UserSelectionRepository userSelectionRepository) {
        this.paymentPreference = paymentPreference;
        this.userSelectionRepository = userSelectionRepository;
    }

    /**
     * Dispatches all payer costs possible scenarios.
     *
     * @param listener The entity that will handle all possible flows.
     * @param payerCosts The list of payer costs.
     */
    public void solve(@NonNull final PayerCostListener listener, @NonNull final List<PayerCost> payerCosts) {
        final PayerCost defaultPayerCost = paymentPreference.getDefaultInstallments(payerCosts);
        final List<PayerCost> filteredPayerCosts = paymentPreference.getInstallmentsBelowMax(payerCosts);

        if (defaultPayerCost == null) {
            if (filteredPayerCosts == null || filteredPayerCosts.isEmpty()) {
                listener.onEmptyOptions();
            } else if (filteredPayerCosts.size() == 1) {
                userSelectionRepository.select(filteredPayerCosts.get(0));
                listener.onSelectedPayerCost();
            } else {
                listener.displayInstallments(filteredPayerCosts);
            }
        } else {
            userSelectionRepository.select(defaultPayerCost);
            listener.onSelectedPayerCost();
        }
    }
}
