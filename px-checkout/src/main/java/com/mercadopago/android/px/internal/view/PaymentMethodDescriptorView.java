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
            updateInstallmentsDescription(model, spannableStringBuilder);
            updateTotalAmountDescription(model, spannableStringBuilder);
            updateInterestDescription(model, spannableStringBuilder);
            updateCFT(model, spannableStringBuilder);
        }

        setText(spannableStringBuilder);
    }

    private void updateInstallmentsDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        model.updateInstallmentsDescriptionSpannable(spannableStringBuilder, getContext(), this);
    }

    private void updateInterestDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        model.updateInterestDescriptionSpannable(spannableStringBuilder, getContext());
    }

    private void updateTotalAmountDescription(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {
        model.updateTotalAmountDescriptionSpannable(spannableStringBuilder, getContext());
    }

    private void updateCFT(@NonNull final Model model,
        @NonNull final SpannableStringBuilder spannableStringBuilder) {

        model.updateCFTSpannable(spannableStringBuilder, getContext());
    }

    public abstract static class Model {
        public static final int SELECTED_PAYER_COST_NONE = -1;
        private String currencyId;
        private List<PayerCost> payerCostList;
        private int defaultPayerCost = SELECTED_PAYER_COST_NONE;
        private int currentPayerCost = SELECTED_PAYER_COST_NONE;

        protected Model(@NonNull final String currencyId, @Nullable final List<PayerCost> payerCostList) {
            this.currencyId = currencyId;
            this.payerCostList = payerCostList;
        }

        protected Model(@NonNull final String currencyId, @NonNull final List<PayerCost> payerCostList,
            final int defaultPayerCost) {
            this.currencyId = currencyId;
            this.payerCostList = payerCostList;
            this.defaultPayerCost = defaultPayerCost;
            currentPayerCost = defaultPayerCost;
        }

        protected Model() {
        }

        public boolean isEmpty() {
            return currencyId == null || payerCostList == null;
        }

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

        public void updateInstallmentsDescriptionSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context, @NonNull final TextView textView) {
            //Do nothing
        }

        public void updateInterestDescriptionSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context) {
            //Do nothing
        }

        public void updateTotalAmountDescriptionSpannable(
            @NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context) {
            //Do nothing
        }

        public void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final Context context) {
            //Do nothing
        }
    }
}
