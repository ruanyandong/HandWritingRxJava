package com.ryd.rxkotlin.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.ryd.rxkotlin.RydObservableOnSubscribe
import com.ryd.rxkotlin.RydObserver
import java.util.concurrent.Executors

class Schedulers() {
    private var IOThreadPool = Executors.newCachedThreadPool()//IO线程池

    companion object {
        //定义一个线程安全的单例模式
        val INSTANCE: Schedulers by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Schedulers()
        }
        private const val IO = 0 //定义IO线程策略
        private const val MAIN = 1 //定义main线程策略

        fun IO():Int{
            return IO
        }

        fun mainThread():Int{
            return MAIN
        }
    }

    private var handler = Handler(Looper.getMainLooper()) { message ->
        //这里就是主线程了。
        message.callback.run()
        return@Handler true
    }


    fun <T> submitSubscribeWork(
            source: RydObservableOnSubscribe<T>, //上游
            downStream: RydObserver<T>,//下游
            thread: Int//指定的线程
    ) {
        when (thread) {
            IO -> {
                IOThreadPool.submit {
                    Log.d("zzz", "submitSubscribeWork: IO ")
                    //从线程池抽取一个线程执行上游和下游的连接操作
                    source.setObserver(downStream)
                }
            }
            MAIN -> {
                handler.let {
                    val message=Message.obtain(it){
                        source.setObserver(downStream)
                    }
                    it.sendMessage(message)
                }
            }
        }
    }

    fun submitObserverWork(function: () -> Unit, thread: Int) {
        when (thread) {
            IO -> {
                IOThreadPool?.submit {
                    function.invoke() //调用高阶函数
                }
            }
            MAIN -> {
                handler.let {
                    val m = Message.obtain(it) {
                        function.invoke()//调用高阶函数
                    }
                    it.sendMessage(m)
                }
            }
        }
    }

}