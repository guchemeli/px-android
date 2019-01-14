package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.viewmodel.GoingToModel;

public class PaymentMethodHeaderView extends FrameLayout {

    /* default */ final View titleView;

    /* default */ final ImageView arrow;

    /* default */ final Animation rotateUp;

    /* default */ final Animation rotateDown;

    private final TitlePager titlePager;

    public interface Listener {

        void onDescriptorViewClicked();

        void onInstallmentsSelectorCancelClicked();
    }

    public PaymentMethodHeaderView(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentMethodHeaderView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.px_view_installments_header, this, true);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.px_rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.px_rotate_down);
        titleView = findViewById(R.id.installments_title);
        titlePager = findViewById(R.id.title_pager);
        arrow = findViewById(R.id.arrow);
    }

    public void setListener(final Listener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (titleView.getVisibility() == VISIBLE) {
                    arrow.startAnimation(rotateDown);
                    listener.onInstallmentsSelectorCancelClicked();
                } else {
                    arrow.startAnimation(rotateUp);
                    listener.onDescriptorViewClicked();
                }
            }
        });
    }

    public void showInstallmentsListTitle() {
        titleView.setVisibility(VISIBLE);
        titlePager.setVisibility(GONE);
    }

    public void showTitlePager(final boolean isClickable) {
        if (titleView.getVisibility() == VISIBLE) {
            arrow.startAnimation(rotateDown);
        }

        titlePager.setVisibility(VISIBLE);
        titleView.setVisibility(GONE);

        setClickable(isClickable);
    }

    public void updateArrowVisibility(float positionOffset, final Model model) {
        if (model.goingTo == GoingToModel.BACKWARDS) {
            positionOffset = 1.0f - positionOffset;
        }

        if (model.currentIsExpandable) {
            if (model.nextIsExpandable) {
                arrow.setAlpha(1.0f);
            } else {
                arrow.setAlpha(1.0f - positionOffset);
            }
        } else {
            if (model.nextIsExpandable) {
                arrow.setAlpha(positionOffset);
            } else {
                arrow.setAlpha(0.0f);
            }
        }
    }

    public static class Model {
        final GoingToModel goingTo;
        final boolean currentIsExpandable;
        final boolean nextIsExpandable;

        public Model(final GoingToModel goingTo, final boolean currentIsExpandable,
            final boolean nextIsExpandable) {
            this.goingTo = goingTo;
            this.currentIsExpandable = currentIsExpandable;
            this.nextIsExpandable = nextIsExpandable;
        }
    }
}
