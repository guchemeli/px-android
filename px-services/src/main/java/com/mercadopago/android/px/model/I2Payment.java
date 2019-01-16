package com.mercadopago.android.px.model;

import android.support.annotation.Nullable;

public interface I2Payment extends IPayment {

    @Nullable
    String getPaymentTypeId();

    @Nullable
    String getPaymentMethodId();

}
