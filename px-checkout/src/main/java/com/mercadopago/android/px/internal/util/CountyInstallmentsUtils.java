package com.mercadopago.android.px.internal.util;

import com.mercadopago.android.px.model.Sites;
import java.util.Collection;
import java.util.HashSet;

public final class CountyInstallmentsUtils {

    private CountyInstallmentsUtils() {
    }

    public static boolean shouldWarnAboutBankInterests(final String siteId) {
        return !TextUtil.isEmpty(siteId) && getSitesWithBankInterestsNotIncluded().contains(siteId);
    }

    private static Collection<String> getSitesWithBankInterestsNotIncluded() {
        final Collection<String> bankInterestsNotIncludedInInstallmentsSites = new HashSet<>();
        bankInterestsNotIncludedInInstallmentsSites.add(Sites.COLOMBIA.getId());
        return bankInterestsNotIncludedInInstallmentsSites;
    }
}
