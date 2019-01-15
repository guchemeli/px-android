package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.PaymentMethod;

public interface UserSelectionRepository {

    void select(@Nullable final PaymentMethod primary, @Nullable final PaymentMethod secondary);

    void select(@Nullable final Card card, @Nullable final PaymentMethod secondaryPaymentMethod);

    void select(@NonNull final PayerCost payerCost);

    void select(@NonNull final Issuer issuer);

    void select(String paymentType);

    @Nullable
    PaymentMethod getPaymentMethod();

    @Nullable
    PaymentMethod getSecondaryPaymentMethod();

    void removePaymentMethodSelection();

    boolean hasPayerCostSelected();

    boolean hasCardSelected();

    @Nullable
    PayerCost getPayerCost();

    @Nullable
    Issuer getIssuer();

    @Nullable
    Card getCard();

    void reset();

    @NonNull
    String getPaymentType();
}
