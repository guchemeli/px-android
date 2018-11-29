package com.mercadopago.android.px.internal.features.express.slider;

import android.view.View;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import java.util.List;

public interface PaymentMethodAdapter {
    void setModels(final List<PaymentMethodDescriptorView.Model> models);
    void showInstallmentsList();
    void updateData(final int currentIndex, final int payerCostSelected);
    void updatePosition(final float positionOffset, final int position);
    void updateViewsOrder(final View previousView, final View currentView, final View nextView);
}
