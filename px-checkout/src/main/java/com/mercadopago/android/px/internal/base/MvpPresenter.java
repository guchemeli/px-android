package com.mercadopago.android.px.internal.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

/**
 * Base class for all <code>MvpPresenter</code> implementations.
 * <p>
 * All <code>MvpPresenter</code>'s implementations MUST NOT contain references to Android library or api calls, that's
 * what it is <code>ResourcesProvider</code> made for.
 * <p>
 * See also {@link ResourcesProvider} See also {@link MvpView}
 */

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class MvpPresenter<V extends MvpView, R extends ResourcesProvider> {

    private transient WeakReference<V> mView;
    private transient R resourcesProvider;

    public void attachResourcesProvider(final R resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    public void attachView(final V view) {
        mView = new WeakReference<>(view);
    }

    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }

    public void fromBundle(@NonNull final Bundle bundle) {}

    @NonNull
    public Bundle toBundle(@NonNull final Bundle bundle) {
        return new Bundle();
    }

    public boolean isViewAttached() {
        return mView != null && mView.get() != null;
    }

    @NonNull
    public V getView() {
        if (mView == null) {
            throw new IllegalStateException("view not attached");
        } else {
            return mView.get();
        }
    }

    public R getResourcesProvider() {
        return resourcesProvider;
    }

    public void detachResourceProvider() {
        resourcesProvider = null;
    }
}