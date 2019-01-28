package com.mercadopago.android.px.internal.repository;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.Issuer;
import java.util.List;

public interface IssuersRepository {

    MPCall<List<Issuer>> getIssuers(String paymentMethodId, String bin);
}
