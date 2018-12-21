package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.internal.view.Renderer;

/**
 * Created by vaserber on 11/13/17.
 */

public class AccreditationCommentRenderer extends Renderer<AccreditationComment> {

    @Override
    public View render(@NonNull final AccreditationComment component, @NonNull final Context context,
        final ViewGroup parent) {
        final View accreditationCommentView = inflate(R.layout.px_accreditation_comment, parent);
        final MPTextView commentTextView = accreditationCommentView.findViewById(R.id.mpsdkAccreditationTimeComment);
        setText(commentTextView, component.props);
        return accreditationCommentView;
    }
}
