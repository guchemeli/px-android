package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.ParcelableUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayerCostConfiguration implements Serializable, Parcelable {

    private Map<String, List<PayerCost>> configuration;
    private Integer selectedPayerCostIndex;

    public PayerCostConfiguration(@NonNull final Integer selectedPayerCostIndex,
        @NonNull final Map<String, List<PayerCost>> configuration) {
        this.selectedPayerCostIndex = selectedPayerCostIndex;
        this.configuration = configuration;
    }

    public List<PayerCost> getPayerCosts(@NonNull final String key) {
        return configuration.get(key);
    }

    public Map<String, List<PayerCost>> getConfiguration() {
        return configuration;
    }

    public int getSelectedPayerCostIndex() {
        return selectedPayerCostIndex;
    }

    protected PayerCostConfiguration(final Parcel in) {
        configuration = new HashMap<>();
        selectedPayerCostIndex = ParcelableUtil.getOptionalInteger(in);
        in.readMap(configuration, PayerCostConfiguration.class.getClassLoader());
    }

    public static final Creator<PayerCostConfiguration> CREATOR = new Creator<PayerCostConfiguration>() {
        @Override
        public PayerCostConfiguration createFromParcel(final Parcel in) {
            return new PayerCostConfiguration(in);
        }

        @Override
        public PayerCostConfiguration[] newArray(final int size) {
            return new PayerCostConfiguration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ParcelableUtil.writeOptional(dest, selectedPayerCostIndex);
        dest.writeMap(configuration);
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof PayerCostConfiguration) {
            final Integer selectedPayerCostIndexToCompare = ((PayerCostConfiguration) o).getSelectedPayerCostIndex();
            final Map<String, List<PayerCost>> configurationToCompare = ((PayerCostConfiguration) o).getConfiguration();
            return configuration.equals(configurationToCompare) &&
                selectedPayerCostIndex.equals(selectedPayerCostIndexToCompare);
        }
        return false;
    }
}
