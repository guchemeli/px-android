package com.mercadopago.android.px.internal.features.installments;

import com.mercadopago.android.px.model.PayerCost;
import java.util.List;

public interface PayerCostListener {

    /**
     * Handles empty list of payer costs.
     */
    void onEmptyOptions();

    /**
     * Handles the automatic selection scenarios such as only option and default.
     */
    void onSelectedPayerCost();

    /**
     * Displays an installments chooser from a given list.
     *
     * @param payerCosts The valid list of payer costs.
     */
    void displayInstallments(final List<PayerCost> payerCosts);
}
