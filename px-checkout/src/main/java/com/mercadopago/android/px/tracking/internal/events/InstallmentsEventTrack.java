package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.tracking.internal.model.ExpressInstallmentsData;
import java.util.Map;

public class InstallmentsEventTrack extends EventTracker {

    private static final String PATH = BASE_PATH + "/review/one_tap/installments";
    @NonNull private final ExpressMetadata expressMetadata;
    @NonNull private final AmountConfiguration amountConfiguration;

    public InstallmentsEventTrack(@NonNull final ExpressMetadata expressMetadata,
        @NonNull final AmountConfiguration amountConfiguration) {
        this.expressMetadata = expressMetadata;
        this.amountConfiguration = amountConfiguration;
    }

    @NonNull
    @Override
    public Map<String, Object> getEventData() {
        return ExpressInstallmentsData.createFrom(expressMetadata, amountConfiguration).toMap();
    }

    @NonNull
    @Override
    public String getEventPath() {
        return PATH;
    }
}
