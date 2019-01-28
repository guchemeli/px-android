package com.mercadopago.android.px.model;

import android.support.annotation.Keep;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Amount configuration represents one hash_amount representation for cards. this DTO is strongly linked with a {@link
 * DiscountConfigurationModel}.
 */
@Keep
public class AmountConfiguration implements Serializable, Parcelable {

    public static final int NO_SELECTED = -1;

    /**
     * default selected payer cost configuration for single payment method selection
     */
    public int selectedPayerCostIndex;

    /**
     * Payer cost configuration for single payment method selection
     */
    @NonNull public List<PayerCost> payerCosts;

    /**
     * Split payment node it it applies.
     */
    @Nullable public Split split;

    protected AmountConfiguration(final Parcel in) {
        selectedPayerCostIndex = in.readInt();
        payerCosts = in.createTypedArrayList(PayerCost.CREATOR);
    }

    @NonNull
    public List<PayerCost> getPayerCosts() {
        return payerCosts == null ? new ArrayList<PayerCost>() : payerCosts;
    }

    public boolean allowSplit() {
        return split != null;
    }

    @NonNull
    public List<PayerCost> getAppliedPayerCost(final boolean userWantToSplit) {
        if (isSplitPossible(userWantToSplit)) {
            return split.getPayerCosts();
        } else {
            return getPayerCosts();
        }
    }

    @NonNull
    public PayerCost getCurrentPayerCost(final boolean userWantToSplit, final int userSelectedIndex) {
        if (isSplitPossible(userWantToSplit)) {
            return PayerCost.getPayerCost(split.getPayerCosts(), userSelectedIndex,
                split.selectedPayerCostIndex);
        } else {
            return PayerCost.getPayerCost(getPayerCosts(), userSelectedIndex,
                selectedPayerCostIndex);
        }
    }

    public boolean isSplitPossible(final boolean userWantToSplit) {
        return userWantToSplit && allowSplit();
    }

    public int getDefaultPayerCostIndex() {
        return selectedPayerCostIndex;
    }

    public PayerCost getPayerCost(final int userSelectedPayerCost) {
        if (userSelectedPayerCost == NO_SELECTED) {
            return payerCosts.get(selectedPayerCostIndex);
        } else {
            return payerCosts.get(userSelectedPayerCost);
        }
    }

    public static final Creator<AmountConfiguration> CREATOR = new Creator<AmountConfiguration>() {
        @Override
        public AmountConfiguration createFromParcel(final Parcel in) {
            return new AmountConfiguration(in);
        }

        @Override
        public AmountConfiguration[] newArray(final int size) {
            return new AmountConfiguration[size];
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
