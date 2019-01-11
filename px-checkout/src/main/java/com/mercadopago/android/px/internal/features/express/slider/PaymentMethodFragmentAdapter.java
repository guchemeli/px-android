package com.mercadopago.android.px.internal.features.express.slider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.mercadopago.android.px.internal.util.ScaleUtil;
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMoneyDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.AddNewCardFragmentDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.PaymentMethodFragmentDrawer;
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableFragmentItem;
import java.util.List;

public class PaymentMethodFragmentAdapter extends FragmentStatePagerAdapter implements PaymentMethodFragmentDrawer {

    @NonNull private final List<DrawableFragmentItem> items;

    public static PaymentMethodFragmentAdapter with(@NonNull final Context context, @NonNull final FragmentManager fm,
        @NonNull final List<DrawableFragmentItem> drawableFragmentItems) {
        return ScaleUtil.isLowRes(context) ? new PaymentMethodFragmentAdapterLowRes(fm,
            drawableFragmentItems) : new PaymentMethodFragmentAdapter(fm, drawableFragmentItems);
    }

    protected PaymentMethodFragmentAdapter(@NonNull final FragmentManager fm,
        @NonNull final List<DrawableFragmentItem> drawableFragmentItems) {
        super(fm);
        items = drawableFragmentItems;
    }

    @Override
    public Fragment getItem(final int position) {
        return items.get(position).draw(this);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment draw(@NonNull final DrawableFragmentItem drawableFragmentItem) {
        throw new IllegalStateException("Unknown type - PaymentMethodFragmentAdapter");
    }

    @Override
    public Fragment draw(@NonNull final AddNewCardFragmentDrawableFragmentItem drawableItem) {
        return AddNewCardFragment.getInstance(drawableItem);
    }

    @Override
    public Fragment draw(@NonNull final SavedCardDrawableFragmentItem drawableItem) {
        return SavedCardFragment.getInstance(drawableItem);
    }

    @Override
    public Fragment draw(@NonNull final AccountMoneyDrawableFragmentItem drawableItem) {
        return AccountMoneyFragment.getInstance(drawableItem);
    }
}
