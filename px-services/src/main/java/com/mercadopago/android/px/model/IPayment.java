package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;

/**
 * Please use {@link IPaymentDescriptor}
 *
 * @deprecated new interface need payment method id and type.
 */
@Deprecated
public interface IPayment extends Serializable {

    @NonNull
    String getPaymentStatus();

    @NonNull
    String getPaymentStatusDetail();

    @Nullable
    Long getId();

    @Nullable
    String getStatementDescription();
}
