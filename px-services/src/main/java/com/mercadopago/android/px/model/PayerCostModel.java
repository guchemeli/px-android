package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.List;

public class PayerCostModel implements Serializable, Parcelable {

    public final int selectedPayerCostIndex;
    public final List<PayerCost> payerCosts;

    protected PayerCostModel(final Parcel in) {
        selectedPayerCostIndex = in.readInt();
        payerCosts = in.createTypedArrayList(PayerCost.CREATOR);
    }

    public static final Creator<PayerCostModel> CREATOR = new Creator<PayerCostModel>() {
        @Override
        public PayerCostModel createFromParcel(final Parcel in) {
            return new PayerCostModel(in);
        }

        @Override
        public PayerCostModel[] newArray(final int size) {
            return new PayerCostModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(selectedPayerCostIndex);
        dest.writeTypedList(payerCosts);
    }
}
