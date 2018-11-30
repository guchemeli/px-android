package com.mercadopago.android.px.internal.features.express.slider;

import android.view.View;

import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.internal.view.PaymentMethodHeaderView;
import com.mercadopago.android.px.internal.view.TitlePager;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodAdapterImpl implements PaymentMethodAdapter {

    private final List<PaymentMethodAdapter> adapters;

    public PaymentMethodAdapterImpl(final TitlePager titlePager,
        final PaymentMethodHeaderView paymentMethodHeaderView, final MeliButton confirmButton) {
        adapters = new ArrayList<>();
        adapters.add(new TitlePagerAdapter(titlePager));
        adapters.add(new PaymentMethodHeaderAdapter(paymentMethodHeaderView));
        adapters.add(new ConfirmButtonAdapter(confirmButton));
    }

    @Override
    public void setModels(final List<PaymentMethodDescriptorView.Model> models) {
        for (final PaymentMethodAdapter adapter : adapters) {
            adapter.setModels(models);
        }
    }

    @Override
    public void showInstallmentsList() {
        for (final PaymentMethodAdapter adapter : adapters) {
            adapter.showInstallmentsList();
        }
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected) {
        for (final PaymentMethodAdapter adapter : adapters) {
            adapter.updateData(currentIndex, payerCostSelected);
        }
    }

    @Override
    public void updatePosition(final float positionOffset, final int position) {
        for (final PaymentMethodAdapter adapter : adapters) {
            adapter.updatePosition(positionOffset, position);
        }
    }

    @Override
    public void updateViewsOrder(final View previousView, final View currentView, final View nextView) {
        for (final PaymentMethodAdapter adapter : adapters) {
            adapter.updateViewsOrder(previousView, currentView, nextView);
        }
    }
}
