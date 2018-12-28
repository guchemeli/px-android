package com.mercadopago.android.px.internal.util;

import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class JsonUtil {

    private static JsonUtil mInstance = null;
    private final Gson mGson;

    private JsonUtil() {
        mGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();
    }

    public static JsonUtil getInstance() {
        if (mInstance == null) {
            mInstance = new JsonUtil();
        }
        return mInstance;
    }

    public <T> T fromJson(@NonNull final String json, @NonNull final Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    public <T> T fromJson(@NonNull final String json, @NonNull final Type classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    private Map<String, Object> getMapFromJson(@NonNull final String json) {
        return new Gson().fromJson(
            json, new TypeToken<HashMap<String, Object>>() {
            }.getType()
        );
    }

    public Map<String, Object> getMapFromObject(final Object src) {
        return getMapFromJson(mGson.toJson(src));
    }

    public String toJson(final Object src) {
        return mGson.toJson(src);
    }

    public Gson getGson() {
        return mGson;
    }
}
