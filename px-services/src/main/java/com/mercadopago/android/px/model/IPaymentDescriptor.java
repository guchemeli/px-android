package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 */
public interface IPaymentDescriptor extends IPayment {

    /**
     * @return payment type id - associated with this payment
     */
    @Nullable
    String getPaymentTypeId();

    /**
     * @return payment method id - associated with this payment
     */
    @Nullable
    String getPaymentMethodId();

    /**
     * method to visit {@link IPaymentDescriptor} implementation.
     *
     * @param handler
     */
    void process(@NonNull final IPaymentDescriptorHandler handler);
}
