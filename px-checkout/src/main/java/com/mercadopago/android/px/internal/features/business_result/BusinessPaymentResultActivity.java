package com.mercadopago.android.px.internal.features.business_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.ActionDispatcher;
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel;
import com.mercadopago.android.px.model.Action;
import com.mercadopago.android.px.model.ExitAction;
import com.mercadopago.android.px.model.PaymentData;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.tracking.internal.views.ResultViewTrack;

import static com.mercadopago.android.px.internal.features.Constants.RESULT_CUSTOM_EXIT;

public class BusinessPaymentResultActivity extends AppCompatActivity implements ActionDispatcher {

    private static final String EXTRA_BUSINESS_PAYMENT_MODEL = "extra_business_payment_model";

    public static Intent getIntent(@NonNull final Context context,
        @NonNull final BusinessPaymentModel model) {
        final Intent intent = new Intent(context, BusinessPaymentResultActivity.class);
        intent.putExtra(EXTRA_BUSINESS_PAYMENT_MODEL, model);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BusinessPaymentModel model = parseBusinessPaymentModel();

        if (model != null) {
            final LinearLayout linearContainer = ViewUtils.createLinearContainer(this);
            linearContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
            setContentView(linearContainer);

            final BusinessPaymentContainer businessPaymentContainer = new BusinessPaymentContainer(model, this);
            final View render = businessPaymentContainer.render(linearContainer);
            linearContainer.addView(render);
        } else {
            throw new IllegalStateException("BusinessPayment can't be loaded");
        }
        trackScreen(model);
    }

    private void trackScreen(final BusinessPaymentModel model) {
        //TODO refactor - added because tracking needed.
        final PaymentMethod paymentMethod =
            Session.getSession(this).getConfigurationModule()
                .getUserSelectionRepository()
                .getPaymentMethod();
        final PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(paymentMethod);

        new ResultViewTrack(ResultViewTrack.Style.CUSTOM, new PaymentResult.Builder()
            .setPaymentData(paymentData)
            .setPaymentStatus(model.payment.getPaymentStatus())
            .setPaymentStatusDetail(model.payment.getPaymentStatusDetail())
            .setPaymentId(model.payment.getId())
            .build()).track();
    }

    @Nullable
    private BusinessPaymentModel parseBusinessPaymentModel() {
        return getIntent().getExtras() != null ? (BusinessPaymentModel) getIntent()
            .getExtras()
            .getParcelable(EXTRA_BUSINESS_PAYMENT_MODEL) : null;
    }

    @Override
    public void dispatch(final Action action) {
        if (action instanceof ExitAction) {
            processCustomExit((ExitAction) action);
        } else {
            throw new UnsupportedOperationException("this Action class can't be executed in this screen");
        }
    }

    @Override
    public void onBackPressed() {
        processCustomExit(new ExitAction("exit", RESULT_OK));
    }

    private void processCustomExit(final ExitAction action) {
        final Intent intent = action.toIntent();
        setResult(RESULT_CUSTOM_EXIT, intent);
        finish();
    }
}
