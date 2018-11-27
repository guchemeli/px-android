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

import java.util.List;

public class PaymentMethodHeaderView extends LinearLayout {

    /* default */ MPTextView titleView;
    /* default */ ImageView arrow;
    /* default */ Animation rotateUp;
    /* default */ Animation rotateDown;

    private TitlePager titlePager;
    private List<PaymentMethodDescriptorView.Model> installmentModels;
    private int currentIndex;

    public void setInstallmentsModel(final List<PaymentMethodDescriptorView.Model> installmentModels) {
        this.installmentModels = installmentModels;
    }

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

    public void update() {
        titleView.setVisibility(VISIBLE);
        titlePager.setVisibility(GONE);
    }

    public void update(final int paymentMethodIndex) {
        currentIndex = paymentMethodIndex;

        if (titleView.getVisibility() == VISIBLE) {
            arrow.startAnimation(rotateDown);
        }

        titlePager.setVisibility(VISIBLE);
        titleView.setVisibility(GONE);

        setClickable(installmentModels.get(currentIndex).hasPayerCostList());
    }

    public void updatePosition(final float positionOffset, final int position) {
        fadeBasedOnPosition(positionOffset, position);
    }

    private void fadeBasedOnPosition(final float positionOffset, final int position) {

        float relativeOffset = positionOffset;

        final GoingToModel goingTo = position == currentIndex ? GoingToModel.FORWARD : GoingToModel.BACKWARDS;

        final PaymentMethodDescriptorView.Model currentModel = installmentModels.get(currentIndex);
        PaymentMethodDescriptorView.Model goingToModel = null;
        if (GoingToModel.BACKWARDS == goingTo && position >= 0) {
            goingToModel = installmentModels.get(position);
            relativeOffset = 1.0f - positionOffset;
        } else if (GoingToModel.FORWARD == goingTo && position + 1 < installmentModels.size()) {
            goingToModel = installmentModels.get(position + 1);
        }

        if (currentModel.hasPayerCostList()) {
            if (goingToModel != null && !goingToModel.hasPayerCostList()) {
                arrow.setAlpha(1.0f - relativeOffset);
            } else {
                arrow.setAlpha(1.0f);
            }
        } else {
            if (goingToModel != null && goingToModel.hasPayerCostList()) {
                arrow.setAlpha(relativeOffset);
            } else {
                arrow.setAlpha(0.0f);
            }
        }
    }
}
