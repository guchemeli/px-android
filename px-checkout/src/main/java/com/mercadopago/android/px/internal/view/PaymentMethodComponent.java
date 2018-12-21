package com.mercadopago.android.px.internal.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.PaymentData;
import com.mercadopago.android.px.model.PaymentMethod;

public class PaymentMethodComponent extends Component<PaymentMethodComponent.PaymentMethodProps, Void> {

    static {
        RendererFactory.register(PaymentMethodComponent.class, PaymentMethodRenderer.class);
    }

    public static class PaymentMethodProps implements Parcelable {

        /* default */ final TotalAmount.Props totalAmountProps;
        @Nullable
        /* default */ final String lastFourDigits;
        @Nullable
        /* default */ final String disclaimer;

        /* default */ final PaymentMethod paymentMethod;

        private PaymentMethodProps(final PaymentMethod paymentMethod,
            @Nullable final String lastFourDigits,
            @Nullable final String disclaimer,
            final TotalAmount.Props totalAmountProps) {
            this.paymentMethod = paymentMethod;
            this.lastFourDigits = lastFourDigits;
            this.disclaimer = disclaimer;
            this.totalAmountProps = totalAmountProps;
        }

        public static PaymentMethodProps with(@NonNull final PaymentData paymentData,
            @NonNull final String currencyId,
            @NonNull final String statementDescription) {
            final TotalAmount.Props totalAmountProps =
                new TotalAmount.Props(currencyId, paymentData.getTransactionAmount(),
                    paymentData.getPayerCost(), paymentData.getDiscount());

            return new PaymentMethodComponent.PaymentMethodProps(paymentData.getPaymentMethod(),
                paymentData.getToken() != null ? paymentData.getToken().getLastFourDigits() : null,
                statementDescription,
                totalAmountProps);
        }

        protected PaymentMethodProps(final Parcel in) {
            paymentMethod = in.readParcelable(PaymentMethod.class.getClassLoader());
            totalAmountProps = in.readParcelable(TotalAmount.Props.class.getClassLoader());
            lastFourDigits = in.readString();
            disclaimer = in.readString();
        }

        public static final Creator<PaymentMethodProps> CREATOR = new Creator<PaymentMethodProps>() {
            @Override
            public PaymentMethodProps createFromParcel(final Parcel in) {
                return new PaymentMethodProps(in);
            }

            @Override
            public PaymentMethodProps[] newArray(final int size) {
                return new PaymentMethodProps[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeParcelable(paymentMethod, flags);
            dest.writeParcelable(totalAmountProps, flags);
            dest.writeString(lastFourDigits);
            dest.writeString(disclaimer);
        }
    }

    public PaymentMethodComponent(@NonNull final PaymentMethodProps props) {
        super(props);
    }
}
