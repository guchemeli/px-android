package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodConfiguration implements Serializable {

    /**
     * amount to pay with alternative payment method - always account money.
     */
    public BigDecimal amount;

    public Discount discount;

    /**
     * if the split payment is between a card and account money
     */
    public List<PayerCost> payerCosts;

    /**
     * Default selected payer cost index
     */
    public int selectedPayerCostIndex;

    /**
     * message to show in split label.
     */
    public String message;

    @SerializedName("id")
    public String paymentMethodId;

    @NonNull
    public List<PayerCost>
    getPayerCosts() {
        return payerCosts == null ? new ArrayList<PayerCost>() : payerCosts;
    }

    @NonNull
    public BigDecimal getVisibleAmountToPay() {
        return discount == null ? amount
            : amount.subtract(discount.getCouponAmount());
    }
}
