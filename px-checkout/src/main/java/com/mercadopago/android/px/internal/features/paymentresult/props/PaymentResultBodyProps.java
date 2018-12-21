package com.mercadopago.android.px.internal.features.paymentresult.props;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.configuration.PaymentResultScreenConfiguration;
import com.mercadopago.android.px.model.Instruction;
import com.mercadopago.android.px.model.PaymentResult;

public class PaymentResultBodyProps {

    public final Instruction instruction;
    public final String processingMode;
    public final String currencyId;
    public PaymentResultScreenConfiguration paymentResultScreenConfiguration;
    public PaymentResult paymentResult;

    public PaymentResultBodyProps(@NonNull final Builder builder) {
        paymentResult = builder.paymentResult;
        instruction = builder.instruction;
        processingMode = builder.processingMode;
        currencyId = builder.currencyId;
        paymentResultScreenConfiguration = builder.paymentResultScreenConfiguration;
    }

    public Builder toBuilder() {
        return new Builder(paymentResultScreenConfiguration)
            .setCurrencyId(currencyId)
            .setInstruction(instruction)
            .setProcessingMode(processingMode)
            .setPaymentResult(paymentResult);
    }

    public static class Builder {

        public Instruction instruction;
        public String processingMode;
        public String currencyId;

        public PaymentResultScreenConfiguration paymentResultScreenConfiguration;

        public PaymentResult paymentResult;

        public Builder(@NonNull final PaymentResultScreenConfiguration paymentResultScreenConfiguration) {
            this.paymentResultScreenConfiguration = paymentResultScreenConfiguration;
        }

        public Builder setInstruction(final Instruction instruction) {
            this.instruction = instruction;
            return this;
        }

        public Builder setProcessingMode(final String processingMode) {
            this.processingMode = processingMode;
            return this;
        }

        public Builder setCurrencyId(final String currencyId) {
            this.currencyId = currencyId;
            return this;
        }

        public Builder setPaymentResult(final PaymentResult paymentResult) {
            this.paymentResult = paymentResult;
            return this;
        }

        public PaymentResultBodyProps build() {
            return new PaymentResultBodyProps(this);
        }
    }
}
