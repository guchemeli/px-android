package com.mercadopago.android.px.internal.util;

import android.support.annotation.Nullable;

public final class TextUtil {

    public static final String EMPTY = "";

    private TextUtil() {
        throw new AssertionError("Util classes shouldn't be instantiated.");
    }

    public static boolean isEmpty(@Nullable final CharSequence text) {
        return text == null || text.length() <= 0;
    }

    public static boolean isNotEmpty(@Nullable final CharSequence text) {
        return !isEmpty(text);
    }
}
