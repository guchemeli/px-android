package com.mercadopago.android.px.internal.services;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.Discount;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DiscountService {

    @GET("/discount_campaigns")
    MPCall<Discount> getDiscount(@Query("public_key") String publicKey,
        @Query("transaction_amount") String transactionAmount, @Query("email") String payerEmail);

    @GET("/discount_campaigns")
    MPCall<Discount> getDiscount(@Query("public_key") String publicKey,
        @Query("transaction_amount") String transactionAmount, @Query("email") String payerEmail,
        @Query("coupon_code") String couponCode);
}
