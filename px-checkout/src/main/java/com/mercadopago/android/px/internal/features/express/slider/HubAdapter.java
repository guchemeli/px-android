package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.view.View;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryView;
import java.util.ArrayList;
import java.util.List;

public class HubAdapter extends ViewAdapter<List<ViewAdapter<?, ? extends View>>, View> {

    public static class Model {
        private final List<PaymentMethodDescriptorView.Model> paymentMethodDescriptorModels;
        private final List<SummaryView.Model> summaryViewModels;

        public Model(final List<PaymentMethodDescriptorView.Model> paymentMethodDescriptorModels,
            final List<SummaryView.Model> summaryViewModels) {
            this.paymentMethodDescriptorModels = paymentMethodDescriptorModels;
            this.summaryViewModels = summaryViewModels;
        }

        public List<PaymentMethodDescriptorView.Model> getPaymentMethodDescriptorModels() {
            return paymentMethodDescriptorModels;
        }

        public List<SummaryView.Model> getSummaryViewModels() {
            return summaryViewModels;
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
    public void updateData(final int currentIndex, final int payerCostSelected) {
        for (final ViewAdapter adapter : data) {
            adapter.updateData(currentIndex, payerCostSelected);
        }
    }

    @Override
    public void updatePosition(final float positionOffset, final int position) {
        for (final ViewAdapter adapter : data) {
            adapter.updatePosition(positionOffset, position);
        }
    }

    @Override
    public void updateViewsOrder(@NonNull final View previousView, @NonNull final View currentView, @NonNull final View nextView) {
        for (final ViewAdapter adapter : data) {
            adapter.updateViewsOrder(previousView, currentView, nextView);
        }
    }
}
