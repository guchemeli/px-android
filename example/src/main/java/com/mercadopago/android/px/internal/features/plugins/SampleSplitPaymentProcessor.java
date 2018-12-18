package com.mercadopago.android.px.internal.features.plugins;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.core.SplitPaymentProcessor;

public class SampleSplitPaymentProcessor extends SplitPaymentProcessor {

    private final PaymentProcessor samplePaymentProcessor = new SamplePaymentProcessor();

    @Nullable
    @Override
    public Fragment getFragment(@NonNull final SplitPaymentProcessor.SplitCheckoutData data,
        @NonNull final Context context) {
        // Wrapped call - just one payment method
        return samplePaymentProcessor
            .getFragment(new CheckoutData(data.paymentDataList.get(0), data.checkoutPreference), context);
    }

    @Override
    public void startPayment(@NonNull final SplitPaymentProcessor.SplitCheckoutData data,
        @NonNull final Context context, @NonNull final OnPaymentListener paymentListener) {
        // Wrapped call - just one payment method
        samplePaymentProcessor.startPayment(new CheckoutData(data.paymentDataList.get(0), data.checkoutPreference),
            context, paymentListener);
    }

    // region old implementation

    @Override
    public void startPayment(@NonNull final CheckoutData data, @NonNull final Context context,
        @NonNull final OnPaymentListener paymentListener) {
        samplePaymentProcessor.startPayment(data, context, paymentListener);
    }

    @Override
    public int getPaymentTimeout() {
        return samplePaymentProcessor.getPaymentTimeout();
    }

    @Override
    public boolean shouldShowFragmentOnPayment() {
        return samplePaymentProcessor.shouldShowFragmentOnPayment();
    }

    @Nullable
    @Override
    public Bundle getFragmentBundle(@NonNull final CheckoutData data, @NonNull final Context context) {
        return samplePaymentProcessor.getFragmentBundle(data, context);
    }

    @Nullable
    @Override
    public Fragment getFragment(@NonNull final CheckoutData data, @NonNull final Context context) {
        return samplePaymentProcessor.getFragment(data, context);
    }

    // endregion old implementation

    // region parcelable

    public static final Creator<SampleSplitPaymentProcessor> CREATOR = new Creator<SampleSplitPaymentProcessor>() {
        @Override
        public SampleSplitPaymentProcessor createFromParcel(final Parcel in) {
            return new SampleSplitPaymentProcessor(in);
        }

        @Override
        public SampleSplitPaymentProcessor[] newArray(final int size) {
            return new SampleSplitPaymentProcessor[size];
        }
    };

    /* default */ SampleSplitPaymentProcessor(final Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
    }

    // endregion
}
