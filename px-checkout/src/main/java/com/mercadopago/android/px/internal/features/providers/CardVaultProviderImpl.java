package com.mercadopago.android.px.internal.features.providers;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.internal.datasource.MercadoPagoServicesAdapter;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.model.SavedESCCardToken;
import com.mercadopago.android.px.model.Token;

public class CardVaultProviderImpl implements CardVaultProvider {

    private final MercadoPagoServicesAdapter mercadoPago;

    public CardVaultProviderImpl(@NonNull final Context context) {
        final Session session = Session.getSession(context);
        final PaymentSettingRepository paymentSettings = session.getConfigurationModule().getPaymentSettings();
        mercadoPago =
            new MercadoPagoServicesAdapter(context, paymentSettings.getPublicKey(), paymentSettings.getPrivateKey());
    }

    @Override
    public void createESCTokenAsync(final SavedESCCardToken escCardToken,
        final TaggedCallback<Token> taggedCallback) {
        mercadoPago.createToken(escCardToken, taggedCallback);
    }
}
