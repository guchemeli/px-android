package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.LabeledSwitch;
import java.math.BigDecimal;
import java.util.List;

public class SplitPaymentHeaderAdapter extends ViewAdapter<List<SplitPaymentHeaderAdapter.Model>, LabeledSwitch> {

    public static class Model {
        /* default */ @Nullable final String label;
        /* default */ @Nullable final BigDecimal balance;
        /* default */ final boolean isVisible;

        public Model(@Nullable final String label, @Nullable final BigDecimal balance, final boolean isVisible) {
            this.label = label;
            this.balance = balance;
            this.isVisible = isVisible;
        }
    }

    public SplitPaymentHeaderAdapter(@NonNull final List<Model> data, @Nullable final LabeledSwitch view) {
        super(data, view);
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected) {
        view.setVisibility(View.GONE);
        view.setText(TextUtil.EMPTY);
//        final Model model = data.get(currentIndex);
//        view.setVisibility(model.isVisible ? View.VISIBLE : View.GONE);
//        view.setText(model.label);
    }
}
