package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;

public class EmptyInstallmentsDescriptor extends PaymentMethodDescriptorView.Model {

    public static PaymentMethodDescriptorView.Model create() {
        return new EmptyInstallmentsDescriptor();
    }

    @Override
    public void updateSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final TextView textView) {
        spannableStringBuilder.append(TextUtil.SPACE);
    }
}
