package com.mercadopago.android.px.internal.features.plugins;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.core.SplitPaymentProcessor;

public class SampleSplitPaymentProcessor implements SplitPaymentProcessor {

    private final PaymentProcessor samplePaymentProcessor = new SamplePaymentProcessor();

    // region old implementation

    @Override
    public void startPayment(@NonNull final CheckoutData data, @NonNull final Context context,
        @NonNull final OnPaymentListener paymentListener) {
        //TODO checkout data has more than one payment method;
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
        //TODO checkout data has more than one payment method;
        return samplePaymentProcessor.getFragmentBundle(data, context);
    }

    @Nullable
    @Override
    public Fragment getFragment(@NonNull final CheckoutData data, @NonNull final Context context) {

        //TODO checkout data has more than one payment method;
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
        //Do nothing
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        //Do nothing
    }

    // endregion
}
