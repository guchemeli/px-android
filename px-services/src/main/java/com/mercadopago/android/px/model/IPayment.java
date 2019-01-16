package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;

/**
 * Please use {@link I2Payment}
 *
 * @deprecated new interface need payment method id and type.
 */
@Deprecated
public interface IPayment extends Serializable {

    @Nullable
    Long getId();

    @Nullable
    String getStatementDescription();

    @NonNull
    String getPaymentStatus();

    @NonNull
    String getPaymentStatusDetail();
}
