package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.viewmodel.GoingToModel;

public class PaymentMethodHeaderView extends LinearLayout {

    /* default */ MPTextView titleView;
    /* default */ ImageView arrow;
    /* default */ Animation rotateUp;
    /* default */ Animation rotateDown;

    private TitlePager titlePager;

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
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.px_view_installments_header, this);

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

    public void showTitlePager(final boolean clickable) {
        if (titleView.getVisibility() == VISIBLE) {
            arrow.startAnimation(rotateDown);
        }

        titlePager.setVisibility(VISIBLE);
        titleView.setVisibility(GONE);

        setClickable(clickable);
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
