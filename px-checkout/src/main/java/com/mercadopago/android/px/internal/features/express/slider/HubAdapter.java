package com.mercadopago.android.px.internal.features.express.slider;

import android.view.View;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryView;
import java.util.ArrayList;
import java.util.List;

public class HubAdapter extends PaymentMethodAdapter<List<PaymentMethodAdapter>, View> {

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
        super(new ArrayList<PaymentMethodAdapter>());
    }

    @Override
    public void showInstallmentsList() {
        for (final PaymentMethodAdapter adapter : data) {
            adapter.showInstallmentsList();
        }
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected) {
        for (final PaymentMethodAdapter adapter : data) {
            adapter.updateData(currentIndex, payerCostSelected);
        }
    }

    @Override
    public void updatePosition(final float positionOffset, final int position) {
        for (final PaymentMethodAdapter adapter : data) {
            adapter.updatePosition(positionOffset, position);
        }
    }

    @Override
    public void updateViewsOrder(final View previousView, final View currentView, final View nextView) {
        for (final PaymentMethodAdapter adapter : data) {
            adapter.updateViewsOrder(previousView, currentView, nextView);
        }
    }
}
