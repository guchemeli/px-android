package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class Cardholder implements Serializable, Parcelable {

    private Identification identification;
    private String name;

    @Deprecated
    public Cardholder() {
    }

    protected Cardholder(final Parcel in) {
        identification = in.readParcelable(Identification.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<Cardholder> CREATOR = new Creator<Cardholder>() {
        @Override
        public Cardholder createFromParcel(final Parcel in) {
            return new Cardholder(in);
        }

        @Override
        public Cardholder[] newArray(final int size) {
            return new Cardholder[size];
        }
    };

    public Identification getIdentification() {
        return identification;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setIdentification(final Identification identification) {
        this.identification = identification;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(identification, flags);
        dest.writeString(name);
    }
}
