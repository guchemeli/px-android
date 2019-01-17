package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;

/**
 * this is not an interface, any change we want to make to process different kind of models won't be affected.
 */
public class I2PaymentHandler {

    public void process(@NonNull final I2Payment payment) {
        // do nothing
    }

    public void process(@NonNull final BusinessPayment businessPayment) {

    }
}
