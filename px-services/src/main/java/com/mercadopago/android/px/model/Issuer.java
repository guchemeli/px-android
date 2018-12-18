package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.List;

public class Issuer implements Serializable, Parcelable {

    private Long id;
    private String name;
    private List<String> labels;

    @Deprecated
    public Issuer(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    protected Issuer(final Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        labels = in.createStringArrayList();
    }

    public static final Creator<Issuer> CREATOR = new Creator<Issuer>() {
        @Override
        public Issuer createFromParcel(final Parcel in) {
            return new Issuer(in);
        }

        @Override
        public Issuer[] newArray(final int size) {
            return new Issuer[size];
        }
    };

    public List<String> getLabels() {
        return labels;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setLabels(final List<String> labels) {
        this.labels = labels;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
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
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeStringList(labels);
    }
}
