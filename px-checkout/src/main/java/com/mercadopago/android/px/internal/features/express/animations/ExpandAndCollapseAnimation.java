package com.mercadopago.android.px.internal.features.express.animations;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.mercadopago.android.px.R;

public class ExpandAndCollapseAnimation {

    private final Animation expandAnimation;
    private final Animation collapseAnimation;
    private final View targetView;

    public ExpandAndCollapseAnimation(@NonNull final View view) {
        targetView = view;
        expandAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.px_anim_expand);
        collapseAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.px_anim_collapse);
        final Animation.AnimationListener listener = new HideViewOnAnimationListener(view);
        collapseAnimation.setAnimationListener(listener);
    }

    /**
     * Expand the view if it is gone
     */
    public void expand() {
        if (targetView.getVisibility() == View.GONE) {
            targetView.setAnimation(expandAnimation);
            targetView.startAnimation(expandAnimation);
        }
    }

    /**
     * Collapse the view if is visible.
     */
    public void collapse() {
        if (targetView.getVisibility() == View.VISIBLE) {
            targetView.setAnimation(collapseAnimation);
            targetView.startAnimation(collapseAnimation);
        }
    }
}
