package com.opentrivia.app.framework.presenter

import com.opentrivia.app.framework.view.BaseView
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


open class BasePresenter<V : BaseView> {

    lateinit var view: WeakReference<V>
    lateinit var subscription: CompositeDisposable

    /**
     * Bind the view to current presenter
     *
     * @param view
     */
    fun bindView(view: V) {
        this.view = WeakReference(view)
        subscription = CompositeDisposable()
    }

    /**
     * Unbind the view from current presenter
     */
    fun unbindView() {
        view.clear()
        subscription.dispose()
    }

    /**
     * Get and return the binded view from current presenter
     *
     * @return the binded view
     */
    fun getView(): V? {
        return view.get()
    }
}