package com.mercadopago.android.px.internal.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.adapters.ReviewPaymentMethodsAdapter;
import com.mercadopago.android.px.internal.base.PXActivity;
import com.mercadopago.android.px.internal.features.providers.ReviewPaymentMethodsProviderImpl;
import com.mercadopago.android.px.internal.util.ErrorUtil;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import java.lang.reflect.Type;
import java.util.List;

public class ReviewPaymentMethodsActivity extends PXActivity implements ReviewPaymentMethodsView {

    private static final String EXTRA_PAYMENT_METHODS = "EXTRA_PAYMENT_METHODS";
    private ReviewPaymentMethodsPresenter presenter;
    private RecyclerView recyclerView;

    public static void start(final Activity activity, final List<PaymentMethod> paymentMethods,
        final int reqCode) {
        final Intent intent = new Intent(activity, ReviewPaymentMethodsActivity.class);
        intent.putExtra(EXTRA_PAYMENT_METHODS, JsonUtil.getInstance().toJson(paymentMethods));
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onCreate(final Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.px_activity_review_payment_methods);
        recyclerView = findViewById(R.id.mpsdkReviewPaymentMethodsView);
        final FrameLayout mTryOtherCardButton = findViewById(R.id.tryOtherCardButton);
        mTryOtherCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                finish();
                overridePendingTransition(R.anim.px_no_change_animation, R.anim.px_slide_down_activity);
            }
        });
        initPresenter();
    }

    protected void initPresenter() {
        List<PaymentMethod> supportedPaymentMethods = null;
        try {
            final Gson gson = new Gson();
            final Type listType = new TypeToken<List<PaymentMethod>>() {
            }.getType();
            supportedPaymentMethods = gson.fromJson(getIntent().getStringExtra(EXTRA_PAYMENT_METHODS), listType);
        } catch (final Exception ex) {
            showError(new MercadoPagoError(presenter.getResourcesProvider().getStandardErrorMessage(), false), "");
        }

        if (supportedPaymentMethods != null && !supportedPaymentMethods.isEmpty()) {
            presenter = new ReviewPaymentMethodsPresenter(supportedPaymentMethods);
            presenter.attachView(this);
            presenter.attachResourcesProvider(new ReviewPaymentMethodsProviderImpl(this));
            presenter.initialize();
        } else {
            //Invalid data.
            finish();
        }
    }

    @Override
    public void showError(final MercadoPagoError error, final String requestOrigin) {
        ErrorUtil.startErrorActivity(this, error);
    }

    @Override
    public void initializeSupportedPaymentMethods(
        @Size(min = 1) @NonNull final List<PaymentMethod> supportedPaymentMethods) {
        final ReviewPaymentMethodsAdapter mAdapter = new ReviewPaymentMethodsAdapter(supportedPaymentMethods);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
