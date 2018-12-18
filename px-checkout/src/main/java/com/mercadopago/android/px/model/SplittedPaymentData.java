package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import java.math.BigDecimal;

public class SplittedPaymentData extends PaymentData {

    private boolean isSplittedPayment = false;

    @NonNull private BigDecimal accountMoneyAmount = BigDecimal.ZERO;


}
