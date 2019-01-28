package com.mercadopago.android.px.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This model represents the Installments + discounts response, it contains all the possible combinations for guessing
 * card payments.
 */
@Keep
public class SummaryAmount implements Serializable {

    @NonNull private String defaultAmountConfiguration;
    @NonNull private Map<String, AmountConfiguration> amountConfigurations;
    @Nullable private Map<String, DiscountConfigurationModel> discountsConfigurations;

    @NonNull
    public String getDefaultAmountConfiguration() {
        return defaultAmountConfiguration;
    }

    @Nullable
    public AmountConfiguration getAmountConfiguration(@NonNull final String key) {
        return amountConfigurations.get(key);
    }

    @NonNull
    public Map<String, DiscountConfigurationModel> getDiscountsConfigurations() {
        return discountsConfigurations == null
            ? new HashMap<String, DiscountConfigurationModel>() : discountsConfigurations;
    }
}
