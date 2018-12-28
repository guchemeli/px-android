package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.tracking.internal.mapper.FromSelectedExpressMetadataToAvailableMethods;
import com.mercadopago.android.px.tracking.internal.mapper.FromUserSelectionToAvailableMethod;
import com.mercadopago.android.px.tracking.internal.model.AvailableMethod;
import com.mercadopago.android.px.tracking.internal.model.ConfirmData;
import java.util.Map;
import java.util.Set;

public final class ConfirmEvent extends EventTracker {

    private static final String EVENT_PATH_REVIEW_CONFIRM = BASE_PATH + "/review/confirm";
    private final ConfirmData data;

    public enum ReviewType {
        ONE_TAP("one_tap"),
        TRADITIONAL("traditional");

        /* default */ public final String value;

        /* default */ ReviewType(final String value) {
            this.value = value;
        }
    }

    @NonNull
    public static ConfirmEvent from(@NonNull final Set<String> cardsWithEsc,
        @NonNull final ExpressMetadata expressMetadata, @Nullable final PayerCost selectedPayerCost) {
        return new ConfirmEvent(new ConfirmData(ReviewType.ONE_TAP,
            new FromSelectedExpressMetadataToAvailableMethods(cardsWithEsc, selectedPayerCost).map(expressMetadata)));
    }

    @NonNull
    public static ConfirmEvent from(@NonNull final Set<String> cardsWithEsc,
        @NonNull final UserSelectionRepository userSelectionRepository) {
        final AvailableMethod ava = new FromUserSelectionToAvailableMethod(cardsWithEsc).map(userSelectionRepository);
        return new ConfirmEvent(new ConfirmData(ReviewType.TRADITIONAL, ava));
    }

    private ConfirmEvent(@NonNull final ConfirmData data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Map<String, Object> getEventData() {
        return data.toMap();
    }

    @NonNull
    @Override
    public String getEventPath() {
        return EVENT_PATH_REVIEW_CONFIRM;
    }
}
