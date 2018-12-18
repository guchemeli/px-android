package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.internal.mapper.FromExpressMetadataToAvailableMethods;
import com.mercadopago.android.px.tracking.internal.mapper.FromItemToItemInfo;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
@Keep
public class OneTapData extends SelectMethodData {

    @Nullable private final DiscountInfo discount;

    public OneTapData(@NonNull final List<AvailableMethod> availableMethods,
        @NonNull final BigDecimal totalAmount,
        @Nullable final DiscountInfo discount,
        @NonNull final List<ItemInfo> items) {
        super(availableMethods, items, totalAmount);
        this.discount = discount;
    }

    @NonNull
    public static OneTapData createFrom(final Iterable<ExpressMetadata> expressMetadataList,
        final CheckoutPreference checkoutPreference,
        final DiscountConfigurationModel discountModel) {

        final List<ItemInfo> itemInfoList = new FromItemToItemInfo().map(checkoutPreference.getItems());

        final DiscountInfo discountInfo =
            DiscountInfo.with(discountModel.getDiscount(), discountModel.getCampaign(), discountModel.isAvailable());

        return new OneTapData(
            new FromExpressMetadataToAvailableMethods(Collections.<String>emptySet()).map(expressMetadataList),
            checkoutPreference.getTotalAmount(), discountInfo, itemInfoList);
    }
}
