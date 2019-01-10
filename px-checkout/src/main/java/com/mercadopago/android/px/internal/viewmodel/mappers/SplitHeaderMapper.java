package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.features.express.slider.SplitPaymentHeaderAdapter;
import com.mercadopago.android.px.model.ExpressMetadata;
import java.math.BigDecimal;

public class SplitHeaderMapper extends Mapper<ExpressMetadata, SplitPaymentHeaderAdapter.Model> {

    @NonNull private final String currencyId;

    public SplitHeaderMapper(@NonNull final String currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public SplitPaymentHeaderAdapter.Model map(@NonNull final ExpressMetadata val) {
        return new SplitPaymentHeaderAdapter.Split("Numero 1", new BigDecimal(10), currencyId, true);
    }
}
