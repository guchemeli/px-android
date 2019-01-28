package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.constants.ProcessingModes;
import com.mercadopago.android.px.internal.repository.IssuersRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.model.Issuer;
import java.util.List;

import static com.mercadopago.android.px.services.BuildConfig.API_ENVIRONMENT;

public class IssuersService implements IssuersRepository {

    @NonNull private final com.mercadopago.android.px.internal.services.IssuersService issuersService;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    public IssuersService(
        @NonNull final com.mercadopago.android.px.internal.services.IssuersService issuersService,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.issuersService = issuersService;

        this.paymentSettingRepository = paymentSettingRepository;
    }

    @Override
    public MPCall<List<Issuer>> getIssuers(final String paymentMethodId, final String bin) {
        return issuersService.getIssuers(API_ENVIRONMENT, paymentSettingRepository.getPublicKey(),
            paymentSettingRepository.getPrivateKey(), paymentMethodId, bin, ProcessingModes.AGGREGATOR);
    }
}
