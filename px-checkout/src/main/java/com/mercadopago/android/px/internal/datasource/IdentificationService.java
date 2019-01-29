package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.repository.IdentificationRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.IdentificationType;
import java.util.List;

public class IdentificationService implements IdentificationRepository {

    @NonNull private final com.mercadopago.android.px.internal.services.IdentificationService identificationService;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    public IdentificationService(
        @NonNull final com.mercadopago.android.px.internal.services.IdentificationService identificationService,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.identificationService = identificationService;
        this.paymentSettingRepository = paymentSettingRepository;
    }

    @Override
    public MPCall<List<IdentificationType>> getIdentificationTypes() {
        final String privateKey = paymentSettingRepository.getPrivateKey();
        if (TextUtil.isNotEmpty(privateKey)) {
            return getIdentificationTypes(privateKey);
        } else {
            return identificationService
                .getIdentificationTypesNonAuthUser(paymentSettingRepository.getPublicKey());
        }
    }

    @Override
    public MPCall<List<IdentificationType>> getIdentificationTypes(@NonNull final String accessToken) {
        return identificationService.getIdentificationTypesForAuthUser(accessToken);
    }
}
