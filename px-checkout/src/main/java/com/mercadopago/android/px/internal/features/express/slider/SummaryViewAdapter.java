package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.view.SummaryView;
import com.mercadopago.android.px.internal.viewmodel.GoingToModel;
import java.util.List;

public class SummaryViewAdapter extends ViewAdapter<List<SummaryView.Model>, SummaryView> {

    private static final int NO_SELECTED = -1;

    private int currentIndex = NO_SELECTED;
    private SummaryView.Model currentModel;

    public SummaryViewAdapter(@NonNull final List<SummaryView.Model> data, @NonNull final SummaryView view) {
        super(data, view);
    }

    @Override
    public void updateData(final int index, final int payerCostSelected, final boolean userWantsToSplit) {
        final SummaryView.Model nextModel = data.get(index);
        if (!nextModel.equals(currentModel)) {
            view.update(nextModel);
        }
        currentIndex = index;
        currentModel = nextModel;
    }

    @Override
    public void updatePosition(float positionOffset, final int position) {
        if (positionOffset <= 0.0f || positionOffset > 1.0f) {
            return;
        }
        final GoingToModel goingTo = position < currentIndex ? GoingToModel.BACKWARDS : GoingToModel.FORWARD;
        final int nextIndex = goingTo == GoingToModel.BACKWARDS ? currentIndex - 1 : currentIndex + 1;
        if (!currentModel.equals(data.get(nextIndex))) {
            if (goingTo == GoingToModel.BACKWARDS) {
                positionOffset = 1.0f - positionOffset;
            }
            view.animateElementList(positionOffset);
        }
    }
}
