package com.mercadopago.android.px.internal.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.internal.view.PaymentMethodComponent;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.PaymentData;
import java.util.ArrayList;
import java.util.List;

public class BusinessPaymentModel implements Parcelable {

    public final BusinessPayment payment;
    private final List<PaymentMethodComponent.PaymentMethodProps> paymentMethodProps;

    public BusinessPaymentModel(final BusinessPayment payment,
        final String currencyId,
        final Iterable<PaymentData> paymentDataList) {
        this.payment = payment;
        paymentMethodProps = new ArrayList<>();

        for (final PaymentData paymentData : paymentDataList) {
            paymentMethodProps.add(PaymentMethodComponent.PaymentMethodProps
                .with(paymentData, currencyId, payment.getStatementDescription()));
        }
    }

    public BusinessPayment getPayment() {
        return payment;
    }

    public List<PaymentMethodComponent.PaymentMethodProps> getPaymentMethodProps() {
        return paymentMethodProps;
    }

    /* default */ BusinessPaymentModel(final Parcel in) {
        payment = in.readParcelable(BusinessPayment.class.getClassLoader());
        paymentMethodProps = in.createTypedArrayList(PaymentMethodComponent.PaymentMethodProps.CREATOR);
    }

    public static final Creator<BusinessPaymentModel> CREATOR = new Creator<BusinessPaymentModel>() {
        @Override
        public BusinessPaymentModel createFromParcel(final Parcel in) {
            return new BusinessPaymentModel(in);
        }

        @Override
        public BusinessPaymentModel[] newArray(final int size) {
            return new BusinessPaymentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(payment, flags);
        dest.writeTypedList(paymentMethodProps);
    }
}
