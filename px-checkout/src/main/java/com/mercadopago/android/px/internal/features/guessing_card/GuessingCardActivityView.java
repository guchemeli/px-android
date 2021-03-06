package com.mercadopago.android.px.internal.features.guessing_card;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.internal.controllers.PaymentMethodGuessingController;
import com.mercadopago.android.px.model.CardInfo;
import com.mercadopago.android.px.model.IdentificationType;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentType;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.CardTokenException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import java.util.List;

public interface GuessingCardActivityView extends MvpView {

    void setupPresenter();

    void onValidStart();

    void showError(MercadoPagoError error, String requestOrigin);

    void setCardNumberListeners(PaymentMethodGuessingController controller);

    void showInputContainer();

    void showApiExceptionError(ApiException exception, String requestOrigin);

    void setCardNumberInputMaxLength(int length);

    void setSecurityCodeInputMaxLength(int length);

    void setSecurityCodeViewLocation(String location);

    void initializeIdentificationTypes(List<IdentificationType> identificationTypes);

    void setNextButtonListeners();

    void setBackButtonListeners();

    void setErrorContainerListener();

    void showMissingIdentificationTypesError(boolean recoverable, String requestOrigin);

    void showSettingNotFoundForBinError();

    void setContainerAnimationListeners();

    void setIdentificationTypeListeners();

    void setIdentificationNumberListeners();

    void hideSecurityCodeInput();

    void hideIdentificationInput();

    void showIdentificationInput();

    void setCardholderNameListeners();

    void setExpiryDateListeners();

    void setSecurityCodeListeners();

    void setIdentificationNumberRestrictions(String type);

    void hideBankDeals();

    void showBankDeals();

    void clearErrorView();

    void setErrorView(String message);

    void setErrorView(CardTokenException exception);

    void setErrorCardNumber();

    void setErrorCardholderName();

    void setErrorExpiryDate();

    void setErrorSecurityCode();

    void setErrorIdentificationNumber();

    void clearErrorIdentificationNumber();

    void initializeTitle();

    void setCardholderName(String cardholderName);

    void setIdentificationNumber(String identificationNumber);

    void setSoftInputMode();

    void finishCardFlow(@NonNull final List<Issuer> issuers);

    void finishCardFlow();

    void finishCardStorageFlowWithSuccess();

    void finishCardStorageFlowWithError(String accessToken);

    void showProgress();

    void hideProgress();

    void setExclusionWithOneElementInfoView(PaymentMethod supportedPaymentMethod, boolean withAnimation);

    void hideExclusionWithOneElementInfoView();

    void setInvalidCardOnePaymentMethodErrorView();

    void setInvalidIdentificationNumberErrorView();

    void setInvalidEmptyNameErrorView();

    void setInvalidExpiryDateErrorView();

    void setInvalidFieldErrorView();

    void setInvalidCardMultipleErrorView();

    void resolvePaymentMethodSet(PaymentMethod paymentMethod);

    void clearSecurityCodeEditText();

    void checkClearCardView();

    void hideRedErrorContainerView(boolean withAnimation);

    void restoreBlackInfoContainerView();

    void clearCardNumberInputLength();

    void askForPaymentType(List<PaymentMethod> paymentMethods, List<PaymentType> paymentTypes, CardInfo cardInfo);

    void showFinishCardFlow();

    void eraseDefaultSpace();

    void setPaymentMethod(PaymentMethod paymentMethod);

    void askForIssuer(CardInfo cardInfo, List<Issuer> issuers, PaymentMethod paymentMethod);

    void recoverCardViews(boolean lowResActive, String cardNumber, String cardHolderName, String expiryMonth,
        String expiryYear, String identificationNumber, IdentificationType identificationType);
}
