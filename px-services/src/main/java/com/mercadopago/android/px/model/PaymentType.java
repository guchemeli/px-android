package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class PaymentType implements Serializable, Parcelable {

    private String id;

    protected PaymentType(final Parcel in) {
        id = in.readString();
    }

    @Deprecated
    public PaymentType(final String paymentTypeId) {
        id = paymentTypeId;
    }

    @Deprecated
    public void setId(final String id) {
        this.id = id;
    }

    public static final Creator<PaymentType> CREATOR = new Creator<PaymentType>() {
        @Override
        public PaymentType createFromParcel(final Parcel in) {
            return new PaymentType(in);
        }

        @Override
        public PaymentType[] newArray(final int size) {
            return new PaymentType[size];
        }
    };

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
    }
}
