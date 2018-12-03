package com.mercadopago.android.px.testcheckout.flows;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.core.MercadoPagoCheckout;

public class ExpressCheckoutTestFlow extends TestFlow {

    public ExpressCheckoutTestFlow() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public ExpressCheckoutTestFlow(@NonNull final MercadoPagoCheckout mercadoPagoCheckout, @NonNull final Context context) {
        super(mercadoPagoCheckout, context);
    }
}
