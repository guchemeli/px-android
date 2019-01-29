package com.mercadopago.android.px.internal.datasource;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.repository.BankDealsRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.services.BankDealService;
import com.mercadopago.android.px.internal.util.LocaleUtil;
import com.mercadopago.android.px.model.BankDeal;
import java.util.List;

public class BankDealsService implements BankDealsRepository {

    @NonNull private final BankDealService bankDealService;
    @NonNull private final Context context;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    public BankDealsService(@NonNull final BankDealService bankDealService, @NonNull final Context context,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.bankDealService = bankDealService;
        this.context = context;
        this.paymentSettingRepository = paymentSettingRepository;
    }

    @Override
    public MPCall<List<BankDeal>> getBankDealsAsync() {
        return bankDealService
            .getBankDeals(paymentSettingRepository.getPublicKey(), paymentSettingRepository.getPrivateKey(), LocaleUtil
                .getLanguage(context));
    }
}
