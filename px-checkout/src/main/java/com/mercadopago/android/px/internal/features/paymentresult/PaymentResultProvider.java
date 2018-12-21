package com.mercadopago.android.px.internal.features.paymentresult;

import com.mercadopago.android.px.internal.base.ResourcesProvider;

public interface PaymentResultProvider extends ResourcesProvider {

    String getStandardErrorMessage();

    String getApprovedTitle();

    String getPendingTitle();

    String getRejectedOtherReasonTitle(final String paymentMethodName);

    String getRejectedInsufficientAmountTitle(final String paymentMethodName);

    String getRejectedDuplicatedPaymentTitle(final String paymentMethodName);

    String getRejectedCardDisabledTitle(final String paymentMethodName);

    String getRejectedBadFilledCardTitle(final String paymentMethodName);

    String getRejectedCardDisabledBodyErrorDescription(String paymentMethodName);

    String getRejectedCallForAuthBodyActionText(final String paymentMethodName);

    String getRejectedBadFilledCardTitle();

    String getRejectedHighRiskTitle();

    String getRejectedMaxAttemptsTitle();

    String getRejectedInsufficientDataTitle();

    String getRejectedBadFilledOther();

    String getRejectedCallForAuthorizeTitle();

    String getEmptyText();

    String getPendingLabel();

    String getRejectionLabel();

    String getCancelPayment();

    String getContinueShopping();

    String getChangePaymentMethodLabel();

    String getCardEnabled();

    String getErrorTitle();

    String getPendingContingencyBodyErrorDescription();

    String getPendingReviewManualBodyErrorDescription();

    String getRejectedCallForAuthBodyErrorDescription();

    String getRejectedInsufficientAmountBodyErrorDescription();

    String getRejectedInsufficientAmountBodyErrorSecondDescription();

    String getRejectedOtherReasonBodyErrorDescription();

    String getRejectedByBankBodyErrorDescription();

    String getRejectedInsufficientDataBodyErrorDescription();

    String getRejectedDuplicatedPaymentBodyErrorDescription();

    String getRejectedMaxAttemptsBodyErrorDescription();

    String getRejectedHighRiskBodyErrorDescription();

    String getRejectedCallForAuthBodySecondaryTitle();

    String getReceiptDescription(final Long receiptId);
}
