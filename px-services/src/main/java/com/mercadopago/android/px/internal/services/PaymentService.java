package com.mercadopago.android.px.internal.services;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethod;
import java.util.List;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PaymentService {

    @GET("/{version}/px_mobile_api/payment_methods/cards")
    MPCall<List<PaymentMethod>> getCardPaymentMethods(@Path(value = "version", encoded = true) String version,
        @Query("access_token") String accessToken);

    @GET("/{version}/checkout/payment_methods/card_issuers")
    MPCall<List<Issuer>> getIssuers(@Path(value = "version", encoded = true) String version,
        @Query("public_key") String publicKey, @Query("access_token") String privateKey,
        @Query("payment_method_id") String paymentMethodId, @Query("bin") String bin,
        @Query("processing_mode") String processingMode);

    @POST("/v1/checkout/payments")
    MPCall<Payment> createPayment(@Header("X-Idempotency-Key") String transactionId,
        @Body Map<String, Object> additionalInfo,
        @QueryMap Map<String, String> query);
}