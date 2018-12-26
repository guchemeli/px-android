package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PayerCostModel;
import com.mercadopago.android.px.tracking.internal.model.ExpressInstallmentsData;
import java.util.Map;

public class InstallmentsEventTrack extends EventTracker {

    private static final String PATH = BASE_PATH + "/review/one_tap/installments";
    @NonNull private final ExpressMetadata expressMetadata;
    @NonNull private final PayerCostModel payerCostModel;

    public InstallmentsEventTrack(@NonNull final ExpressMetadata expressMetadata,
        @NonNull final PayerCostModel payerCostModel) {
        this.expressMetadata = expressMetadata;
        this.payerCostModel = payerCostModel;
    }

    @NonNull
    @Override
    public Map<String, Object> getEventData() {
        return ExpressInstallmentsData.createFrom(expressMetadata, payerCostModel).toMap();
    }

    @NonNull
    @Override
    public String getEventPath() {
        return PATH;
    }
}
