package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import com.mercadopago.android.px.internal.view.LabeledSwitch;
import java.math.BigDecimal;
import java.util.List;

public class SplitPaymentHeaderAdapter extends ViewAdapter<List<SplitPaymentHeaderAdapter.Model>, LabeledSwitch>
    implements CompoundButton.OnCheckedChangeListener {

    @NonNull private final SplitListener splitListener;
    private int current = 0;

    public interface SplitListener {
        void onSplitChanged(final boolean isChecked, final int index);
    }

    public abstract static class Model {
        public abstract void visit(final LabeledSwitch labeledSwitch);
    }

    public static final class Empty extends Model {
        @Override
        public void visit(final LabeledSwitch labeledSwitch) {
            labeledSwitch.setVisibility(View.GONE);
        }
    }

    public static final class Split extends Model {

        private final String message;
        private final BigDecimal balance;
        private boolean isChecked;

        public Split(final String message, final BigDecimal balance, final boolean isChecked) {
            this.message = message;
            this.balance = balance;
            this.isChecked = isChecked;
        }

        @Override
        public void visit(final LabeledSwitch labeledSwitch) {
            labeledSwitch.setVisibility(View.VISIBLE);
            labeledSwitch.setText("Some label");
            labeledSwitch.setChecked(true);
        }
    }

    public SplitPaymentHeaderAdapter(@NonNull final List<Model> data, @Nullable final LabeledSwitch view,
        @NonNull final SplitListener splitListener) {
        super(data, view);
        this.splitListener = splitListener;
        view.setOnCheckedChanged(this);
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected) {
        // Empty data case
        if (currentIndex >= data.size()) {
            view.setVisibility(View.GONE);
            return;
        }

        current = currentIndex;
        final Model model = data.get(currentIndex);
        model.visit(view);
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        splitListener.onSplitChanged(isChecked, current);
    }
}
