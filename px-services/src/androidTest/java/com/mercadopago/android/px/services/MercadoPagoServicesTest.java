package com.mercadopago.android.px.services;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MercadoPagoServicesTest {

    private static final String TEST_PK = "TEST-92f16019-1533-4f21-aaf9-70482692f41e";
    private static final String TEST_AT = "TEST-4008515596580497-071112-4d6622f6fb95cb093fd38760751ec98d-335851940";
    private static final int TIMEOUT = 10000;

    private static final String STUB_EMAIL = "dummy@email.com";
    private static final String STUB_TITLE = "title";
    private static final BigDecimal STUB_AMOUNT = new BigDecimal("100");

    private CountDownLatch lock;

    @Before
    public void setUp() {
        lock = new CountDownLatch(1);
    }

    @Test
    public void verifyPreferenceCreation() {
        final MercadoPagoServices mercadoPagoServices =
            new MercadoPagoServices(InstrumentationRegistry.getContext(), TEST_PK,
                TEST_AT);
        mercadoPagoServices.createPreference(new CheckoutPreference.Builder(
            Sites.ARGENTINA,
            STUB_EMAIL,
            Collections.singletonList(new Item.Builder(STUB_TITLE, 1,
                STUB_AMOUNT).build())
        ), new Callback<CheckoutPreference>() {
            @Override
            public void success(final CheckoutPreference preference) {
                assertFalse(preference.getId().isEmpty());
                lock.countDown();
            }

            @Override
            public void failure(final ApiException apiException) {
                throw new IllegalStateException("api call failed");
            }
        });

        try {
            lock.await(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            assertTrue(false);
        }
    }
}