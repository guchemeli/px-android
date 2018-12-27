package com.mercadopago.android.px.mocks;

import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.SummaryAmount;
import com.mercadopago.android.px.utils.ResourcesUtil;

public final class StubSummaryAmount {
    private StubSummaryAmount() {
    }

    public static SummaryAmount getSummaryAmountEmptyPayerCosts() {
        return getFromFile("summary_amount_empty_payer_costs.json");
    }

    public static SummaryAmount getSummaryAmountOnePayerCosts() {
        return getFromFile("summary_amount_one_payer_cost.json");
    }

    public static SummaryAmount getSummaryAmountTwoPayerCosts() {
        return getFromFile("summary_amount_two_payer_costs.json");
    }

    private static SummaryAmount getFromFile(final String s) {
        return JsonUtil.getInstance()
            .fromJson(ResourcesUtil.getStringResource(s),
                SummaryAmount.class);
    }
}
