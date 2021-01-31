package com.ryd.rxkotlin

/**
 * 真正的被观察者
 */
interface RydObservableOnSubscribe<T> {
    /**
     * 设置下游
     */
    fun setObserver(observer:RydObserver<T>) : Unit
}