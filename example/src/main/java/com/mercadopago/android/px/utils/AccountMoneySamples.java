package com.mercadopago.android.px.utils;

import android.support.v4.util.Pair;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.internal.features.plugins.SamplePaymentProcessorNoView;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.ExitAction;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mercadopago.android.px.utils.PaymentUtils.getGenericPaymentApproved;

public final class AccountMoneySamples {

    private static final String MERCHANT_PUBLIC_KEY = "TEST-4763b824-93d7-4ca2-a7f7-93539c3ee5bd";
    private static final String ACCOUNT_MONEY_ACCESS_TOKEN =
        "TEST-3465447741993645-121313-56b9e7f91689a6778c394e89fc94e061-386611132";
    private static final String ACCOUNT_MONEY_PAYER_EMAIL = "prueba@test_user_84540917@testuser.com";
    private static final String BUSINESS_PAYMENT_IMAGE_URL =
        "https://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg";
    private static final String BUSINESS_PAYMENT_TITLE = "Title";
    private static final String BUSINESS_PAYMENT_BUTTON_NAME = "ButtonSecondaryName";

    private AccountMoneySamples() {

    }

    public static void addAll(final Collection<Pair<String, MercadoPagoCheckout.Builder>> options) {
        options.add(new Pair<>("Account money with Generic Payment", startCheckoutWithGenericPayment()));
        options.add(new Pair<>("Account money with Business Payment", startCheckoutWithBusinessPayment()));
    }

    private static MercadoPagoCheckout.Builder startCheckoutWithGenericPayment() {
        final GenericPayment payment = getGenericPaymentApproved();
        final PaymentProcessor paymentProcessor = new SamplePaymentProcessorNoView(payment);
        final PaymentConfiguration paymentConfiguration = new PaymentConfiguration.Builder(paymentProcessor).build();

        return new MercadoPagoCheckout.Builder(MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(100), paymentConfiguration)
            .setPrivateKey(ACCOUNT_MONEY_ACCESS_TOKEN);
    }

    private static MercadoPagoCheckout.Builder startCheckoutWithBusinessPayment() {
        final BusinessPayment payment = new BusinessPayment.Builder(BusinessPayment.Decorator.APPROVED,
            Payment.StatusCodes.STATUS_APPROVED,
            Payment.StatusDetail.STATUS_DETAIL_ACCREDITED,
            BUSINESS_PAYMENT_IMAGE_URL,
            BUSINESS_PAYMENT_TITLE)
            .setPaymentMethodVisibility(true)
            .setSecondaryButton(new ExitAction(BUSINESS_PAYMENT_BUTTON_NAME, 34))
            .build();

        final PaymentProcessor paymentProcessor = new SamplePaymentProcessorNoView(payment);
        final PaymentConfiguration paymentConfiguration = new PaymentConfiguration.Builder(paymentProcessor).build();

        return new MercadoPagoCheckout.Builder(MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(100), paymentConfiguration)
            .setPrivateKey(ACCOUNT_MONEY_ACCESS_TOKEN);
    }

    private static CheckoutPreference getCheckoutPreferenceWithPayerEmail(final int amount) {
        final List<Item> items = new ArrayList<>();
        final Item item = new Item.Builder("Android", 1, new BigDecimal(amount))
            .setDescription("Androide")
            .setPictureUrl("https://www.androidsis.com/wp-content/uploads/2015/08/marshmallow.png")
            .setId("1234")
            .build();
        items.add(item);
        return new CheckoutPreference.Builder(Sites.ARGENTINA,
            ACCOUNT_MONEY_PAYER_EMAIL, items)
            .build();
    }
}
