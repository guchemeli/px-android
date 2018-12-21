package com.mercadopago.android.px.internal.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CompactComponent<Props, Actions> {

    @NonNull protected Props props;
    @Nullable private final Actions actions;

    public CompactComponent(@NonNull final Props props) {
        this(props, null);
    }

    public CompactComponent(@NonNull final Props props, @Nullable final Actions callBack) {
        actions = callBack;
        this.props = props;
    }

    public View setProps(@NonNull final Props props, final ViewGroup parent) {
        this.props = props;
        return render(parent);
    }

    public abstract View render(@Nonnull final ViewGroup parent);

    @Nullable
    protected Actions getActions() {
        return actions;
    }
}
