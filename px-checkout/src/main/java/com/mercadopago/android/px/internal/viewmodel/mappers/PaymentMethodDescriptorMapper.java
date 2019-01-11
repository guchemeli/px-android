package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.AmountConfigurationRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.internal.viewmodel.AccountMoneyDescriptor;
import com.mercadopago.android.px.internal.viewmodel.EmptyInstallmentsDescriptor;
import com.mercadopago.android.px.internal.viewmodel.InstallmentsDescriptorWithPayerCost;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PaymentTypes;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDescriptorMapper
    extends Mapper<List<ExpressMetadata>, List<PaymentMethodDescriptorView.Model>> {

    @NonNull private final PaymentSettingRepository paymentConfiguration;
    @NonNull private final AmountConfigurationRepository amountConfigurationRepository;

    public PaymentMethodDescriptorMapper(@NonNull final PaymentSettingRepository paymentConfiguration,
        @NonNull final AmountConfigurationRepository amountConfigurationRepository) {
        this.paymentConfiguration = paymentConfiguration;
        this.amountConfigurationRepository = amountConfigurationRepository;
    }

    @Override
    public List<PaymentMethodDescriptorView.Model> map(@NonNull final List<ExpressMetadata> expressMetadataList) {
        final List<PaymentMethodDescriptorView.Model> models = new ArrayList<>();

        for (final ExpressMetadata expressMetadata : expressMetadataList) {
            models.add(createInstallmentsDescriptorModel(expressMetadata));
        }

        //Last card is Add new payment method card
        models.add(createAddNewPaymentModel());

        return models;
    }

    private PaymentMethodDescriptorView.Model createInstallmentsDescriptorModel(final ExpressMetadata expressMetadata) {
        final String paymentTypeId = expressMetadata.getPaymentTypeId();
        final CardMetadata cardMetadata = expressMetadata.getCard();

        if (PaymentTypes.isCreditCardPaymentType(paymentTypeId)) {
            //This model is useful for Credit Card only
            return InstallmentsDescriptorWithPayerCost
                .createFrom(paymentConfiguration.getCheckoutPreference().getSite().getCurrencyId(),
                    amountConfigurationRepository.getConfigurationFor(cardMetadata.getId()));
        } else if (PaymentTypes.isAccountMoney(expressMetadata.getPaymentMethodId())) {
            return AccountMoneyDescriptor.createFrom(expressMetadata.getAccountMoney());
        } else {
            //This model is useful in case of One payment method (debit) to represent an empty row
            return EmptyInstallmentsDescriptor.create();
        }
    }

    private PaymentMethodDescriptorView.Model createAddNewPaymentModel() {
        return EmptyInstallmentsDescriptor.create();
    }
}
