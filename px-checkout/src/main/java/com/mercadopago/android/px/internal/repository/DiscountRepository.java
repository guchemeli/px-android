package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.model.Campaign;
import com.mercadopago.android.px.model.Discount;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import java.math.BigDecimal;

public interface DiscountRepository extends ResourcesProvider {

    void configureMerchantDiscountManually(@Nullable final PaymentConfiguration paymentConfiguration);

    @NonNull
    MPCall<Boolean> configureDiscountAutomatically(final BigDecimal amountToPay);

    @Nullable
    Discount getDiscount();

    @Nullable
    Campaign getCampaign();

    @Nullable
    Campaign getCampaign(String discountId);

    boolean isNotAvailableDiscount();

    boolean hasValidDiscount();

    void reset();
}
