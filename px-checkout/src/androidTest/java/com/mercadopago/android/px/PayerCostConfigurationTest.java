package com.mercadopago.android.px;

import android.os.Parcel;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.PayerCostConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PayerCostConfigurationTest {

    @Test
    public void parcelPayerCostConfiguration() {
        final Map<String, List<PayerCost>> configuration = new HashMap<>();
        final List<PayerCost> payerCosts = new ArrayList<>();
        configuration.put("configuration", payerCosts);
        final PayerCostConfiguration payerCostConfigurationBeforeParcel = new PayerCostConfiguration(2, configuration);

        final Parcel parcel = Parcel.obtain();
        payerCostConfigurationBeforeParcel.writeToParcel(parcel, payerCostConfigurationBeforeParcel.
            describeContents());
        parcel.setDataPosition(0);
        final PayerCostConfiguration payerCostConfigurationAfterParcel =
            PayerCostConfiguration.CREATOR.createFromParcel(parcel);

        assertEquals(payerCostConfigurationBeforeParcel, payerCostConfigurationAfterParcel);
    }
}
