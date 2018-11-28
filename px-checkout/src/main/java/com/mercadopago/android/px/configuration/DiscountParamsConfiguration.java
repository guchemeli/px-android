package com.mercadopago.android.px.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class DiscountParamsConfiguration {

    @NonNull private final Set<String> labels;
    @Nullable private final String flow;

    /* default */ DiscountParamsConfiguration(@NonNull final Builder builder) {
        labels = builder.labels;
        flow = builder.flow;
    }

    /**
     * Let us know what the labels is
     *
     * @return set of labels
     */
    @NonNull
    public Set<String> getLabels() {
        return labels;
    }

    /**
     * Let us know what the flow is
     *
     * @return flow id
     */
    @Nullable
    public String getFlow() {
        return flow;
    }

    public static class Builder {
        /* default */ Set<String> labels;
        /* default */ String flow;

        public Builder() {
            labels = new HashSet<>();
            flow = "";
        }

        /**
         * This are filters for enable particular discounts.
         *
         * @param labels set of Mercado Pago filters
         * @return builder to keep operating
         */
        public Builder setLabels(@NonNull final Set<String> labels) {
            this.labels = labels;
            return this;
        }

        /**
         * Payment flow let us to enable discounts for the flow specified.
         *
         * @param flow payment flow id
         * @return builder to keep operating
         */
        public Builder setFlow(@NonNull final String flow) {
            this.flow = flow;
            return this;
        }

        public DiscountParamsConfiguration build() {
            return new DiscountParamsConfiguration(this);
        }
    }
}
