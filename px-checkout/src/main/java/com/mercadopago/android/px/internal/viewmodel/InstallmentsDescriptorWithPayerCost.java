package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.textformatter.AmountLabeledFormatter;
import com.mercadopago.android.px.internal.util.textformatter.CFTFormatter;
import com.mercadopago.android.px.internal.util.textformatter.InterestFormatter;
import com.mercadopago.android.px.internal.util.textformatter.PayerCostFormatter;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;
import java.util.List;

/**
 * Model used to instantiate InstallmentsDescriptorView For payment methods with payer costs: credit_card only
 */
public final class InstallmentsDescriptorWithPayerCost extends PaymentMethodDescriptorView.Model {

    @NonNull
    public static PaymentMethodDescriptorView.Model createFrom(
        @NonNull final PaymentSettingRepository paymentConfiguration,
        @NonNull final AmountConfiguration amountConfiguration) {

        final CheckoutPreference checkoutPreference = paymentConfiguration.getCheckoutPreference();
        final String currencyId = checkoutPreference.getSite().getCurrencyId();

        return new InstallmentsDescriptorWithPayerCost(currencyId,
            amountConfiguration.getPayerCosts(), amountConfiguration.getDefaultPayerCostIndex());
    }

    private InstallmentsDescriptorWithPayerCost(@NonNull final String currencyId,
        @NonNull final List<PayerCost> payerCostList, final int currentPayerCost) {
        super(currencyId, payerCostList, currentPayerCost);
    }

    @Override
    public void updateSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final TextView textView) {
        updateInstallment(spannableStringBuilder, context, textView);
        updateTotalAmountDescriptionSpannable(spannableStringBuilder, context);
        updateInterestDescriptionSpannable(spannableStringBuilder, context);
        updateCFTSpannable(spannableStringBuilder, context);
    }

    private void updateInstallment(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context,
        @NonNull final TextView textView) {


        final Spannable amount = TextFormatter.withCurrencyId(getCurrencyId())
            .amount(getCurrentPayerCost().getInstallmentAmount())
            .normalDecimals()
            .into(textView)
            .toSpannable();

        final AmountLabeledFormatter amountLabeledFormatter = new AmountLabeledFormatter(spannableStringBuilder, context)
            .withInstallment(getCurrentPayerCost().getInstallments())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_black))
            .withSemiBoldStyle();
        amountLabeledFormatter.apply(amount);
    }

    /**
     * Updates total amount the user will pay with credit card, only if there are interests involved.
     */

    private void updateTotalAmountDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        if (BigDecimal.ZERO.compareTo(getCurrentPayerCost().getInstallmentRate()) < 0) {
            final PayerCostFormatter payerCostFormatter =
                new PayerCostFormatter(spannableStringBuilder, context, getCurrentPayerCost(), getCurrencyId())
                    .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey));
            payerCostFormatter.apply();
        }
    }

    private void updateInterestDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        if (hasMultipleInstallments() && BigDecimal.ZERO.compareTo(getCurrentPayerCost().getInstallmentRate()) == 0) {
            final InterestFormatter interestFormatter = new InterestFormatter(spannableStringBuilder, context)
                .withTextColor(ContextCompat.getColor(context, R.color.px_discount_description));
            interestFormatter.apply();
        }
    }

    private void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {

        final CFTFormatter cftFormatter = new CFTFormatter(spannableStringBuilder, context, getCurrentPayerCost())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey));
        cftFormatter.build();
    }
}
