package com.mercadopago.android.px.model.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.IPaymentDescriptorHandler;

/**
 * Parcelable version for IPayment description.
 */
public final class IParcelablePaymentDescriptor implements IPaymentDescriptor, Parcelable {

    private final String paymentTypeId;
    private final String paymentMethodId;
    @Nullable private final Long id;
    @Nullable private final String statementDescription;
    private final String paymentStatus;
    private final String paymentStatusDetail;

    private IParcelablePaymentDescriptor(@NonNull final String paymentStatus,
        @NonNull final String paymentStatusDetail,
        @NonNull final String paymentTypeId,
        @NonNull final String paymentMethodId,
        @Nullable final Long id,
        @Nullable final String statementDescription) {
        this.paymentTypeId = paymentTypeId;
        this.paymentMethodId = paymentMethodId;
        this.id = id;
        this.statementDescription = statementDescription;
        this.paymentStatus = paymentStatus;
        this.paymentStatusDetail = paymentStatusDetail;
    }

    public static IParcelablePaymentDescriptor with(@NonNull final IPayment iPayment) {
        return new IParcelablePaymentDescriptor(
            iPayment.getPaymentStatus(),
            iPayment.getPaymentStatusDetail(),
            null, null,
            iPayment.getId(),
            iPayment.getStatementDescription()
        );
    }

    public static IParcelablePaymentDescriptor with(@NonNull final IPaymentDescriptor iPaymentDescriptor) {
        return new IParcelablePaymentDescriptor(
            iPaymentDescriptor.getPaymentStatus(),
            iPaymentDescriptor.getPaymentStatusDetail(),
            iPaymentDescriptor.getPaymentTypeId(),
            iPaymentDescriptor.getPaymentMethodId(),
            iPaymentDescriptor.getId(),
            iPaymentDescriptor.getStatementDescription()
        );
    }

    private IParcelablePaymentDescriptor(final Parcel in) {
        paymentTypeId = in.readString();
        paymentMethodId = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        statementDescription = in.readString();
        paymentStatus = in.readString();
        paymentStatusDetail = in.readString();
    }

    public static final Creator<IParcelablePaymentDescriptor> CREATOR = new Creator<IParcelablePaymentDescriptor>() {
        @Override
        public IParcelablePaymentDescriptor createFromParcel(final Parcel in) {
            return new IParcelablePaymentDescriptor(in);
        }

        @Override
        public IParcelablePaymentDescriptor[] newArray(final int size) {
            return new IParcelablePaymentDescriptor[size];
        }
    };

    @NonNull
    @Override
    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    @NonNull
    @Override
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    @Override
    public void process(@NonNull final IPaymentDescriptorHandler handler) {
        handler.visit(this);
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
        return paymentStatus;
    }

    @NonNull
    @Override
    public String getPaymentStatusDetail() {
        return paymentStatusDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(paymentTypeId);
        dest.writeString(paymentMethodId);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(statementDescription);
        dest.writeString(paymentStatus);
        dest.writeString(paymentStatusDetail);
    }
}
