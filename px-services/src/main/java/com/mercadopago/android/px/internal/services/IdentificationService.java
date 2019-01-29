package com.mercadopago.android.px.internal.services;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.IdentificationType;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IdentificationService {

    /**
     * Get identification types according to site. Non authenticated user.
     * @return MPCall with list of IdentificationTypes in case of Success.
     */
    @GET("/v1/identification_types")
    MPCall<List<IdentificationType>> getIdentificationTypesNonAuthUser(@NonNull @Query("public_key") String publicKey);

    /**
     * Get identification types according to site. Authenticated user.
     * @return MPCall with list of IdentificationTypes in case of Success.
     */
    @GET("/v1/identification_types")
    MPCall<List<IdentificationType>> getIdentificationTypesForAuthUser(@NonNull@Query("access_token") String privateKey);
}
