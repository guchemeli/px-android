package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.tracking.internal.model.DiscountInfo;
import java.util.Map;

public class AppliedDiscountViewTracker extends ViewTracker {
    private static final String PATH = BASE_VIEW_PATH + PAYMENTS_PATH + "/applied_discount";

    @NonNull private final DiscountConfigurationModel discountModel;

    public AppliedDiscountViewTracker(@NonNull final DiscountConfigurationModel discountModel) {
        this.discountModel = discountModel;
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        final Map<String, Object> data = super.getData();
        data.put("discount",
            DiscountInfo.with(discountModel.getDiscount(), discountModel.getCampaign(),
                discountModel.isAvailable()).toMap());
        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return PATH;
    }
}
