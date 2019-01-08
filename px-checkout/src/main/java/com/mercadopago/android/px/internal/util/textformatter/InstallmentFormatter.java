package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadolibre.android.ui.font.Font;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;

public class InstallmentFormatter extends ChainFormatter {

    private int installment;
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;
    private boolean semiBoldStyle;

    public InstallmentFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
        semiBoldStyle = false;
    }

    public InstallmentFormatter withInstallment(final int installment) {
        this.installment = installment;
        return this;
    }

    public InstallmentFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public InstallmentFormatter withSemiBoldStyle() {
        semiBoldStyle = true;
        return this;
    }

    @Override
    public Spannable apply(@NonNull final CharSequence amount) {
        final int indexStart = spannableStringBuilder.length();
        if (installment != 0) {

            final int holder = R.string.px_amount_with_installments_holder;
            final String installmentAmount = String.valueOf(installment);
            final int holderFixedCharactersLength = 2;

            final int length = installmentAmount.length() + holderFixedCharactersLength + amount.length();

            final CharSequence charSequence = context.getResources().getString(holder, installmentAmount, amount);

            spannableStringBuilder.append(charSequence);

            updateTextColor(indexStart, indexStart + length);
            updateTextStyle(indexStart, indexStart + length);
        } else {
            final int holder = R.string.px_string_holder;
            final CharSequence charSequence = context.getResources().getString(holder, amount);
            spannableStringBuilder.append(charSequence);

            final int length = charSequence.length();

            updateTextColor(indexStart, indexStart + length);
            updateTextStyle(indexStart, indexStart + length);
        }

        return spannableStringBuilder;
    }

    private void updateTextColor(final int indexStart, final int indexEnd) {
        if (textColor != 0) {
            ViewUtils.setColorInSpannable(textColor, indexStart, indexEnd, spannableStringBuilder);
        }
    }

    private void updateTextStyle(final int indexStart, final int indexEnd) {
        if (semiBoldStyle) {
            ViewUtils.setSemiBoldFontInSpannable(indexStart, indexEnd, spannableStringBuilder, context);
        } else {
            ViewUtils.setFontInSpannable(indexStart, indexEnd, spannableStringBuilder, Font.REGULAR.getFontPath(), context);
        }
    }
}
