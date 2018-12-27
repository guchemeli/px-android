package com.mercadopago.android.px.model;

import java.io.Serializable;

public class PaymentRecovery implements Serializable {
    private final Token mToken;
    private final String mStatusDetail;
    private final PaymentMethod mPaymentMethod;
    private final Issuer mIssuer;

    public PaymentRecovery(final Token token,
        final PaymentMethod paymentMethod,
        final Issuer issuer,
        final String paymentStatus,
        final String paymentStatusDetail) {

        validate(token, paymentMethod, issuer, paymentStatus, paymentStatusDetail);
        mToken = token;
        mPaymentMethod = paymentMethod;
        mIssuer = issuer;
        mStatusDetail = paymentStatusDetail;
    }

    private void validate(final Token token, final PaymentMethod paymentMethod, final Issuer issuer,
        final String paymentStatus, final String paymentStatusDetail) {
        if (token == null) {
            throw new IllegalStateException("token is null");
        }

        if (paymentMethod == null) {
            throw new IllegalStateException("payment method is null");
        }

        if (issuer == null) {
            throw new IllegalStateException("issuer is null");
        }

        if (!Payment.StatusDetail.isRecoverablePaymentStatus(paymentStatus, paymentStatusDetail)) {
            throw new IllegalStateException("this payment is not recoverable");
        }
    }

    public Token getToken() {
        return mToken;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public Issuer getIssuer() {
        return mIssuer;
    }

    public boolean isTokenRecoverable() {
        return Payment.StatusDetail.isStatusDetailRecoverable(mStatusDetail);
    }

    public boolean isStatusDetailCallForAuthorize() {
        return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE.equals(mStatusDetail);
    }

    public boolean isStatusDetailCardDisabled() {
        return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED.equals(mStatusDetail);
    }

    public boolean isStatusDetailInvalidESC() {
        return Payment.StatusDetail.STATUS_DETAIL_INVALID_ESC.equals(mStatusDetail);
    }
}
