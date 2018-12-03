package com.mercadopago.android.px.testcheckout.pages;

import com.mercadopago.android.px.testcheckout.assertions.CheckoutValidator;
import com.mercadopago.android.testlib.pages.PageObject;

/**
 * @deprecated this page's flow does not exists anymore.
 */
@Deprecated
public class DiscountCongratsPage extends PageObject<CheckoutValidator> {

    public DiscountCongratsPage(final CheckoutValidator validator) {
        super(validator);
    }


    /**
     * @deprecated this page's flow does not exists anymore.
     * Button layout has been deleted. It doesn't exist no more.
     */
    @Deprecated
    public PaymentMethodPage pressContinueToPaymentMethod() {
        //TODO delete when new version launched.
        //onView(withId(R.id.button)).perform(click());
        return new PaymentMethodPage(validator);
    }

    @Override
    public DiscountCongratsPage validate(final CheckoutValidator validator) {
        validator.validate(this);
        return this;
    }
}
