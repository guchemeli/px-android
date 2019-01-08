package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class PaymentMethodAdapter<T, V extends View> {

    @NonNull protected T data;
    @Nullable protected final V view;

    public PaymentMethodAdapter(@NonNull final T data) {
        this(data, null);
    }

    public PaymentMethodAdapter(@NonNull final T data, @Nullable final V view) {
        this.data = data;
        this.view = view;
    }

    public void update(@NonNull final T newData) {
        data = newData;
    }

    public abstract void showInstallmentsList();

    public abstract void updateData(int currentIndex, int payerCostSelected);

    public abstract void updatePosition(float positionOffset, int position);

    public abstract void updateViewsOrder(View previousView, View currentView, View nextView);
}
