package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;
import com.mercadopago.android.px.model.PayerCost;
import java.util.List;

public class PaymentMethodDescriptorView extends MPTextView {

    public PaymentMethodDescriptorView(final Context context) {
        this(context, null);
    }

    public PaymentMethodDescriptorView(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentMethodDescriptorView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void update(@NonNull final Model model) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        if (model.isEmpty()) {
            spannableStringBuilder.append(" ");
        } else {
            model.updateSpannable(spannableStringBuilder, getContext(), this);
        }

        setText(spannableStringBuilder);
    }

    public static class Model {
        public static final int SELECTED_PAYER_COST_NONE = -1;
        private int defaultPayerCost = SELECTED_PAYER_COST_NONE;
        private int currentPayerCost = SELECTED_PAYER_COST_NONE;
        @Nullable private String currencyId;
        @Nullable private List<PayerCost> payerCostList;

        public Model(@Nullable final String currencyId, @Nullable final List<PayerCost> payerCostList) {
            this(currencyId, payerCostList, SELECTED_PAYER_COST_NONE);
        }

        public Model() {
            this(null, null);
        }

        public Model(@NonNull final String currencyId,
            @Nullable final List<PayerCost> payerCostList,
            final int defaultPayerCost) {
            this.currencyId = currencyId;
            this.payerCostList = payerCostList;
            this.defaultPayerCost = defaultPayerCost;
            currentPayerCost = defaultPayerCost;
        }

        public boolean isEmpty() {
            return currencyId == null || payerCostList == null;
        }

        @Nullable
        public String getCurrencyId() {
            return currencyId;
        }

        @Nullable
        public PayerCost getCurrentPayerCost() {
            return payerCostList == null ? null : payerCostList.get(currentPayerCost);
        }

        public void setCurrentPayerCost(final int currentPayerCost) {
            this.currentPayerCost = currentPayerCost == SELECTED_PAYER_COST_NONE ?
                defaultPayerCost : currentPayerCost;
        }

        protected boolean hasMultipleInstallments() {
            final PayerCost payerCost = getCurrentPayerCost();
            return payerCost != null && payerCost.getInstallments() > 1;
        }

        public boolean hasPayerCostList() {
            return payerCostList != null && payerCostList.size() > 1;
        }

        public void updateSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context, @NonNull final TextView textView) {
            //Do nothing
        }
    }
}
