package com.mercadopago.android.px.internal.features.express.animations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import com.mercadopago.android.px.R;

public class FadeAnimator {

    private final int normalDuration;
    private final int fastDuration;
    private final int fastestDuration;

    public FadeAnimator(@NonNull final Context context) {
        normalDuration = context.getResources().getInteger(R.integer.px_long_animation_time);
        fastDuration = context.getResources().getInteger(R.integer.px_short_animation_time);
        fastestDuration = context.getResources().getInteger(R.integer.px_shortest_animation_time);
    }

    private AlphaAnimation createFade(final int from, final int to) {
        final AlphaAnimation anim = new AlphaAnimation(from, to);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setFillAfter(true);
        return anim;
    }

    private boolean shouldFadeIn(@NonNull final View viewToAnimate) {
        return viewToAnimate.getVisibility() == View.GONE && hasEndedAnim(viewToAnimate);
    }

    private boolean shouldFadeOut(@NonNull final View viewToAnimate) {
        return viewToAnimate.getVisibility() == View.VISIBLE && hasEndedAnim(viewToAnimate);
    }

    private boolean hasEndedAnim(@NonNull final View viewToAnimate) {
        return viewToAnimate.getAnimation() != null && viewToAnimate.getAnimation().hasEnded()
            || viewToAnimate.getAnimation() == null;
    }

    public void fadeIn(@NonNull final View viewToAnimate) {
        if (!shouldFadeIn(viewToAnimate)) {
            return;
        }
        final AlphaAnimation fade = createFade(0, 1);
        viewToAnimate.setVisibility(View.VISIBLE);
        fade.setDuration(normalDuration);
        viewToAnimate.startAnimation(fade);
    }

    public void fadeInFastest(@NonNull final View viewToAnimate) {
        if (!shouldFadeIn(viewToAnimate)) {
            return;
        }
        final AlphaAnimation fade = createFade(0, 1);
        viewToAnimate.setVisibility(View.VISIBLE);
        fade.setDuration(fastestDuration);
        viewToAnimate.startAnimation(fade);
    }

    public void fadeOut(@NonNull final View viewToAnimate) {
        if (!shouldFadeOut(viewToAnimate)) {
            return;
        }
        viewToAnimate.clearAnimation();
        viewToAnimate.setVisibility(View.VISIBLE);
        final AlphaAnimation fade = createFade(1, 0);
        fade.setAnimationListener(new HideViewOnAnimationListener(viewToAnimate));
        fade.setDuration(normalDuration);
        viewToAnimate.startAnimation(fade);
    }

    public void fadeOutFast(@NonNull final View viewToAnimate) {
        if (!shouldFadeOut(viewToAnimate)) {
            return;
        }
        viewToAnimate.setVisibility(View.VISIBLE);
        final AlphaAnimation fade = createFade(1, 0);
        fade.setAnimationListener(new HideViewOnAnimationListener(viewToAnimate));
        fade.setDuration(fastDuration);
        viewToAnimate.startAnimation(fade);
    }
}
