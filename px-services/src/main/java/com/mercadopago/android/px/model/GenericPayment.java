package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.internal.IParcelablePaymentDescriptor;

@SuppressWarnings("unused")
@Deprecated
public final class GenericPayment implements IPayment, Parcelable {

    @Nullable public final Long id;
    @NonNull public final String status;
    @NonNull public final String statusDetail;
    @Nullable public final String statementDescription;

    private GenericPayment(final Builder builder) {
        id = builder.paymentId;
        status = builder.status;
        statusDetail = builder.statusDetail;
        statementDescription = builder.statementDescription;
    }

    private GenericPayment(final Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        status = in.readString();
        statusDetail = in.readString();
        statementDescription = in.readString();
    }

    /**
     * Constructor for non-splited payment ; where is not neccessary to say which payment method have failed.
     */
    @Deprecated
    public GenericPayment(@NonNull final Long paymentId, @NonNull final String status,
        @NonNull final String statusDetail) {
        id = paymentId;
        this.status = status;
        this.statusDetail = processStatusDetail(status, statusDetail);
        statementDescription = null;
    }

    @Deprecated
    public GenericPayment(@Nullable final Long paymentId,
        @NonNull final String status,
        @NonNull final String statusDetail,
        @NonNull final String statementDescription) {
        id = paymentId;
        this.status = status;
        this.statusDetail = processStatusDetail(status, statusDetail);
        this.statementDescription = statementDescription;
    }

    /**
     * Resolve the status type, it transforms a generic status and detail into a known status detail {@link
     * Payment.StatusDetail }
     *
     * @param status the payment status type
     * @param statusDetail the payment detail type
     * @return an status detail type
     */
    private String processStatusDetail(@NonNull final String status, @NonNull final String statusDetail) {

        if (Payment.StatusCodes.STATUS_APPROVED.equals(status)) {
            return Payment.StatusDetail.STATUS_DETAIL_APPROVED_PLUGIN_PM;
        }

        if (Payment.StatusCodes.STATUS_REJECTED.equals(status)) {
            if (Payment.StatusDetail.isKnownErrorDetail(statusDetail)) {
                return statusDetail;
            } else {
                return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_PLUGIN_PM;
            }
        }

        return statusDetail;
    }

    @Nullable
    @Override
    public Long getId() {
        return id;
    }

    @Nullable
    @Override
    public String getStatementDescription() {
        return statementDescription;
    }

    @NonNull
    @Override
    public String getPaymentStatus() {
        return status;
    }

    @NonNull
    @Override
    public String getPaymentStatusDetail() {
        return statusDetail;
    }

    public static final Creator<GenericPayment> CREATOR = new Creator<GenericPayment>() {
        @Override
        public GenericPayment createFromParcel(final Parcel in) {
            return new GenericPayment(in);
        }

        @Override
        public GenericPayment[] newArray(final int size) {
            return new GenericPayment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(status);
        dest.writeString(statusDetail);
        dest.writeString(statementDescription);
    }

    public static class Builder {

        @Nullable /* default */ Long paymentId;
        @NonNull /* default */ final String status;
        @NonNull /* default */ final String statusDetail;
        @Nullable /* default */ String statementDescription;

        public Builder(@NonNull final String status, @NonNull final String statusDetail) {
            this.status = status;
            this.statusDetail = statusDetail;
        }

        public Builder setStatementDescription(@Nullable final String statementDescription) {
            this.statementDescription = statementDescription;
            return this;
        }

        public Builder setPaymentId(@Nullable final Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public GenericPayment createGenericPayment() {
            return new GenericPayment(this);
        }
    }
}