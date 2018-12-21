package com.mercadopago.android.px.internal.features.paymentresult;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.internal.base.MvpPresenter;
import com.mercadopago.android.px.internal.callbacks.FailureRecovery;
import com.mercadopago.android.px.internal.constants.ProcessingModes;
import com.mercadopago.android.px.internal.features.review_and_confirm.components.actions.ChangePaymentMethodAction;
import com.mercadopago.android.px.internal.repository.InstructionsRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.view.ActionsListener;
import com.mercadopago.android.px.internal.view.CopyAction;
import com.mercadopago.android.px.internal.view.LinkAction;
import com.mercadopago.android.px.internal.view.NextAction;
import com.mercadopago.android.px.internal.view.RecoverPaymentAction;
import com.mercadopago.android.px.internal.view.ResultCodeAction;
import com.mercadopago.android.px.internal.viewmodel.PostPaymentAction;
import com.mercadopago.android.px.model.Action;
import com.mercadopago.android.px.model.Instruction;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.views.ResultViewTrack;
import java.util.List;

/* default */ class PaymentResultPresenter extends MvpPresenter<PaymentResultPropsView, PaymentResultProvider>
    implements ActionsListener {
    private PaymentResult paymentResult;

    /* default */ final PaymentResultNavigator navigator;
    private final PaymentSettingRepository paymentSettings;
    private final InstructionsRepository instructionsRepository;

    private FailureRecovery failureRecovery;
    private boolean initialized = false;
    private PostPaymentAction.OriginAction originAction;

    /* default */ PaymentResultPresenter(@NonNull final PaymentResultNavigator navigator,
        final PaymentSettingRepository paymentSettings,
        final InstructionsRepository instructionsRepository) {
        this.navigator = navigator;
        this.paymentSettings = paymentSettings;
        this.instructionsRepository = instructionsRepository;
    }

    public void initialize() {
        if (!initialized) {
            onValidStart();
            initialized = true;
        }
    }

    protected void onValidStart() {
        new ResultViewTrack(ResultViewTrack.Style.GENERIC, paymentResult).track();
        getView().setPropPaymentResult(paymentSettings.getCheckoutPreference().getSite().getCurrencyId(),
            paymentResult,
            paymentResult.isOffPayment());
        checkGetInstructions();
    }

    public void setPaymentResult(final PaymentResult paymentResult) {
        this.paymentResult = paymentResult;
    }

    private void checkGetInstructions() {
        if (paymentResult.isOffPayment()) {
            getInstructionsAsync();
        } else {
            getView().notifyPropsChanged();
        }
    }

    /* default */ void getInstructionsAsync() {
        instructionsRepository.getInstructions(paymentResult)
            .enqueue(new Callback<List<Instruction>>() {
                @Override
                public void success(final List<Instruction> instructions) {
                    if (isViewAttached()) {
                        if (instructions.isEmpty()) {
                            navigator
                                .showError(
                                    new MercadoPagoError(getResourcesProvider().getStandardErrorMessage(), false),
                                    ApiUtil.RequestOrigin.GET_INSTRUCTIONS);
                        } else {
                            resolveInstructions(instructions);
                        }
                    }
                }

                @Override
                public void failure(final ApiException apiException) {
                    if (isViewAttached()) {
                        navigator.showError(new MercadoPagoError(apiException, ApiUtil.RequestOrigin.GET_INSTRUCTIONS),
                            ApiUtil.RequestOrigin.GET_INSTRUCTIONS);
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                getInstructionsAsync();
                            }
                        });
                    }
                }
            });
    }

    public void recoverFromFailure() {
        if (failureRecovery != null) {
            failureRecovery.recover();
        }
    }

    /* default */ void setFailureRecovery(final FailureRecovery failureRecovery) {
        this.failureRecovery = failureRecovery;
    }

    /* default */ void resolveInstructions(final List<Instruction> instructionsList) {
        final Instruction instruction = getInstruction(instructionsList);
        if (instruction == null) {
            navigator.showError(new MercadoPagoError(getResourcesProvider().getStandardErrorMessage(), false),
                ApiUtil.RequestOrigin.GET_INSTRUCTIONS);
        } else {
            getView().setPropInstruction(instruction, ProcessingModes.AGGREGATOR, false);
            getView().notifyPropsChanged();
        }
    }

    private Instruction getInstruction(final List<Instruction> instructions) {
        final Instruction instruction;
        if (instructions.size() == 1) {
            instruction = instructions.get(0);
        } else {
            instruction = getInstructionForType(instructions,
                paymentResult.getPaymentData().getPaymentMethod().getPaymentTypeId());
        }
        return instruction;
    }

    private Instruction getInstructionForType(final Iterable<Instruction> instructions, final String paymentTypeId) {
        Instruction instructionForType = null;
        for (final Instruction instruction : instructions) {
            if (instruction.getType().equals(paymentTypeId)) {
                instructionForType = instruction;
                break;
            }
        }
        return instructionForType;
    }

    @Override
    public void onAction(@NonNull final Action action) {
        if (action instanceof NextAction) {
            navigator.finishWithResult(MercadoPagoCheckout.PAYMENT_RESULT_CODE);
        } else if (action instanceof ResultCodeAction) {
            navigator.finishWithResult(((ResultCodeAction) action).resultCode);
        } else if (action instanceof ChangePaymentMethodAction) {
            navigator.changePaymentMethod();
        } else if (action instanceof RecoverPaymentAction) {
            navigator.recoverPayment(originAction);
        } else if (action instanceof LinkAction) {
            navigator.openLink(((LinkAction) action).url);
        } else if (action instanceof CopyAction) {
            navigator.copyToClipboard(((CopyAction) action).content);
        }
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
    }

    public void setOriginAction(@NonNull final PostPaymentAction.OriginAction originAction) {
        this.originAction = originAction;
    }
}
