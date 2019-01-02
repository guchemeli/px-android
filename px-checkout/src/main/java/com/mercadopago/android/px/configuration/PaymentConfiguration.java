package com.mercadopago.android.px.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.core.PaymentMethodPlugin;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.model.commission.ChargeRule;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
public class PaymentConfiguration {

    @NonNull private final PaymentProcessor paymentProcessor;
    @NonNull private final ArrayList<ChargeRule> charges;

    protected PaymentConfiguration(@NonNull final PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
        charges = new ArrayList<>();
    }

    /* default */ PaymentConfiguration(@NonNull final Builder builder) {
        paymentProcessor = builder.paymentProcessor;
        charges = builder.charges;
    }

    @NonNull
    public PaymentProcessor getPaymentProcessor() {
        return paymentProcessor;
    }

    @NonNull
    public ArrayList<ChargeRule> getCharges() {
        return charges;
    }

    @Deprecated
    @Nullable
    public DiscountConfiguration getDiscountConfiguration() {
        return null;
    }

    @Deprecated
    @NonNull
    public Collection<PaymentMethodPlugin> getPaymentMethodPluginList() {
        return new ArrayList<>();
    }

    public static final class Builder {

        /* default */ @NonNull final PaymentProcessor paymentProcessor;
        /* default */ @NonNull final ArrayList<PaymentMethodPlugin> paymentMethodPluginList;
        /* default */ @NonNull ArrayList<ChargeRule> charges;

        /**
         * @param paymentProcessor your custom payment processor.
         */
        public Builder(@NonNull final PaymentProcessor paymentProcessor) {
            this.paymentProcessor = paymentProcessor;
            paymentMethodPluginList = new ArrayList<>();
            charges = new ArrayList<>();
        }

        /**
         * Add extra charges that will apply to total amount.
         *
         * @param charges the list of charges that could apply.
         * @return builder to keep operating
         */
        public Builder addChargeRules(@NonNull final Collection<ChargeRule> charges) {
            this.charges.addAll(charges);
            return this;
        }

        /**
         * Add your own payment method option to pay. Deprecated on version 4.5.0 due to native support of account money
         * feature. This method is now NOOP.
         *
         * @param paymentMethodPlugin your payment method plugin.
         * @return builder
         */
        @Deprecated
        public Builder addPaymentMethodPlugin(@NonNull final PaymentMethodPlugin paymentMethodPlugin) {
            return this;
        }

        /**
         * {@link DiscountConfiguration} is an object that represents the discount to be applied or error information to
         * present to the user.
         * <p>
         * it's mandatory to handle your discounts by hand if you set a payment processor.
         *
         * @param discountConfiguration your custom discount configuration
         * @return builder to keep operating
         * @deprecated this configuration is not longer valid - NOOP method
         */
        @Deprecated
        public Builder setDiscountConfiguration(@NonNull final DiscountConfiguration discountConfiguration) {
            return this;
        }

        @NonNull
        public PaymentConfiguration build() {
            return new PaymentConfiguration(this);
        }
    }
}
