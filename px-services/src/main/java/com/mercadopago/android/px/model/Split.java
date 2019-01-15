package com.mercadopago.android.px.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.util.TextUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Split DTO - represents the split payment amount and charges.
 */
@Keep
public class Split implements Serializable {

    /**
     * determines if the split payment is active by default or not.
     */
    public boolean defaultEnabled;

    /**
     * message to show in split label.
     */
    @NonNull public String message;

    /**
     * if the split payment is between a card and account money
     */
    @NonNull public List<PayerCost> payerCosts;

    /**
     * amount to pay with alternative payment method - always account money.
     */
    @NonNull public BigDecimal amount;

    @NonNull
    public String secondaryPaymentMethodId;

    /**
     * Default selected payer cost index
     */
    public int selectedPayerCostIndex;

    @Nullable
    public String primaryMethodDiscountToken;

    @Nullable
    public String secondaryMethodDiscountToken;

    @Nullable
    public BigDecimal primaryCouponAmount;

    @Nullable
    public BigDecimal secondaryCouponAmount;

    @NonNull
    public List<PayerCost>
    getPayerCosts() {
        return payerCosts == null ? new ArrayList<PayerCost>() : payerCosts;
    }

    @Nullable
    public Discount getSecondaryDiscount(@Nullable final Discount toOverride) {
        if (TextUtil.isEmpty(secondaryMethodDiscountToken) || toOverride == null) {
            return null;
        }
        return new Discount.Builder(secondaryMethodDiscountToken, toOverride.getCurrencyId(),
            secondaryCouponAmount)
            .setAmountOff(toOverride.getAmountOff())
            .setName(toOverride.getName())
            .setPercentOff(toOverride.getPercentOff())
            .build();
    }

    @Nullable
    public Discount getPrimaryDiscount(@Nullable final Discount toOverride) {
        if (TextUtil.isEmpty(primaryMethodDiscountToken) || toOverride == null) {
            return null;
        }
        return new Discount.Builder(primaryMethodDiscountToken, toOverride.getCurrencyId(),
            primaryCouponAmount)
            .setAmountOff(toOverride.getAmountOff())
            .setName(toOverride.getName())
            .setPercentOff(toOverride.getPercentOff())
            .build();
    }
}
