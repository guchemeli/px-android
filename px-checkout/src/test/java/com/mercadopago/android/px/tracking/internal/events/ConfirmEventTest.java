package com.mercadopago.android.px.tracking.internal.events;

import com.mercadopago.android.px.model.AccountMoneyMetadata;
import com.mercadopago.android.px.model.CardDisplayInfo;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PayerCost;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmEventTest {

    private static final String EXPECTED_PATH = "/px_checkout/review/confirm";
    private static final String EXPECTED_JUST_CARD =
        "{payment_method_type=credit_card, payment_method_id=visa, extra_info={issuer_id=0.0, card_id=123, selected_installment={quantity=1.0, installment_amount=10.0, visible_total_price=10.0, interest_rate=10.0}, has_esc=false}, review_type=one_tap}";
    private static final String EXPECTED_JUST_AM =
        "{payment_method_type=account_money, payment_method_id=account_money, extra_info={balance=10.0, invested=true}, review_type=one_tap}";

    @Mock private ExpressMetadata expressMetadata;
    @Mock private Set<String> cardIdsWithEsc;

    @Test
    public void whenGetEventPathVerifyIsCorrect() {
        final ConfirmEvent event = ConfirmEvent.from(cardIdsWithEsc, expressMetadata, mock(PayerCost.class));
        assertEquals(EXPECTED_PATH, event.getEventPath());
    }

    @Test
    public void whenExpressMetadataHasAccountMoneyThenShowItInMetadata() {
        final AccountMoneyMetadata am = mock(AccountMoneyMetadata.class);
        when(expressMetadata.getPaymentMethodId()).thenReturn("account_money");
        when(expressMetadata.getPaymentTypeId()).thenReturn("account_money");
        when(expressMetadata.getAccountMoney()).thenReturn(am);
        when(am.getBalance()).thenReturn(BigDecimal.TEN);
        when(am.isInvested()).thenReturn(true);
        final ConfirmEvent event = ConfirmEvent.from(cardIdsWithEsc, expressMetadata, mock(PayerCost.class));
        assertEquals(EXPECTED_JUST_AM, event.getEventData().toString());
    }

    @Test
    public void whenExpressMetadataHasSavedCardThenShowItInMetadata() {
        final CardMetadata card = mock(CardMetadata.class);
        final PayerCost payerCost = mock(PayerCost.class);
        final CardDisplayInfo cardDisplayInfo = mock(CardDisplayInfo.class);

        when(payerCost.getTotalAmount()).thenReturn(BigDecimal.TEN);
        when(payerCost.getInstallmentAmount()).thenReturn(BigDecimal.TEN);
        when(payerCost.getInstallments()).thenReturn(1);
        when(payerCost.getInstallmentRate()).thenReturn(BigDecimal.TEN);
        when(card.getId()).thenReturn("123");
        when(card.getDisplayInfo()).thenReturn(cardDisplayInfo);

        when(expressMetadata.getPaymentMethodId()).thenReturn("visa");
        when(expressMetadata.getPaymentTypeId()).thenReturn("credit_card");
        when(expressMetadata.getCard()).thenReturn(card);
        when(expressMetadata.isCard()).thenReturn(true);

        final ConfirmEvent event = ConfirmEvent.from(cardIdsWithEsc, expressMetadata, payerCost);

        assertEquals(EXPECTED_JUST_CARD, event.getEventData().toString());
    }
}