package com.mercadopago.android.px.model.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.I2Payment;
import com.mercadopago.android.px.model.I2PaymentHandler;

/**
 *
 */
public class I2ParcelablePayment implements I2Payment, Parcelable {

    private final String paymentTypeId;
    private final String paymentMethodId;
    private final Long id;
    private final String statementDescription;
    private final String paymentStatus;
    private final String paymentStatusDetail;

    public I2ParcelablePayment(@NonNull final I2Payment i2Payment) {
        paymentTypeId = i2Payment.getPaymentTypeId();
        paymentMethodId = i2Payment.getPaymentMethodId();
        id = i2Payment.getId();
        statementDescription = i2Payment.getStatementDescription();
        paymentStatus = i2Payment.getPaymentStatus();
        paymentStatusDetail = i2Payment.getPaymentStatusDetail();
    }

    private I2ParcelablePayment(final Parcel in) {
        paymentTypeId = in.readString();
        paymentMethodId = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        statementDescription = in.readString();
        paymentStatus = in.readString();
        paymentStatusDetail = in.readString();
    }

    public static final Creator<I2ParcelablePayment> CREATOR = new Creator<I2ParcelablePayment>() {
        @Override
        public I2ParcelablePayment createFromParcel(final Parcel in) {
            return new I2ParcelablePayment(in);
        }

        @Override
        public I2ParcelablePayment[] newArray(final int size) {
            return new I2ParcelablePayment[size];
        }
    };

    @NonNull
    @Override
    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    @NonNull
    @Override
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    @Override
    public void process(@NonNull final I2PaymentHandler handler) {
        handler.process(this);
    }

    @Nullable
    @Override
    public Long getId() {
        return id;
    }

    @Nullable
    @Override
    public String getStatementDescription() {
        return statementDescription;
    }

    @NonNull
    @Override
    public String getPaymentStatus() {
        return paymentStatus;
    }

    @NonNull
    @Override
    public String getPaymentStatusDetail() {
        return paymentStatusDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(paymentTypeId);
        dest.writeString(paymentMethodId);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(statementDescription);
        dest.writeString(paymentStatus);
        dest.writeString(paymentStatusDetail);
    }
}
