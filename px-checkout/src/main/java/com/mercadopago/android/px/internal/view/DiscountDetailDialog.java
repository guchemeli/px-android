package com.mercadopago.android.px.internal.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import com.mercadolibre.android.ui.widgets.MeliDialog;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.DiscountDetailContainer.Props.DialogTitleType;
import com.mercadopago.android.px.model.DiscountConfigurationModel;
import com.mercadopago.android.px.tracking.internal.views.AppliedDiscountViewTracker;

public class DiscountDetailDialog extends MeliDialog {

    private static final String TAG = DiscountDetailDialog.class.getName();
    private static final String ARG_DISCOUNT = "arg_discount";

    public static void showDialog(@NonNull final FragmentManager supportFragmentManager,
        @NonNull final DiscountConfigurationModel discountModel) {
        final DiscountDetailDialog discountDetailDialog = new DiscountDetailDialog();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_DISCOUNT, discountModel);
        discountDetailDialog.setArguments(arguments);
        discountDetailDialog.show(supportFragmentManager, TAG);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewGroup container = view.findViewById(R.id.main_container);

        final DiscountConfigurationModel discountModel = getArguments().getParcelable(ARG_DISCOUNT);
        new AppliedDiscountViewTracker(discountModel).track();
        final DiscountDetailContainer discountDetailContainer = new DiscountDetailContainer(
            new DiscountDetailContainer.Props(DialogTitleType.BIG, discountModel));
        discountDetailContainer.render(container);
    }

    @Override
    public int getContentView() {
        return R.layout.px_dialog_detail_discount;
    }

    @Nullable
    @Override
    public String getSecondaryExitString() {
        return getString(R.string.px_terms_and_conditions);
    }
}
