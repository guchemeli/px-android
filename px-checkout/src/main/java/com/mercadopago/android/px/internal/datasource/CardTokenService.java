package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.repository.CardTokenRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.services.GatewayService;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.Device;
import com.mercadopago.android.px.model.Token;

public class CardTokenService implements CardTokenRepository {

    @NonNull private final GatewayService gatewayService;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;
    @NonNull private Device device;

    public CardTokenService(@NonNull final GatewayService gatewayService,
        @NonNull final PaymentSettingRepository paymentSettingRepository,
        @NonNull final Device device) {
        this.gatewayService = gatewayService;
        this.paymentSettingRepository = paymentSettingRepository;
        this.device = device;
    }

    @Override
    public MPCall<Token> createTokenAsync(final CardToken cardToken) {
        cardToken.setDevice(device);
        return gatewayService
            .createToken(paymentSettingRepository.getPublicKey(), paymentSettingRepository.getPrivateKey(), cardToken);
    }
}
