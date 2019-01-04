package com.mercadopago.android.px.tracking.internal.services;

import android.support.annotation.NonNull;
import android.util.Log;
import com.mercadopago.android.px.internal.util.HttpClientUtil;
import com.mercadopago.android.px.model.PaymentIntent;
import com.mercadopago.android.px.model.TrackingIntent;
import com.mercadopago.android.px.tracking.internal.utils.JsonConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mercadopago.android.px.services.BuildConfig.API_ENVIRONMENT;

public class MPTrackingServiceImpl implements MPTrackingService {

    private static final String BASE_URL = "https://api.mercadopago.com/";

    private static final int CONNECT_TIMEOUT = 20;
    private static final int READ_TIMEOUT = 20;
    private static final int WRITE_TIMEOUT = 20;

    private final TrackingAPI trackingAPI;

    public MPTrackingServiceImpl() {
        trackingAPI = createClient();
    }

    private TrackingAPI createClient() {
        return new Retrofit.Builder().client(HttpClientUtil.createClient(CONNECT_TIMEOUT, READ_TIMEOUT, WRITE_TIMEOUT))
            .addConverterFactory(GsonConverterFactory.create(JsonConverter.getInstance().getGson()))
            .baseUrl(BASE_URL)
            .build()
            .create(TrackingAPI.class);
    }

    @Override
    public void trackToken(final TrackingIntent trackingIntent) {

        final Call<Void> call = trackingAPI.trackToken(API_ENVIRONMENT, trackingIntent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull final Call<Void> call, @NonNull final Response<Void> response) {
                if (response.code() == 400) {
                    Log.e("Failure", "Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(@NonNull final Call<Void> call, @NonNull final Throwable t) {
                Log.e("Failure", "Service failure");
            }
        });
    }

    @Override
    public void trackPaymentId(final PaymentIntent paymentIntent) {

        final Call<Void> call = trackingAPI.trackPaymentId(API_ENVIRONMENT, paymentIntent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull final Call<Void> call, @NonNull final Response<Void> response) {
                if (response.code() == 400) {
                    Log.e("Failure", "Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(@NonNull final Call<Void> call, @NonNull final Throwable t) {
                Log.e("Failure", "Service failure");
            }
        });
    }
}
