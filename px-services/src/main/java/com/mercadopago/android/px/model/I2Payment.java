package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;

/**
 *
 */
public interface I2Payment extends IPayment {

    @NonNull
    String getPaymentTypeId();

    @NonNull
    String getPaymentMethodId();

    void process(@NonNull final I2PaymentHandler handler);
}
