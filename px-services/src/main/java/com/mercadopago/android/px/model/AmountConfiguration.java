package com.mercadopago.android.px.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Amount configuration represents one hash_amount representation. this DTO is strongly linked with a {@link
 * DiscountConfigurationModel}.
 */
@Keep
public class AmountConfiguration implements Serializable {

    public static final int NO_SELECTED = -1;

    /**
     * default selected payer cost configuration for single payment method selection
     */
    public int selectedPayerCostIndex;

    /**
     * Payer cost configuration for single payment method selection
     */
    public List<PayerCost> payerCosts;

    /**
     * Split payment node it it applies.
     */
    public Split split;

    /**
     * null if there is no discount configuration associated.
     */
    @Nullable public String discountToken;

    @NonNull
    public List<PayerCost> getPayerCosts() {
        return payerCosts == null ? new ArrayList<PayerCost>() : payerCosts;
    }

    public int getDefaultPayerCostIndex() {
        return selectedPayerCostIndex;
    }

    public PayerCost getPayerCost(final int userSelectedPayerCost) {
        if (userSelectedPayerCost == NO_SELECTED) {
            return payerCosts.get(selectedPayerCostIndex);
        } else {
            return payerCosts.get(userSelectedPayerCost);
        }
    }
}
