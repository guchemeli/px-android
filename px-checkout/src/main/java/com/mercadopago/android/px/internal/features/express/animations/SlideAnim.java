package com.mercadopago.android.px.internal.features.express.animations;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import com.mercadopago.android.px.R;

public class SlideAnim {

    @NonNull private final View target;

    private Position pos;

    private enum Position {
        DOWN,
        UP
    }

    public SlideAnim(@NonNull final View target) {
        this.target = target;
        pos = Position.UP;
    }

    public void slideDown(final float initialPosition, final float endPosition) {
        if (pos == Position.UP && (hasEndedAnim())) {
            pos = Position.DOWN;
            final ObjectAnimator slideDownAnim =
                ObjectAnimator.ofFloat(target, "y", initialPosition, endPosition);
            slideDownAnim
                .setDuration((long) target.getContext().getResources().getInteger(R.integer.px_default_animation_time));
            slideDownAnim.start();
        }
    }

    public void slideUp(final float initialPosition, final float endPosition) {
        if (pos == Position.DOWN && hasEndedAnim()) {
            pos = Position.UP;
            final ObjectAnimator slideUpAnim =
                ObjectAnimator.ofFloat(target, "y", initialPosition, endPosition);
            slideUpAnim.setDuration(
                (long) target.getContext().getResources().getInteger(R.integer.px_default_animation_time));
            slideUpAnim.start();
        }
    }

    private boolean hasEndedAnim() {
        return target.getAnimation() != null && target.getAnimation().hasEnded()
            || target.getAnimation() == null;
    }
}
