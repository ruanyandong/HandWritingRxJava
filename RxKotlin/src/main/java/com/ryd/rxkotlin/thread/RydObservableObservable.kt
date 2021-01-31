package com.ryd.rxkotlin.thread

import android.util.Log
import com.ryd.rxkotlin.RydObservableOnSubscribe
import com.ryd.rxkotlin.RydObserver

/**
 * 指定下游线程
 */
class RydObservableObservable<T>(
        private val source: RydObservableOnSubscribe<T>,
        private val thread: Int //看这里，新增了指定线程
) : RydObservableOnSubscribe<T> {


    override fun setObserver(downStream: RydObserver<T>) {
        Log.d("zzz", "RydObservableOnSubscribe setObserver: thread MAIN")
        val observer = MlxObserverObserver(downStream, thread)
        source.setObserver(observer)
    }

    class MlxObserverObserver<T>(
            val downStream: RydObserver<T>,
            val thread: Int
    ) : RydObserver<T> {

        override fun onSubscribe() {
            Log.d("zzz", "onSubscribe: MAIN")
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onSubscribe()
            }, thread)
        }

        override fun onNext(item: T) {
            Log.d("zzz", "onNext: Main")
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onNext(item)
            }, thread)
        }

        override fun onError(e: Throwable) {
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onError(e)
            }, thread)
        }

        override fun onComplete() {
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onComplete()
            }, thread)
        }
    }
}