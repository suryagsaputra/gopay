package com.gojek.gopaysdk.data.rest.request

import com.gojek.gopaysdk.data.exception.ApiError
import com.gojek.gopaysdk.data.rest.callback.ApiCallback
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> execute(callback: ApiCallback<T>, exec: () -> Observable<T>): Subscription {
    return execute(exec)
        .subscribe(object : Subscriber<T>() {
            override fun onCompleted() = Unit
            override fun onError(e: Throwable) = callback.onError(ApiError(e))
            override fun onNext(result: T?) = callback.onSuccess(result)
        })
}

fun <T> execute(exec: () -> Observable<T>): Observable<T> {
    return exec()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext { throwable -> Observable.error(throwable) }
}