package com.mercadopago.android.px.internal.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.internal.util.CurrenciesUtil;
import java.math.BigDecimal;

public class HeaderTitleFormatter extends AmountFormat {

    private final String paymentMethodName;

    public HeaderTitleFormatter(@NonNull final String currencyId,
        @NonNull final BigDecimal amount,
        @Nullable final String paymentMethodName) {
        super(currencyId, amount);
        this.paymentMethodName = paymentMethodName;
    }

    public CharSequence formatTextWithAmount(final String text) {
        if (paymentMethodName == null) {
            return formatTextWithOnlyAmount(text);
        } else {
            return formatTextWithAmountAndName(text);
        }
    }

    private CharSequence formatTextWithOnlyAmount(final String text) {
        final SpannableStringBuilder spannableAmount =
            CurrenciesUtil.getSpannableAmountWithSymbolWithoutZeroDecimals(currencyId, amount);
        return insertSpannedAmountInText(text, spannableAmount);
    }

    private CharSequence formatTextWithAmountAndName(final String text) {
        final String formattedAmount = CurrenciesUtil.getLocalizedAmountWithCurrencySymbol(amount, currencyId, true);
        final String formattedText = String.format(text, paymentMethodName, formattedAmount);
        return formatTextWithOnlyAmount(formattedText);
    }
}
