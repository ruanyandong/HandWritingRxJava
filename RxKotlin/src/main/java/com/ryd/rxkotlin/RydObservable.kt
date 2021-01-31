package com.ryd.rxkotlin

import android.util.Log
import com.ryd.rxkotlin.donext.RydDoOnNextObservable
import com.ryd.rxkotlin.filter.RydFilterObservable
import com.ryd.rxkotlin.map.RydMapObservable
import com.ryd.rxkotlin.thread.RydObservableObservable
import com.ryd.rxkotlin.thread.RydSubscribeObservable

/**
 * 被观察者
 */
class RydObservable<T> constructor() {
    /**
     * 上游对象
     */
    private var source: RydObservableOnSubscribe<T>? = null

    /**
     * 次构造方法，用于接收上游对象
     */
    constructor(source: RydObservableOnSubscribe<T>) : this() {
        this.source = source
    }

    private var source2: ((RydObserver<T>) -> Unit)? = null

    constructor(source: (RydObserver<T>) -> Unit) : this() {
        this.source2 = source
    }

    /**
     * 静态方法创建一个真正的被观察者
     */
    companion object {
        fun <T> create(emitter: RydObservableOnSubscribe<T>): RydObservable<T> {
            return RydObservable(emitter)
        }

        fun <T> create(source2: (RydObserver<T>) -> Unit): RydObservable<T> {
            return RydObservable(source2)
        }

        fun <T> just(item: T): RydObservable<T> {
            return create {
                it.onNext(item)
            }
        }
    }

    /**
     * 这里接收一个下游对象
     */
    fun setObserver(downStream: RydObserver<T>) {
        Log.d("zzz", "setObserver: xujia")
        downStream.onSubscribe()
        source?.setObserver(downStream)
        source2?.invoke(downStream)
    }

    fun <R> map(func: (T) -> R): RydObservable<R> {
        //source就是上游真正的被观察者。
        val map = RydMapObservable(this.source!!, func)
        return RydObservable(map)
    }

    // 指定上游线程
    fun subscribeOn(thread: Int): RydObservable<T> {
        Log.d("zzz", "subscribeOn: 指定上游线程")
        val subscribe = RydSubscribeObservable(this.source!!, thread)
        return RydObservable(subscribe)
    }

    // 指定下游线程
    fun observableOn(thread: Int): RydObservable<T> {
        Log.d("zzz", "observableOn: 指定下游线程")
        val observable = RydObservableObservable(this.source!!, thread)
        return RydObservable(observable)
    }


    fun filter(func: (T) -> Boolean): RydObservable<T> {
        val map = RydFilterObservable(this.source!!, func)
        return RydObservable(map)
    }

    fun doOnNext(func: (T) -> Unit): RydObservable<T> {
        val subscribe = RydDoOnNextObservable(this.source!!, func)
        return RydObservable(subscribe)
    }
}