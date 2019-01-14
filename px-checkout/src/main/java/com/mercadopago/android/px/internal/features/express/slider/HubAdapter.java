package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.view.View;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryView;
import java.util.ArrayList;
import java.util.List;

public class HubAdapter extends ViewAdapter<List<ViewAdapter<?, ? extends View>>, View> {

    public static class Model {

        @NonNull public final List<PaymentMethodDescriptorView.Model> paymentMethodDescriptorModels;
        @NonNull public final List<SummaryView.Model> summaryViewModels;
        @NonNull public final List<SplitPaymentHeaderAdapter.Model> splitModels;

        public Model(@NonNull final List<PaymentMethodDescriptorView.Model> paymentMethodDescriptorModels,
            @NonNull final List<SummaryView.Model> summaryViewModels,
            @NonNull final List<SplitPaymentHeaderAdapter.Model> splitModels) {
            this.paymentMethodDescriptorModels = paymentMethodDescriptorModels;
            this.summaryViewModels = summaryViewModels;
            this.splitModels = splitModels;
        }
    }

    public HubAdapter() {
        super(new ArrayList<ViewAdapter<?, ? extends View>>());
    }

    @Override
    public void showInstallmentsList() {
        for (final ViewAdapter adapter : data) {
            adapter.showInstallmentsList();
        }
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected, final boolean userWantsToSplit) {
        for (final ViewAdapter adapter : data) {
            adapter.updateData(currentIndex, payerCostSelected, userWantsToSplit);
        }
    }

    @Override
    public void updatePosition(final float positionOffset, final int position) {
        for (final ViewAdapter adapter : data) {
            adapter.updatePosition(positionOffset, position);
        }
    }

    @Override
    public void updateViewsOrder(@NonNull final View previousView, @NonNull final View currentView,
        @NonNull final View nextView) {
        for (final ViewAdapter adapter : data) {
            adapter.updateViewsOrder(previousView, currentView, nextView);
        }
    }
}
