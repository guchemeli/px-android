package com.mercadopago.android.px.internal.features.providers;

import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.model.SavedESCCardToken;
import com.mercadopago.android.px.model.Token;

public interface CardVaultProvider extends ResourcesProvider {

    void createESCTokenAsync(final SavedESCCardToken escCardToken, final TaggedCallback<Token> taggedCallback);
}
