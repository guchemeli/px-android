package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.view.View;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import java.util.List;

public class ConfirmButtonAdapter extends PaymentMethodAdapter<Integer, MeliButton> {


    public ConfirmButtonAdapter(final Integer size,
        @NonNull final MeliButton view) {
        super(size, view);
    }

    @Override
    public void showInstallmentsList() {
        //Nothing to do here
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected) {
        if (isLastElement(currentIndex)) {
            view.setState(MeliButton.State.DISABLED);
        } else {
            view.setState(MeliButton.State.NORMAL);
        }
    }

    @Override
    public void updatePosition(final float positionOffset, final int position) {
        //Nothing to do here
    }

    @Override
    public void updateViewsOrder(final View previousView, final View currentView, final View nextView) {
        //Nothing to do here
    }

    private boolean isLastElement(final int position) {
        return position >= data - 1;
    }
}
