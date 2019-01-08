package com.mercadopago.android.px.internal.features.express.installments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.Site;
import java.util.List;

import static com.mercadopago.android.px.model.AmountConfiguration.NO_SELECTED;

public class InstallmentsAdapter extends RecyclerView.Adapter<InstallmentRowHolder> {

    @NonNull private final Site site;
    @NonNull private List<PayerCost> payerCosts;
    private int payerCostSelected;
    @NonNull private final ItemListener itemListener;

    public interface ItemListener {
        void onClick(final PayerCost payerCostSelected);
    }

    public InstallmentsAdapter(@NonNull final Site site,
        @NonNull final List<PayerCost> payerCosts, @NonNull final ItemListener itemListener) {
        this.site = site;
        this.payerCosts = payerCosts;
        this.itemListener = itemListener;
        payerCostSelected = NO_SELECTED;
    }

    public InstallmentsAdapter(@NonNull final Site site,
        @NonNull final List<PayerCost> payerCosts,
        final int payerCostSelected,
        @NonNull final ItemListener itemListener) {
        this.site = site;
        this.payerCosts = payerCosts;
        this.payerCostSelected = payerCostSelected;
        this.itemListener = itemListener;
    }

    public void setPayerCosts(@NonNull final List<PayerCost> payerCosts) {
        this.payerCosts = payerCosts;
    }

    public void setPayerCostSelected(final int payerCostSelected) {
        this.payerCostSelected = payerCostSelected;
    }

    @Override
    public InstallmentRowHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View installmentView =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.px_view_payer_cost_item, parent, false);
        return new InstallmentRowHolder(installmentView);
    }

    @Override
    public void onBindViewHolder(final InstallmentRowHolder holder, final int position) {
        holder.populate(itemListener, site, payerCosts.get(position));

        if (position == payerCostSelected) {
            holder.highLight();
        } else {
            holder.noHighLight();
        }
    }

    @Override
    public int getItemCount() {
        return payerCosts.size();
    }
}
