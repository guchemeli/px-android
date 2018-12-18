package com.mercadopago.android.px.core;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.PaymentData;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.util.List;

@SuppressWarnings("unused")
public abstract class SplitPaymentProcessor implements PaymentProcessor, Parcelable {

    public static final class SplitCheckoutData {

        @NonNull public final CheckoutPreference checkoutPreference;
        @NonNull @Size(min = 1) public final List<PaymentData> paymentDataList;

        public SplitCheckoutData(@NonNull final CheckoutPreference checkoutPreference,
            @NonNull @Size(min = 1) final List<PaymentData> paymentDataList) {
            this.checkoutPreference = checkoutPreference;
            this.paymentDataList = paymentDataList;
        }
    }

    protected SplitPaymentProcessor(final Parcel in) {
    }

    /**
     * Fragment that will appear if {@link #shouldShowFragmentOnPayment()} is true when user clicks this payment
     * method.
     * <p>
     * inside {@link android.support.v4.app.Fragment#onAttach(Context)} context will be an instance of {@link
     * OnPaymentListener}
     *
     * @param data checkout data to the moment it's called.
     * @param context that you may need to fill information.
     * @return fragment
     */
    @Nullable
    public abstract Fragment getFragment(@NonNull final SplitCheckoutData data, @NonNull final Context context);

    /**
     * Method that we will call if {@link #shouldShowFragmentOnPayment()} is false. we will place a loading for you
     * meanwhile we call this method.
     *
     * @param data checkout data to the moment it's called.
     * @param context that you may need to fill information.
     * @param paymentListener when you have processed your payment you should call {@link OnPaymentListener}
     */
    public abstract void startPayment(@NonNull final SplitCheckoutData data, @NonNull final Context context,
        @NonNull final OnPaymentListener paymentListener);
}
