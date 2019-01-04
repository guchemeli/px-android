package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.SummaryAmount;
import javax.annotation.Nonnull;

public interface DiscountRepository extends ResourcesProvider {

    /**
     * Obtains the discount configuration that applies in a particular moment of the flow
     * <p>
     * E.g. If the user did not select any payment method, the general discount is retrieved otherwise you will retrieve
     * the best discount between the general discount or the selected payment method.
     * <p>
     * In the future, with a discount selector feature, the selected discount will be dominant over the best one.
     *
     * @return The current dominant configuration
     */
    @NonNull
    DiscountConfigurationModel getCurrentConfiguration();

    /**
     * Obtains the complete discount configuration for a specif custom option.
     *
     * @param id The {@link com.mercadopago.android.px.model.CustomSearchItem} ID.
     * @return The discount configuration, returns null if the ID is invalid.
     */
    DiscountConfigurationModel getConfigurationFor(@Nonnull final String id);

    /**
     * Adds to the repository the discount configurations to be consumed.
     *
     * @param summaryAmount new discount configurations for guessing.
     */
    void addConfigurations(@NonNull final SummaryAmount summaryAmount);
}
