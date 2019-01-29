package com.mercadopago.android.px.internal.repository;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.Token;

public interface CardTokenRepository {

    /**
     * After gathering user card's information, create a cardToken to create Payment.
     *
     * @param cardToken: Card information to create cardToken.
     * @return Token associated to Card.
     */
    MPCall<Token> createTokenAsync(final CardToken cardToken);
}
