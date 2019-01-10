package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.AmountConfiguration;
import javax.annotation.Nonnull;

public interface AmountConfigurationRepository {

    /**
     * Obtains the payer costs configuration that applies in a particular moment of the flow
     * <p>
     * In the future, with a discount selector feature, the selected discount will define the associated payer cost.
     *
     * @return The current dominant configuration.
     */
    @NonNull
    AmountConfiguration getCurrentConfiguration();

    /**
     * Obtains the complete payer cost configuration for a specif custom option.
     *
     * @param cardId The {@link com.mercadopago.android.px.model.CustomSearchItem} ID.
     * @return The payer cost configuration, returns null if don't have a configuration or ID is invalid.
     */
    @NonNull
    AmountConfiguration getConfigurationFor(@Nonnull final String cardId);
}
