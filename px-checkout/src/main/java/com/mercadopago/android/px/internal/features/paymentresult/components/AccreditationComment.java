package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.view.ActionDispatcher;
import com.mercadopago.android.px.internal.view.Component;

public class AccreditationComment extends Component<String, Void> {

    /* default */ AccreditationComment(@NonNull final String comment, @NonNull final ActionDispatcher dispatcher) {
        super(comment, dispatcher);
    }
}
