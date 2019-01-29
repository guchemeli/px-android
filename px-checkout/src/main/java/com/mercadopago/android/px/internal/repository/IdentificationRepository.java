package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.IdentificationType;
import java.util.List;

public interface IdentificationRepository {

    /**
     * Get identification types according to site. Non authenticated user.
     * @return List of IdentificationTypes.
     */
    MPCall<List<IdentificationType>> getIdentificationTypes();

    /**
     * Get identification types according to site. Authenticated user.
     * @return List of IdentificationTypes.
     */
    MPCall<List<IdentificationType>> getIdentificationTypes(@NonNull final String accessToken);
}
