package com.mercadopago.android.px.internal.features.providers;

import com.mercadopago.android.px.internal.base.ResourcesProvider;

public interface PayerInformationProvider extends ResourcesProvider {

    String getInvalidIdentificationNumberErrorMessage();

    String getInvalidIdentificationNameErrorMessage();

    String getInvalidIdentificationLastNameErrorMessage();

    String getInvalidIdentificationBusinessNameErrorMessage();

    String getMissingPublicKeyErrorMessage();

    String getMissingIdentificationTypesErrorMessage();
}
