package com.mercadopago.android.px.model;

import java.io.Serializable;

//TODO move to internal package.
public class PaymentRecovery implements Serializable {

    private final String statusDetail;

    public PaymentRecovery(final String paymentStatusDetail) {
        statusDetail = paymentStatusDetail;
    }

    public boolean isTokenRecoverable() {
        return Payment.StatusDetail.isStatusDetailRecoverable(statusDetail);
    }

    public boolean isStatusDetailCallForAuthorize() {
        return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE.equals(statusDetail);
    }

    public boolean isStatusDetailCardDisabled() {
        return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED.equals(statusDetail);
    }

    public boolean isStatusDetailInvalidESC() {
        return Payment.StatusDetail.STATUS_DETAIL_INVALID_ESC.equals(statusDetail);
    }
}
