package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.CircleTransform;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ElementDescriptorView extends LinearLayout {

    private static final int DEFAULT_MAX_LINES = -1;

    private TextView label;
    private ImageView icon;

    public ElementDescriptorView(final Context context) {
        this(context, null);
    }

    public ElementDescriptorView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElementDescriptorView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.PXElementDescriptorView,
            0, 0);
        float iconHeight;
        float iconWidth;
        float labelSize;
        int textColor;
        int textMaxLines;
        try {
            iconHeight =
                a.getDimension(R.styleable.PXElementDescriptorView_px_element_icon_height, LayoutParams.WRAP_CONTENT);
            iconWidth =
                a.getDimension(R.styleable.PXElementDescriptorView_px_element_icon_width, LayoutParams.WRAP_CONTENT);
            labelSize = a.getDimensionPixelSize(R.styleable.PXElementDescriptorView_px_element_label_size,
                (int) context.getResources().getDimension(R.dimen.px_l_text));
            textColor = a.getColor(R.styleable.PXElementDescriptorView_px_element_label_text_color, Color.BLACK);
            textMaxLines = a.getInt(R.styleable.PXElementDescriptorView_px_element_label_max_lines, DEFAULT_MAX_LINES);
        } finally {
            a.recycle();
        }

        init(iconWidth, iconHeight, labelSize, textColor, textMaxLines);
    }

    public void setTextSize(final float textSize) {
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setTextColor(final int textColor) {
        label.setTextColor(textColor);
    }

    public void setIconSize(final int width, final int height) {
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        icon.setLayoutParams(layoutParams);
    }

    private void init(final float iconWidth, final float iconHeight, final float labelSize, final int textColor,
        final int textMaxLines) {
        inflate(getContext(), R.layout.px_view_element_descriptor, this);
        label = findViewById(R.id.label);
        icon = findViewById(R.id.icon);
        setIconSize((int) iconWidth, (int) iconHeight);
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize);
        label.setTextColor(textColor);
        if (textMaxLines != DEFAULT_MAX_LINES) {
            label.setMaxLines(textMaxLines);
        }
    }

    public void update(@NonNull final ElementDescriptorView.Model model) {
        label.setText(model.getLabel());
        final Picasso picasso = Picasso.with(getContext());
        final RequestCreator requestCreator;

        if (TextUtil.isNotEmpty(model.getUrlIcon())) {
            requestCreator = picasso.load(model.getUrlIcon());
        } else {
            requestCreator = picasso.load(model.getIconResourceId());
        }

        requestCreator
            .transform(new CircleTransform())
            .placeholder(model.getIconResourceId())
            .error(model.getIconResourceId())
            .into(icon);
    }

    public static class Model {

        @NonNull private final String label;
        @NonNull private final String urlIcon;
        @DrawableRes private final int resourceIdIcon;

        public Model(@NonNull final String label, @DrawableRes final int resource) {
            this.label = label;
            resourceIdIcon = resource;
            urlIcon = "";
        }

        public Model(@NonNull final String label, @Nullable final String urlIcon,
            @DrawableRes final int defaultResource) {
            this.label = label;
            this.urlIcon = urlIcon == null ? "" : urlIcon;
            resourceIdIcon = defaultResource;
        }

        @NonNull
            /* default */ String getLabel() {
            return label;
        }

        @NonNull
            /* default */ String getUrlIcon() {
            return urlIcon;
        }

        @DrawableRes
            /* default */ int getIconResourceId() {
            return resourceIdIcon;
        }
    }
}
