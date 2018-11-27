package com.mercadopago.android.px.internal.viewmodel;

import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;

public class EmptyInstallmentsDescriptor extends PaymentMethodDescriptorView.Model {

    protected EmptyInstallmentsDescriptor() {
        super();
    }

    public static PaymentMethodDescriptorView.Model create() {
        return new EmptyInstallmentsDescriptor();
    }
}
