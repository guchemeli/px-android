package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryDetailDescriptorFactory;
import com.mercadopago.android.px.internal.view.SummaryView;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.TotalDetailColor;
import com.mercadopago.android.px.internal.viewmodel.TotalLocalized;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryViewModelMapper extends Mapper<List<ExpressMetadata>, List<SummaryView.Model>> {

    @NonNull private final CheckoutPreference checkoutPreference;
    @NonNull private final DiscountRepository discountRepository;
    @NonNull private final AmountRepository amountRepository;
    @NonNull private final ElementDescriptorView.Model elementDescriptorModel;
    @NonNull private final AmountDescriptorView.OnClickListenerWithDiscount listener;

    private Map<DiscountConfigurationModel, SummaryView.Model> modelCache;

    public SummaryViewModelMapper(@NonNull final CheckoutPreference checkoutPreference,
        @NonNull final DiscountRepository discountRepository, @NonNull final AmountRepository amountRepository,
        @NonNull final ElementDescriptorView.Model elementDescriptorModel,
        @NonNull final AmountDescriptorView.OnClickListenerWithDiscount listener) {
        this.checkoutPreference = checkoutPreference;
        this.discountRepository = discountRepository;
        this.amountRepository = amountRepository;
        this.elementDescriptorModel = elementDescriptorModel;
        this.listener = listener;
    }

    @Override
    public List<SummaryView.Model> map(@NonNull final List<ExpressMetadata> expressMetadataList) {
        modelCache = new HashMap<>();
        final List<SummaryView.Model> models = new ArrayList<>();

        for (final ExpressMetadata expressMetadata : expressMetadataList) {
            final String customOptionId;
            if (expressMetadata.isCard()) {
                customOptionId = expressMetadata.getCard().getId();
            } else {
                // Account money
                customOptionId = expressMetadata.getPaymentMethodId();
            }
            models.add(createModel(discountRepository.getConfigurationFor(customOptionId)));
        }

        models.add(createModel(discountRepository.getConfigurationFor(TextUtil.EMPTY)));

        return models;
    }

    private SummaryView.Model createModel(@NonNull final DiscountConfigurationModel discountModel) {
        if (modelCache.containsKey(discountModel)) {
            return modelCache.get(discountModel);
        } else {
            final List<AmountDescriptorView.Model> summaryDetailList =
                new SummaryDetailDescriptorFactory(discountModel, checkoutPreference).create();

            final AmountDescriptorView.Model totalRow = new AmountDescriptorView.Model(
                new TotalLocalized(),
                new AmountLocalized(discountModel.getAmountWithDiscount(amountRepository.getItemsAmount()),
                    checkoutPreference.getSite().getCurrencyId()),
                new TotalDetailColor());

            final SummaryView.Model model = new SummaryView.Model(elementDescriptorModel, summaryDetailList, totalRow,
                new AmountDescriptorView.OnClickListener() {
                    @Override
                    public void onAmountDescriptorClicked() {
                        listener.onAmountDescriptorClicked(discountModel);
                    }
                });

            modelCache.put(discountModel, model);
            return model;
        }
    }
}
