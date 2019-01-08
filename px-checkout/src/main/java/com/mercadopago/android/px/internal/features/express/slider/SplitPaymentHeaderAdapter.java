package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.Nullable;
import android.view.View;
import com.mercadopago.android.px.internal.view.LabeledSwitch;

/* default */ class SplitPaymentHeaderAdapter extends PaymentMethodAdapter<Void, LabeledSwitch> {

    public SplitPaymentHeaderAdapter(@Nullable final LabeledSwitch view) {
        super(null, view);
    }

    @Override
    public void showInstallmentsList() {

    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected) {

    }

    @Override
    public void updatePosition(final float positionOffset, final int position) {

    }

    @Override
    public void updateViewsOrder(final View previousView, final View currentView, final View nextView) {

    }
}
