package com.mercadopago.android.px.internal.repository;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.BankDeal;
import java.util.List;

public interface BankDealsRepository {
    /**
     * Get Bank's special offers.
     *
     * @return List of BankDeals
     */
    MPCall<List<BankDeal>> getBankDealsAsync();
}
