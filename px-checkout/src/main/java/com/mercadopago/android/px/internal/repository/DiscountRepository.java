package com.mercadopago.android.px.internal.repository;

import android.support.annotation.Nullable;
import com.mercadopago.android.px.configuration.DiscountParamsConfiguration;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.model.Campaign;
import com.mercadopago.android.px.model.Discount;
import java.util.Set;

public interface DiscountRepository extends ResourcesProvider {

    //TODO maybe this method will be necessary
    void configureMerchantDiscountManually(@Nullable final PaymentConfiguration paymentConfiguration);

    void configureExtraData(@Nullable final DiscountParamsConfiguration discountParamsConfiguration);

    @Nullable
    Discount getDiscount();

    @Nullable
    Campaign getCampaign();

    @Nullable
    Set<String> getLabels();

    @Nullable
    String getFlow();

    boolean isNotAvailableDiscount();

    boolean hasValidDiscount();

    void reset();
}
