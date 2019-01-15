package com.mercadopago.android.px.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Amount configuration represents one hash_amount representation for cards. this DTO is strongly linked with a {@link
 * DiscountConfigurationModel}.
 */
@Keep
public class AmountConfiguration implements Serializable {

    /**
     * default selected payer cost configuration for single payment method selection
     */
    public int selectedPayerCostIndex;

    /**
     * Payer cost configuration for single payment method selection
     */
    @NonNull public List<PayerCost> payerCosts;

    /**
     * Split payment node it it applies.
     */
    @Nullable public Split split;

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

    public boolean allowSplit() {
        return split != null;
    }

    @NonNull
    public List<PayerCost> getAppliedPayerCost(final boolean userWantToSplit) {
        if (userWantToSplit && allowSplit()) {
            return split.getPayerCosts();
        } else {
            return getPayerCosts();
        }
    }

    @NonNull
    public PayerCost getCurrentPayerCost(final boolean userWantToSplit, final int userSelectedIndex) {
        if (userWantToSplit && allowSplit()) {
            return PayerCost.getPayerCost(split.getPayerCosts(), userSelectedIndex,
                split.selectedPayerCostIndex);
        } else {
            return PayerCost.getPayerCost(getPayerCosts(), userSelectedIndex,
                selectedPayerCostIndex);
        }
    }
}
