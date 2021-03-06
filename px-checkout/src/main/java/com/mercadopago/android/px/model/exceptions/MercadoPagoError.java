package com.mercadopago.android.px.model.exceptions;

import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import java.io.Serializable;

public class MercadoPagoError implements Serializable {

    private String message;
    private String errorDetail;
    private String requestOrigin;
    private ApiException apiException;
    private final boolean recoverable;

    public MercadoPagoError(final String message, final boolean recoverable) {
        this.message = message;
        this.recoverable = recoverable;
    }

    public MercadoPagoError(String message, String detail, boolean recoverable) {
        this.message = message;
        errorDetail = detail;
        this.recoverable = recoverable;
    }

    public MercadoPagoError(ApiException apiException, String requestOrigin) {
        this.apiException = apiException;
        this.requestOrigin = requestOrigin;
        recoverable = apiException != null && apiException.isRecoverable();
    }

    public ApiException getApiException() {
        return apiException;
    }

    public boolean isRecoverable() {
        return recoverable;
    }

    public String getMessage() {
        if (message == null && getApiException() != null && !TextUtil.isEmpty(getApiException().getMessage())) {
            return getApiException().getMessage();
        }
        return message;
    }

    public String getRequestOrigin() {
        return requestOrigin;
    }

    public String getErrorDetail() {
        return errorDetail == null ? "" : errorDetail;
    }

    public boolean isApiException() {
        return apiException != null;
    }

    public boolean isPaymentProcessing() {
        return getApiException() != null
            && getApiException().getStatus() == ApiUtil.StatusCodes.PROCESSING;
    }

    public boolean isInternalServerError() {
        return getApiException() != null
            && String.valueOf(getApiException().getStatus())
            .startsWith(ApiUtil.StatusCodes.INTERNAL_SERVER_ERROR_FIRST_DIGIT);
    }

    public boolean isBadRequestError() {
        return getApiException() != null
            && (getApiException().getStatus() == ApiUtil.StatusCodes.BAD_REQUEST);
    }

    public boolean isNoConnectivityError() {
        return getApiException() != null
            && (getApiException().getStatus() == ApiUtil.StatusCodes.NO_CONNECTIVITY_ERROR);
    }

    @Override
    public String toString() {
        return "MercadoPagoError{" +
            "message='" + message + '\'' +
            ", errorDetail='" + errorDetail + '\'' +
            ", requestOrigin='" + requestOrigin + '\'' +
            ", apiException=" + apiException +
            ", recoverable=" + recoverable +
            '}';
    }
}
