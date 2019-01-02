package com.mercadopago.android.px.internal.features.installments;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.exceptions.ApiException;
import java.math.BigDecimal;
import java.util.List;

public interface InstallmentsView extends MvpView {

    void showApiErrorScreen(final ApiException apiException, final String requestOrigin);

    void showInstallments(final List<PayerCost> payerCostList);

    void finishWithResult();

    void showLoadingView();

    void hideLoadingView();

    void showErrorNoPayerCost();

    void warnAboutBankInterests();

    void showDetailDialog(@NonNull final DiscountConfigurationModel discountModel);

    void showAmount(@NonNull final DiscountConfigurationModel discountModel, @NonNull final BigDecimal itemsPlusCharges,
        @NonNull final Site site);
}
