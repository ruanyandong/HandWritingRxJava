package com.ryd.rxkotlin.filter

import com.ryd.rxkotlin.RydObservableOnSubscribe
import com.ryd.rxkotlin.RydObserver

class RydFilterObservable <T>(val source: RydObservableOnSubscribe<T>, val func:((T)->Boolean)): RydObservableOnSubscribe<T> {

    override fun setObserver(emitter: RydObserver<T>){
        val map= MlxFilterObserver(emitter,func)
        source.setObserver(map)
    }

    class MlxFilterObserver<T>(val emitter: RydObserver<T>, val func:((T)->Boolean)): RydObserver<T> {

        override fun onSubscribe() {
            emitter.onSubscribe()
        }

        override fun onNext(item: T) {
            val result=func.invoke(item)
            if(result){
                emitter.onNext(item)
            }
        }

        override fun onError(e: Throwable) {
            emitter.onError(e)
        }

        override fun onComplete() {
            emitter.onComplete()
        }

    }

}