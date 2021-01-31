package com.ryd.rxkotlin.thread

import android.util.Log
import com.ryd.rxkotlin.RydObservableOnSubscribe
import com.ryd.rxkotlin.RydObserver

/**
 * 指定上游线程
 */
class RydSubscribeObservable<T>(
        private val source: RydObservableOnSubscribe<T>,
        private val thread: Int //看这里，新增了指定线程
) : RydObservableOnSubscribe<T> {

    override fun setObserver(emitter: RydObserver<T>) {
        Log.d("zzz", "RydSubscribeObservable: setObserver thread IO")
        val downStream = RydSubscribeObserver(emitter)
        //提交任务给指定线程，也就是再指定线程完成上下游的链接
        Schedulers.INSTANCE.submitSubscribeWork(source, downStream, thread)
    }

    class RydSubscribeObserver<T>(val emitter: RydObserver<T>) : RydObserver<T> {

        override fun onSubscribe() {
            Log.d("zzz", "RydSubscribeObservable onSubscribe IO")
            emitter.onSubscribe()
        }

        override fun onNext(item: T) {
            Log.d("zzz", "RydSubscribeObservable onNext IO")
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