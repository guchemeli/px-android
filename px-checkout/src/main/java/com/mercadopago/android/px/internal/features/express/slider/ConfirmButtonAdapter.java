package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import com.mercadolibre.android.ui.widgets.MeliButton;

public class ConfirmButtonAdapter extends ViewAdapter<Integer, MeliButton> {

    public ConfirmButtonAdapter(final Integer size,
        @NonNull final MeliButton view) {
        super(size, view);
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected, final boolean userWantsToSplit) {
        if (isLastElement(currentIndex)) {
            view.setState(MeliButton.State.DISABLED);
        } else {
            view.setState(MeliButton.State.NORMAL);
        }
    }

    private boolean isLastElement(final int position) {
        return position >= data - 1;
    }
}
