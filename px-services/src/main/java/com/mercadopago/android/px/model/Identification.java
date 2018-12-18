package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class Identification implements Serializable, Parcelable {

    private String number;
    private String type;

    @Deprecated
    public Identification() {
    }

    protected Identification(final Parcel in) {
        number = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(number);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Identification> CREATOR = new Creator<Identification>() {
        @Override
        public Identification createFromParcel(final Parcel in) {
            return new Identification(in);
        }

        @Override
        public Identification[] newArray(final int size) {
            return new Identification[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    @Deprecated
    public void setNumber(final String number) {
        this.number = number;
    }

    @Deprecated
    public void setType(final String type) {
        this.type = type;
    }
}
