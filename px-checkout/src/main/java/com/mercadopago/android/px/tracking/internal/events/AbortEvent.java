package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.views.ViewTracker;

public class AbortEvent extends EventTracker {

    private static final String ABORT = "/abort";

    @NonNull private final ViewTracker viewTracker;

    public AbortEvent(@NonNull final ViewTracker viewTracker) {
        this.viewTracker = viewTracker;
    }

    @NonNull
    @Override
    public String getEventPath() {
        return viewTracker.getViewPath() + ABORT;
    }
}