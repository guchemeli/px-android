package com.mercadopago.utils;

import com.mercadopago.mocks.PaymentMethods;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentData;
import com.mercadopago.model.PaymentResult;

public final class StubPaymentResult {

    private StubPaymentResult() {
    }

    public static PaymentResult stubApprovedOffPaymentResult(){
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(PaymentMethods.getPaymentMethodOff());
        return new PaymentResult.Builder()
                .setPaymentData(paymentData)
                .setPaymentStatus(Payment.StatusCodes.STATUS_APPROVED)
                .build();
    }
}
