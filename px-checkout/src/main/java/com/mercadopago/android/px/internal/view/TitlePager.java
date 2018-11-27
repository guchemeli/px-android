package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.viewmodel.GoingToModel;
import com.mercadopago.android.px.internal.features.express.slider.TitlePagerAdapter;

public class TitlePager extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private View previousView;
    private View currentView;
    private View nextView;
    private int currentWidth;
    private TitlePagerAdapter adapter;

    public TitlePager(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitlePager(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.px_view_title_pager, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 3) {
            throw new RuntimeException("Incorrect number of children for Title Pager (must be 3)");
        }
        previousView = getChildAt(0);
        currentView = getChildAt(1);
        nextView = getChildAt(2);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (getWidth() > 0) {
            currentWidth = getWidth();
            resetViewsPosition();
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public void setAdapter(final TitlePagerAdapter adapter) {
        this.adapter = adapter;
    }

    public void updatePosition(final float offset, final GoingToModel goingTo) {
        float positionOffset = offset;

        if (positionOffset == 0.0f) {
            return;
        }

        if (goingTo == GoingToModel.BACKWARDS) {
            previousView.setAlpha(1.0f - positionOffset);
            currentView.setAlpha(positionOffset);
            positionOffset = 1.0f - positionOffset;
            positionOffset *= -1.0f;
        } else {
            //Added to fix lint error, not really needed
            positionOffset = Math.abs(positionOffset);
            nextView.setAlpha(positionOffset);
            currentView.setAlpha(1.0f - positionOffset);
        }

        final float offsetInPixels = currentWidth * positionOffset;

        previousView.setX(-currentWidth - offsetInPixels);
        currentView.setX(-offsetInPixels);
        nextView.setX(currentWidth - offsetInPixels);
    }

    public void orderViews(final GoingToModel goingTo) {
        final View auxView;
        if (goingTo == GoingToModel.BACKWARDS) {
            auxView = previousView;
            previousView = currentView;
            currentView = nextView;
            nextView = auxView;
        } else {
            auxView = nextView;
            nextView = currentView;
            currentView = previousView;
            previousView = auxView;
        }
        adapter.updateViewsOrder(previousView, currentView, nextView);
    }

    private void resetViewsPosition() {
        currentView.setX(0);
        previousView.setX(-currentWidth);
        nextView.setX(currentWidth);
    }
}
