package com.mercadopago.android.px.core.internal;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.core.SplitPaymentProcessor;
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper;
import com.mercadopago.android.px.preferences.CheckoutPreference;

public class PaymentProcessorMapper extends Mapper<PaymentProcessor, SplitPaymentProcessor> {

    @NonNull /* default */ final PaymentListenerMapper paymentListenerMapper;
    @NonNull /* default */ final CheckoutDataMapper checkoutDataMapper;

    public PaymentProcessorMapper(@NonNull final PaymentListenerMapper paymentListenerMapper,
        @NonNull final CheckoutDataMapper checkoutDataMapper) {
        this.paymentListenerMapper = paymentListenerMapper;
        this.checkoutDataMapper = checkoutDataMapper;
    }

    @Override
    public SplitPaymentProcessor map(@NonNull final PaymentProcessor val) {
        return new SplitPaymentProcessor() {
            @Override
            public void startPayment(@NonNull final Context context, @NonNull final CheckoutData data,
                @NonNull final OnPaymentListener paymentListener) {
                val.startPayment(checkoutDataMapper.map(data), context, paymentListenerMapper.map(paymentListener));
            }

            @Override
            public int getPaymentTimeout(@NonNull final CheckoutPreference checkoutPreference) {
                return val.getPaymentTimeout();
            }

            @Override
            public boolean shouldShowFragmentOnPayment(@NonNull final CheckoutPreference checkoutPreference) {
                return val.shouldShowFragmentOnPayment();
            }

            @Override
            public boolean supportsSplitPayment(@NonNull final CheckoutPreference checkoutPreference) {
                return false;
            }

            @Nullable
            @Override
            public Fragment getFragment(@NonNull final CheckoutData data, @NonNull final Context context) {
                final PaymentProcessor.CheckoutData mapped = checkoutDataMapper.map(data);
                final Fragment fragment = val.getFragment(checkoutDataMapper.map(data), context);
                if (fragment != null) {
                    fragment.setArguments(val.getFragmentBundle(mapped, context));
                }
                return fragment;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(final Parcel dest, final int flags) {
            }
        };
    }
}
