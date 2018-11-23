package com.mercadopago.android.px.internal.repository;

import android.support.annotation.Nullable;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.model.Campaign;
import com.mercadopago.android.px.model.Discount;

public interface DiscountRepository extends ResourcesProvider {

    void configureMerchantDiscountManually(@Nullable final PaymentConfiguration paymentConfiguration);

    @Nullable
    Discount getDiscount();

    @Nullable
    Campaign getCampaign();

    boolean isNotAvailableDiscount();

    boolean hasValidDiscount();

    void reset();
}
