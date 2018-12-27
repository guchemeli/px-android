package com.mercadopago.android.px.securitycode;

import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.internal.features.SecurityCodeActivityView;
import com.mercadopago.android.px.internal.features.SecurityCodePresenter;
import com.mercadopago.android.px.internal.features.providers.SecurityCodeProvider;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.mocks.Cards;
import com.mercadopago.android.px.mocks.Issuers;
import com.mercadopago.android.px.mocks.PaymentMethods;
import com.mercadopago.android.px.mocks.Tokens;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CardInfo;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.SavedCardToken;
import com.mercadopago.android.px.model.SavedESCCardToken;
import com.mercadopago.android.px.model.SecurityCode;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.Token;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.CardTokenException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.utils.MVPStructure;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecurityCodePresenterTest {

    private static final String PAYMENT_METHOD_NOT_SET = "payment_method_not_set";
    private static final String CARD_AND_TOKEN_NOT_SET = "card_and_token_not_set";
    private static final String CARD_AND_TOKEN_SET_WITHOUT_RECOVERY = "card_and_token_set_without_recovery";
    private static final String CARD_INFO_NOT_SET = "card_info_not_set";
    private static final int CARD_TOKEN_INVALID_SECURITY_CODE = 9;

    @Mock private PaymentSettingRepository configuration;
    @Mock private CheckoutPreference checkoutPreference;
    @Mock private Site site;

    @Before
    public void setUp() {
        when(configuration.getCheckoutPreference()).thenReturn(checkoutPreference);
        when(checkoutPreference.getSite()).thenReturn(site);
    }

    @Test
    public void showErrorWhenInvalidParameters() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();
        mvp.getPresenter().setCardInfo(new CardInfo(Tokens.getVisaToken()));

        mvp.getPresenter().initialize();

        Assert.assertTrue(mvp.getProvider().standardErrorMessageGotten);
        Assert.assertTrue(isErrorShown(mvp.getView()));
        Assert.assertFalse(mvp.getView().initializeDone);
        assertEquals(CARD_AND_TOKEN_NOT_SET, mvp.getProvider().errorMessage);
    }

    @Test
    public void ifPaymentMethodNotSetShowError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        mvp.getPresenter().setCard(Cards.getCard());

        mvp.getPresenter().initialize();

        assertTrue(mvp.getProvider().standardErrorMessageGotten);
        assertTrue(isErrorShown(mvp.getView()));
        assertFalse(mvp.getView().initializeDone);
        assertEquals(mvp.getProvider().errorMessage, PAYMENT_METHOD_NOT_SET);
    }

    @Test
    public void ifCardAndTokenNotSetShowError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        mvp.getPresenter().initialize();

        assertTrue(mvp.getProvider().standardErrorMessageGotten);
        assertTrue(isErrorShown(mvp.getView()));
        assertFalse(mvp.getView().initializeDone);
        assertEquals(mvp.getProvider().errorMessage, CARD_AND_TOKEN_NOT_SET);
    }

    @Test
    public void ifCardAndTokenWithoutRecoverySetShowError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        mvp.getPresenter().setCard(Cards.getCard());
        mvp.getPresenter().setToken(Tokens.getToken());

        mvp.getPresenter().initialize();

        assertTrue(mvp.getProvider().standardErrorMessageGotten);
        assertTrue(isErrorShown(mvp.getView()));
        assertFalse(mvp.getView().initializeDone);
        assertEquals(mvp.getProvider().errorMessage, CARD_AND_TOKEN_SET_WITHOUT_RECOVERY);
    }

    @Test
    public void ifCardInfoNotSetThenShowError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        mvp.getPresenter().setCard(Cards.getCard());
        mvp.getPresenter().setPaymentMethod(PaymentMethods.getPaymentMethodOnMaster());

        mvp.getPresenter().initialize();

        assertTrue(mvp.getProvider().standardErrorMessageGotten);
        assertTrue(isErrorShown(mvp.getView()));
        assertFalse(mvp.getView().initializeDone);
        assertEquals(mvp.getProvider().errorMessage, CARD_INFO_NOT_SET);
    }

    @Test
    public void ifCardAndTokenWithRecoverySetDontShowError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPM = PaymentMethods.getPaymentMethodOnVisa();

        mvp.getPresenter().setCard(Cards.getCard());
        mvp.getPresenter().setToken(Tokens.getTokenWithESC());
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForESC(mockedPM));
        mvp.getPresenter().setPaymentMethod(mockedPM);
        mvp.getPresenter().setCardInfo(new CardInfo(Cards.getCard()));

        mvp.getPresenter().initialize();

        assertFalse(mvp.getProvider().standardErrorMessageGotten);
        assertFalse(isErrorShown(mvp.getView()));
        assertTrue(mvp.getView().initializeDone);
    }

    @Test
    public void whenInitializedThenSetInputMaxLength() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();

        mvp.getPresenter().setCard(Cards.getCard());
        mvp.getPresenter().setToken(Tokens.getTokenWithESC());
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForESC(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(Cards.getCard()));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();
        assertEquals(mvp.getView().maxLength, mockedPaymentMethod.getSettings().get(0).getSecurityCode().getLength());
    }

    @Test
    public void whenInitializedWithoutSecurityCodeSettingsThenSetDefaultInputMaxLength() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMasterWithoutSecurityCodeSettings();
        Card mockedCard = Cards.getCardWithoutSecurityCodeSettings();

        mvp.getPresenter().setCard(mockedCard);
        mvp.getPresenter().setToken(Tokens.getTokenWithESC());
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForESC(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedCard));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();
        assertEquals(mvp.getView().maxLength, Card.CARD_DEFAULT_SECURITY_CODE_LENGTH);
    }

    @Test
    public void onCallForAuthRecoveryCloneToken() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        Token mockedToken = Tokens.getToken();

        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForCallForAuth(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedToken));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        mvp.getProvider().setCloneTokenResponse(mockedToken);
        mvp.getProvider().setPutSecurityCodeResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        assertEquals(mvp.getProvider().successfulCloneTokenResponse, mockedToken);
    }

    @Test
    public void onCallForAuthRecoveryCloneTokenThenPutSecurityCode() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        Token mockedToken = Tokens.getToken();

        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForCallForAuth(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedToken));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        mvp.getProvider().setCloneTokenResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        assertEquals(mvp.getProvider().successfulCloneTokenResponse, mockedToken);

        mvp.getProvider().setPutSecurityCodeResponse(mockedToken);
        mvp.getPresenter().putSecurityCode();
        assertEquals(mvp.getProvider().successfulPutSecurityCodeResponse, mockedToken);
    }

    @Test
    public void onCallForAuthRecoveryCloneTokenError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        final Token mockedToken = Tokens.getToken();

        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForCallForAuth(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedToken));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        final ApiException apiException = Tokens.getInvalidCloneToken();
        final MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        mvp.getProvider().setCloneTokenResponse(mpException);

        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        Assert.assertEquals(mvp.getProvider().failedResponse, mpException);
        Assert.assertTrue(mvp.getView().errorState);
    }

    @Test
    public void onCallForAuthRecoveryPutSecurityCodeAndGetError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        final Token mockedToken = Tokens.getToken();

        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForCallForAuth(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedToken));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        mvp.getProvider().setCloneTokenResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        Assert.assertEquals(mvp.getProvider().successfulCloneTokenResponse, mockedToken);

        final ApiException apiException = Tokens.getInvalidCloneToken();
        final MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        mvp.getProvider().setPutSecurityCodeResponse(mpException);

        mvp.getPresenter().putSecurityCode();
        Assert.assertEquals(mvp.getProvider().failedResponse, mpException);
        Assert.assertTrue(mvp.getView().errorState);
    }

    @Test
    public void onCloneTokenAndSecurityCodeInputIsNotValidThenShowError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        Token mockedToken = Tokens.getToken();

        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(getPaymentRecoveryForCallForAuth(mockedPaymentMethod));
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedToken));

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        mvp.getProvider().shouldFailSecurityCodeValidation = true;
        mvp.getProvider().setCloneTokenResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("1");
        mvp.getPresenter().validateSecurityCodeInput();

        assertTrue(mvp.getView().errorState);
        assertNotNull(mvp.getView().cardTokenErrorCode);
    }

    @Test
    public void onSaveCardWithESCEnabledThenCreateESCToken() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        Card mockedCard = Cards.getCard();

        mvp.getPresenter().setCard(mockedCard);
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedCard));

        //ESC enabled
        mvp.getProvider().enableESC(true);

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        Token mockedToken = Tokens.getTokenWithESC();
        mvp.getProvider().setCreateTokenWithEscResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        assertEquals(mvp.getProvider().successfulcreateESCTokenResponse, mockedToken);
    }

    @Test
    public void onSaveCardWithESCEnabledThenCreateESCTokenHasError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        Card mockedCard = Cards.getCard();

        mvp.getPresenter().setCard(mockedCard);
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedCard));

        //ESC enabled
        mvp.getProvider().enableESC(true);

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        ApiException apiException = Tokens.getInvalidTokenWithESC();
        MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        mvp.getProvider().setPutSecurityCodeResponse(mpException);

        mvp.getProvider().setCreateTokenWithEscResponse(mpException);
        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        assertEquals(mvp.getProvider().failedResponse, mpException);
        assertTrue(mvp.getView().errorState);
    }

    @Test
    public void onESCRecoverFromPaymentThenCreateESCToken() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        Token mockedToken = Tokens.getTokenWithESC();
        Card mockedCard = Cards.getCard();
        PaymentRecovery paymentRecovery = getPaymentRecoveryForESC(mockedPaymentMethod);

        mvp.getPresenter().setCard(mockedCard);
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedCard));
        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(paymentRecovery);

        //ESC enabled
        mvp.getProvider().enableESC(true);

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        mvp.getProvider().setCreateTokenWithEscResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        Assert.assertEquals(mvp.getProvider().successfulcreateESCTokenResponse, mockedToken);
    }

    @Test
    public void onSavedCardWithoutESCEnabledThenCreateToken() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        final Card mockedCard = Cards.getCard();

        mvp.getPresenter().setCard(mockedCard);
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedCard));

        //Disable ESC
        mvp.getProvider().enableESC(false);

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        final Token mockedToken = Tokens.getToken();
        mvp.getProvider().setCreateTokenWithSavedCardTokenResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        Assert.assertEquals(mvp.getProvider().successfulcreateTokenWithSavedCardTokenResponse, mockedToken);
    }

    @Test
    public void onSavedCardWithoutESCEnabledThenCreateTokenHasError() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        final Card mockedCard = Cards.getCard();

        mvp.getPresenter().setCard(mockedCard);
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedCard));

        //Disable ESC
        mvp.getProvider().enableESC(false);

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        final ApiException apiException = Tokens.getInvalidCreateToken();
        final MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        mvp.getProvider().setCreateTokenWithSavedCardTokenResponse(mpException);

        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        Assert.assertEquals(mvp.getProvider().failedResponse, mpException);
    }

    @Test
    public void onESCRecoverFromPaymentWithPaymentResultIntegrationThenCreateESCToken() {
        MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> mvp =
            getMVPStructure();

        final PaymentMethod mockedPaymentMethod = PaymentMethods.getPaymentMethodOnMaster();
        final Token mockedToken = Tokens.getTokenWithESC();
        final PaymentRecovery paymentRecovery = getPaymentRecoveryForESC(mockedPaymentMethod);

        //With wallet integration, with payment result with invalid esc in payment
        //we dont have a card, we only have a token in payment data
        mvp.getPresenter().setPaymentMethod(mockedPaymentMethod);
        mvp.getPresenter().setCardInfo(new CardInfo(mockedToken));
        mvp.getPresenter().setToken(mockedToken);
        mvp.getPresenter().setPaymentRecovery(paymentRecovery);

        //ESC enabled
        mvp.getProvider().enableESC(true);

        mvp.getPresenter().initialize();
        mvp.getPresenter().initializeSettings();

        //Input for security code
        mvp.getProvider().setCreateTokenWithEscResponse(mockedToken);
        mvp.getPresenter().saveSecurityCode("123");
        mvp.getPresenter().validateSecurityCodeInput();
        assertEquals(mvp.getProvider().successfulcreateESCTokenResponse, mockedToken);
    }

    private boolean isErrorShown(SecurityCodeMockedView view) {
        return !TextUtil.isEmpty(view.errorMessage);
    }

    private PaymentRecovery getPaymentRecoveryForESC(final PaymentMethod paymentMethod) {
        final Token mockedToken = Tokens.getTokenWithESC();
        final Issuer mockedIssuer = Issuers.getIssuerMLA();
        return new PaymentRecovery(mockedToken, paymentMethod, mockedIssuer,
            Payment.StatusCodes.STATUS_REJECTED, Payment.StatusDetail.STATUS_DETAIL_INVALID_ESC);
    }

    private PaymentRecovery getPaymentRecoveryForCallForAuth(final PaymentMethod paymentMethod) {
        final Token mockedToken = Tokens.getToken();
        final Issuer mockedIssuer = Issuers.getIssuerMLA();
        return new PaymentRecovery(mockedToken, paymentMethod, mockedIssuer,
            Payment.StatusCodes.STATUS_REJECTED, Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE);
    }

    private MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode> getMVPStructure() {
        final MVPStructure<SecurityCodePresenter, SecurityCodeMockedProvider, SecurityCodeMockedView, SecurityCode>
            mvpStructure = new MVPStructure<>();

        final SecurityCodeMockedView view = new SecurityCodeMockedView();

        final SecurityCodePresenter presenter = new SecurityCodePresenter(configuration);
        presenter.attachView(view);
        final SecurityCodeMockedProvider provider = new SecurityCodeMockedProvider();
        presenter.attachResourcesProvider(provider);

        mvpStructure.setPresenter(presenter);
        mvpStructure.setProvider(provider);
        mvpStructure.setView(view);

        return mvpStructure;
    }

    private static class SecurityCodeMockedProvider implements SecurityCodeProvider {

        private boolean isEscEnabled;
        /* default */ boolean standardErrorMessageGotten = false;
        private String errorMessage;
        private boolean putSecurityCodeShouldFail = false;
        private boolean cloneTokenShouldFail = false;
        private boolean createTokenWithSavedCardTokenShouldFail = false;
        private boolean createESCTokenShouldFail = false;
        private MercadoPagoError failedResponse;
        /* default */ Token successfulCloneTokenResponse;
        private Token successfulPutSecurityCodeResponse;
        private Token successfulcreateTokenWithSavedCardTokenResponse;
        private Token successfulcreateESCTokenResponse;
        private boolean shouldFailSecurityCodeValidation = false;

        public void enableESC(final boolean enable) {
            isEscEnabled = enable;
        }

        public String getStandardErrorMessageGotten() {
            standardErrorMessageGotten = true;
            return "We are going to fix it. Try later.";
        }

        @Override
        public String getTokenAndCardNotSetMessage() {
            errorMessage = CARD_AND_TOKEN_NOT_SET;
            return errorMessage;
        }

        @Override
        public String getTokenAndCardWithoutRecoveryCantBeBothSetMessage() {
            errorMessage = CARD_AND_TOKEN_SET_WITHOUT_RECOVERY;
            return errorMessage;
        }

        @Override
        public String getPaymentMethodNotSetMessage() {
            errorMessage = PAYMENT_METHOD_NOT_SET;
            return errorMessage;
        }

        @Override
        public String getCardInfoNotSetMessage() {
            errorMessage = CARD_INFO_NOT_SET;
            return errorMessage;
        }

        public void setCloneTokenResponse(final MercadoPagoError exception) {
            cloneTokenShouldFail = true;
            failedResponse = exception;
        }

        public void setCloneTokenResponse(final Token token) {
            cloneTokenShouldFail = false;
            successfulCloneTokenResponse = token;
        }

        @Override
        public void cloneToken(final String tokenId, final TaggedCallback<Token> taggedCallback) {
            if (cloneTokenShouldFail) {
                taggedCallback.onFailure(failedResponse);
            } else {
                taggedCallback.onSuccess(successfulCloneTokenResponse);
            }
        }

        public void setPutSecurityCodeResponse(final MercadoPagoError exception) {
            putSecurityCodeShouldFail = true;
            failedResponse = exception;
        }

        public void setPutSecurityCodeResponse(final Token token) {
            putSecurityCodeShouldFail = false;
            successfulPutSecurityCodeResponse = token;
        }

        @Override
        public void putSecurityCode(final String securityCode, final String tokenId,
            final TaggedCallback<Token> taggedCallback) {
            if (putSecurityCodeShouldFail) {
                taggedCallback.onFailure(failedResponse);
            } else {
                taggedCallback.onSuccess(successfulPutSecurityCodeResponse);
            }
        }

        public void setCreateTokenWithSavedCardTokenResponse(final MercadoPagoError exception) {
            createTokenWithSavedCardTokenShouldFail = true;
            failedResponse = exception;
        }

        public void setCreateTokenWithSavedCardTokenResponse(final Token token) {
            createTokenWithSavedCardTokenShouldFail = false;
            successfulcreateTokenWithSavedCardTokenResponse = token;
        }

        @Override
        public void createToken(final SavedCardToken savedCardToken, final TaggedCallback<Token> taggedCallback) {
            if (createTokenWithSavedCardTokenShouldFail) {
                taggedCallback.onFailure(failedResponse);
            } else {
                taggedCallback.onSuccess(successfulcreateTokenWithSavedCardTokenResponse);
            }
        }

        public void setCreateTokenWithEscResponse(final MercadoPagoError exception) {
            createESCTokenShouldFail = true;
            failedResponse = exception;
        }

        public void setCreateTokenWithEscResponse(final Token token) {
            createESCTokenShouldFail = false;
            successfulcreateESCTokenResponse = token;
        }

        @Override
        public void createToken(final SavedESCCardToken savedESCCardToken, final TaggedCallback<Token> taggedCallback) {
            if (createESCTokenShouldFail) {
                taggedCallback.onFailure(failedResponse);
            } else {
                taggedCallback.onSuccess(successfulcreateESCTokenResponse);
            }
        }

        @Override
        public boolean isESCEnabled() {
            return isEscEnabled;
        }

        @Override
        public void validateSecurityCodeFromToken(final String mSecurityCode, final PaymentMethod mPaymentMethod,
            final String firstSixDigits) throws CardTokenException {
            if (shouldFailSecurityCodeValidation) {
                throw new CardTokenException(CARD_TOKEN_INVALID_SECURITY_CODE);
            }
        }

        @Override
        public void validateSecurityCodeFromToken(final String mSecurityCode) {

        }

        @Override
        public void validateSecurityCodeFromToken(final SavedCardToken savedCardToken, final Card card) throws CardTokenException {
            if (shouldFailSecurityCodeValidation) {
                throw new CardTokenException(CARD_TOKEN_INVALID_SECURITY_CODE);
            }
        }
    }

    private class SecurityCodeMockedView implements SecurityCodeActivityView {
        private boolean screenTracked = false;
        private boolean loadingViewShown = false;
        private boolean finishWithResult = false;
        private boolean initializeDone = false;
        private boolean backSecurityCodeShown = false;
        private boolean frontSecurityCodeShown = false;
        private String errorMessage;
        private MercadoPagoError error;
        private boolean timerShown = false;
        private int maxLength;
        private boolean errorState = false;
        private Integer cardTokenErrorCode;

        @Override
        public void initialize() {
            initializeDone = true;
        }

        @Override
        public void setSecurityCodeInputMaxLength(final int length) {
            maxLength = length;
        }

        @Override
        public void showError(final MercadoPagoError error, final String requestOrigin) {
            this.error = error;
            errorMessage = error.getMessage();
            errorState = true;
        }

        @Override
        public void showApiExceptionError(final ApiException exception, final String requestOrigin) {
        }

        @Override
        public void setErrorView(final CardTokenException exception) {
            cardTokenErrorCode = exception.getErrorCode();
            errorState = true;
        }

        @Override
        public void clearErrorView() {
        }

        @Override
        public void showLoadingView() {
            loadingViewShown = true;
        }

        @Override
        public void stopLoadingView() {
            loadingViewShown = false;
        }

        @Override
        public void showTimer() {
            timerShown = true;
        }

        @Override
        public void finishWithResult() {
            finishWithResult = true;
        }

        @Override
        public void showBackSecurityCodeCardView() {
        }

        @Override
        public void showFrontSecurityCodeCardView() {
        }
    }
}
