package com.mercadopago.android.px.model;

import android.support.annotation.Keep;
import java.io.Serializable;

/**
 * Split DTO - represents the split payment amount and charges.
 */
@Keep
public class Split implements Serializable {

    /**
     * determines if the split payment is active by default or not.
     */
    public boolean defaultEnabled;

    public PaymentMethodConfiguration primaryPaymentMethod;

    public PaymentMethodConfiguration secondaryPaymentMethod;

}
