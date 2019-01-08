package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.CustomSearchItem;
import com.mercadopago.android.px.model.AmountConfiguration;
import java.util.List;
import javax.annotation.Nonnull;

public class ConfigurationSolverImpl implements ConfigurationSolver {

    @NonNull private final String defaultSelectedAmountConfiguration;
    @NonNull private final List<CustomSearchItem> customSearchItems;

    public ConfigurationSolverImpl(
        @NonNull final String defaultSelectedAmountConfiguration,
        @NonNull final List<CustomSearchItem> customSearchItems) {
        this.defaultSelectedAmountConfiguration = defaultSelectedAmountConfiguration;
        this.customSearchItems = customSearchItems;
    }

    @Override
    @NonNull
    public String getDefaultSelectedAmountConfiguration() {
        return defaultSelectedAmountConfiguration;
    }

    @Override
    @NonNull
    public String getConfigurationHashFor(@Nonnull final String customOptionId) {
        for (final CustomSearchItem customSearchItem : customSearchItems) {
            if (customSearchItem.getId() != null && customSearchItem.getId().equals(customOptionId)) {
                return customSearchItem.getDefaultAmountConfiguration();
            }
        }
        return TextUtil.EMPTY;
    }

    @Override
    @Nullable
    public AmountConfiguration getPayerCostConfigurationFor(@NonNull final String customOptionId) {
        for (final CustomSearchItem customSearchItem : customSearchItems) {
            if (customSearchItem.getId() != null && customSearchItem.getId().equals(customOptionId)) {
                return customSearchItem.getPayerCostConfiguration(customSearchItem.getDefaultAmountConfiguration());
            }
        }
        return null;
    }

    @Override
    @Nullable
    public AmountConfiguration getPayerCostConfigurationFor(@NonNull final String customOptionId,
        @NonNull final String configurationHash) {
        for (final CustomSearchItem customSearchItem : customSearchItems) {
            if (customSearchItem.getId() != null && customSearchItem.getId().equals(customOptionId)) {
                return customSearchItem.getPayerCostConfiguration(configurationHash);
            }
        }
        return null;
    }
}

