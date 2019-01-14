package com.mercadopago.android.px.internal.features.express.animations;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;

public class HideViewOnAnimationListener implements Animation.AnimationListener {

    private final View targetView;

    public HideViewOnAnimationListener(@NonNull final View targetView) {
        this.targetView = targetView;
    }

    @Override
    public void onAnimationStart(final Animation animation) {
        // Do nothing.
    }

    @Override
    public void onAnimationEnd(final Animation animation) {
        targetView.setVisibility(View.GONE);
        targetView.clearAnimation();
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
        // Do nothing.
    }
}
