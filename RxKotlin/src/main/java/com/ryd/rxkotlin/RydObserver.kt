package com.ryd.rxkotlin

/**
 * 观察者
 */
interface RydObserver<T> {
    fun onSubscribe()
    fun onNext(item:T)
    fun onError(e:Throwable)
    fun onComplete()
}