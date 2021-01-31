package com.ryd.handwritingrxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ryd.rxkotlin.RydObservable;
import com.ryd.rxkotlin.RydObservableOnSubscribe;
import com.ryd.rxkotlin.RydObserver;
import com.ryd.rxkotlin.thread.Schedulers;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RydObservable.Companion.create(new RydObservableOnSubscribe<Integer>() {
            @Override
            public void setObserver(@NotNull RydObserver<Integer> observer) {
                Log.i("zzz", "上游线程:"+Thread.currentThread().getName());
                observer.onNext(10);
            }
        }).subscribeOn(Schedulers.Companion.IO())
                .observableOn(Schedulers.Companion.mainThread())
                .setObserver(new RydObserver<Integer>() {
                    @Override
                    public void onSubscribe() {
                        Log.d("zzz", "onSubscribe: MainActivity");
                    }

                    @Override
                    public void onNext(Integer item) {
                        Log.i("zzz", "下游线程:"+Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}