package com.ryd.rxkotlin.map

import com.ryd.rxkotlin.RydObservableOnSubscribe
import com.ryd.rxkotlin.RydObserver


class RydMapObservable<T, R>(
        private val source: RydObservableOnSubscribe<T>,
        private val func: ((T) -> R)
) : RydObservableOnSubscribe<R> {

    override fun setObserver(downStream: RydObserver<R>) {
        //此时的downStream就是真正的下游
        val map = RydMapObserver(downStream, func)//创建自己的观察者对象
        source.setObserver(map)//将自己传递给上游
    }

    /**
     * 观察者内部进行数据转换
     */
    class RydMapObserver<T, R>(
            private val downStream: RydObserver<R>,
            private val func: ((T) -> R)
    ) : RydObserver<T> {

        override fun onSubscribe() {
            downStream.onSubscribe()//当接收到上游传来的订阅后，将事件传递给下游
        }

        override fun onNext(item: T) {
            //应用转换规则，得到转换后的数据
            val result = func(item) //func.invoke(item)也可以
            //将转换后的数据传递给下游
            downStream.onNext(result)
        }

        override fun onError(e: Throwable) {
            //将错误传递给下游
            downStream.onError(e)
        }

        override fun onComplete() {
            //完成事件传递给下游
            downStream.onComplete()
        }

    }


}