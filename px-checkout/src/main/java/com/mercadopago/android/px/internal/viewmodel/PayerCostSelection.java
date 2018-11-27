package com.mercadopago.android.px.internal.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;

public class PayerCostSelection implements Parcelable {

    private final int[] payerCostSelected;

    public PayerCostSelection(final int paymentMethodsSize) {
        payerCostSelected = new int[paymentMethodsSize];
        for (int i = 0; i < payerCostSelected.length; i++) {
            payerCostSelected[i] = PaymentMethodDescriptorView.Model.SELECTED_PAYER_COST_NONE;
        }
    }

    public int get(final int paymentMethodIndex) {
        return payerCostSelected[paymentMethodIndex];
    }

    public void save(final int paymentMethodIndex, final int payerCostIndex) {
        payerCostSelected[paymentMethodIndex] = payerCostIndex;
    }

    public static final Creator<PayerCostSelection> CREATOR = new Creator<PayerCostSelection>() {
        @Override
        public PayerCostSelection createFromParcel(final Parcel in) {
            return new PayerCostSelection(in);
        }

        @Override
        public PayerCostSelection[] newArray(final int size) {
            return new PayerCostSelection[size];
        }
    };

    protected PayerCostSelection(final Parcel in) {
        payerCostSelected = in.createIntArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeIntArray(payerCostSelected);
    }
}
