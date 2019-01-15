package com.mercadopago.android.px.internal.viewmodel;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.mercadopago.android.px.internal.util.CurrenciesUtil;
import java.math.BigDecimal;

public class AmountFormat {

    protected String currencyId;
    protected BigDecimal amount;

    /* default */ AmountFormat(final String currencyId,
        final BigDecimal amount) {
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    /* default */ CharSequence insertSpannedAmountInText(final String title,
        final SpannableStringBuilder spannedAmount) {
        final String formattedAmount = CurrenciesUtil.getLocalizedAmountWithCurrencySymbol(amount, currencyId, true);
        CharSequence result = title;

        if (title.contains(formattedAmount)) {
            final String formattedText = title.replace(formattedAmount, "*");
            final int index = formattedText.indexOf("*");
            final String firstSubstring = formattedText.substring(0, index);
            final String secondSubstring = formattedText.substring(index + 1, formattedText.length());

            final CharSequence auxSubstring1 = TextUtils.concat(firstSubstring, "\n");
            final CharSequence auxSubstring2 = TextUtils.concat(auxSubstring1, spannedAmount);
            result = TextUtils.concat(auxSubstring2, secondSubstring);
        }

        return result;
    }
}
