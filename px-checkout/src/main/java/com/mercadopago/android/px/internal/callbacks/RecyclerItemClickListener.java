package com.mercadopago.android.px.internal.callbacks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private final GestureDetector mGestureDetector;
    private final OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(final View view, final int position);
    }

    public RecyclerItemClickListener(final Context context, final OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(final MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public void onTouchEvent(final RecyclerView rv, final MotionEvent e) {

    }

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView view, final MotionEvent e) {
        final View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {

    }
}
