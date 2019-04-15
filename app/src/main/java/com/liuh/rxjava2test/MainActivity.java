package com.liuh.rxjava2test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void observableUse(View view) {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(0);
                emitter.onNext(1);
                emitter.onNext(2);
            }
        });

        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: " + integer);
            }
        });
    }

    public void rangeAndMap(View view) {
        Observable.range(0, 10)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.valueOf(integer);
                    }
                })
                .forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }


    /**
     * defer 直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
     * <p>
     * defer操作符会一直等待直到有观察者订阅它，然后它使用Observable工厂方法生成一个Observable。
     * <p>
     * 比如下面的代码两个订阅输出的结果是不一致的
     *
     * @param view
     */
    public void defer(View view) {
        Observable<Long> defer = Observable.defer(new Callable<ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> call() throws Exception {
                return Observable.just(System.currentTimeMillis());
            }
        });

        defer.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.e(TAG, "accept: " + aLong);
            }
        });

        Log.e(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        defer.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.e(TAG, "accept: " + aLong);
            }
        });
    }

    public void repeat(View view) {
        Observable.range(5, 10)
                .repeat(10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    public void timer(View view) {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "accept: " + aLong);
                    }
                });
    }


    public void map(View view) {
        Observable.range(1, 5)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }

    /**
     * 强制类型转换，如果无法转换，将会抛出异常
     * <p>
     * 是 map 操作符的一种特殊版本，内部还是调用的 map(...)
     *
     * @param view
     */
    public void cast(View view) {
        Observable.just(new Date())
                .cast(Object.class)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e(TAG, "accept: " + o);
                    }
                });
    }

    /**
     * 将一个发送事件的上游 Observable 变换为多个发送事件的 Observables，然后将它们发射的事件合并后放进一个单独的 Observable 里
     * <p>
     * 不保证事件发送的顺序
     *
     * @param view
     */
    public void flatMap(View view) {
        Observable.range(1, 10)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        int delay = 0;
                        if (integer % 3 == 0) {
                            delay = 500;
                        }

                        return Observable.just(String.valueOf(integer)).delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }

    public void contactMap(View view) {
        Observable.range(1, 10)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        int delay = 0;
                        if (integer % 3 == 0) {
                            delay = 500;
                        }

                        return Observable.just(String.valueOf(integer)).delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }

    /**
     * flatMapIterable 可以用来将上流的任意一个元素转换成一个Iterable对象，然后我们可以对其进行消费
     *
     * @param view
     */
    public void flatMapIterable(View view) {
        Observable.range(0, 5)
                .flatMapIterable(new Function<Integer, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(Integer integer) throws Exception {
                        return Collections.singletonList(String.valueOf(integer));
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }


    public void buffer(View view) {
        Observable.range(0, 5)
                .buffer(3)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.e(TAG, "accept: " + Arrays.toString(integers.toArray()));
                    }
                });
    }

    /**
     * groupBy用于分组元素，它可以被用来根据指定的条件将元素分成若干组。
     * 它将得到一个Observable<GroupedObservable<T, M>>类型的Observable
     * <p>
     * 以下函数打印结果：，1，1，2，2，3，3，4，4，5，5，6
     *
     * @param view
     */
    public void groupBy(View view) {
        Observable<GroupedObservable<Integer, Integer>> observable
                = Observable.concat(Observable.range(1, 4), Observable.range(1, 6))
                .groupBy(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer;
                    }
                });

        Observable.concat(observable).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: " + integer);
            }
        });
    }

    /**
     * scan操作符对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。
     * 它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列。
     *
     * 打印结果：2,6,24,120,720
     *
     * @param view
     */
    public void scan(View view) {
        Observable.range(2, 5)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer * integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "scan...accept: " + integer);
                    }
                });
    }
}
