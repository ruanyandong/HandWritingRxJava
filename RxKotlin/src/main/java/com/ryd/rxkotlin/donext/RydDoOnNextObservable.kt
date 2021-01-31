package com.ryd.rxkotlin.donext

import com.ryd.rxkotlin.RydObservableOnSubscribe
import com.ryd.rxkotlin.RydObserver

class RydDoOnNextObservable <T>(val source: RydObservableOnSubscribe<T>, val func:((T)->Unit)): RydObservableOnSubscribe<T> {

    override fun setObserver(emitter: RydObserver<T>){
        val map= MlxDoOnNextObserver(emitter,func)
        source.setObserver(map)
    }

    class MlxDoOnNextObserver<T>(val emitter: RydObserver<T>, val func:((T)->Unit)): RydObserver<T> {

        override fun onSubscribe() {
            emitter.onSubscribe()
        }

        override fun onNext(item: T) {
            val result=func.invoke(item)
            emitter.onNext(item)
        }

        override fun onError(e: Throwable) {
            emitter.onError(e)
        }

        override fun onComplete() {
            emitter.onComplete()
        }

    }

}