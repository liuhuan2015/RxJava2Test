package com.liuh.rxjava2test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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

    /**
     * from 从指定的数据源中获取Observable
     *
     * @param view
     */
    public void fromCallable(View view) {
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return Observable.just(1, 2, 3, 4, 5);
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.e(TAG, "accept: " + o);
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
     * <p>
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

    /**
     * 和Buffer类似，但不是发射来自原始Observable的数据包，它发射的是Observable，
     * 这些Observables中的每一个都发射原始Observable数据的一个子集，最后发射一个onCompleted通知。
     *
     * @param view
     */
    public void window(View view) {
//        Observable.range(1, 10)
//                .window(3)
//                .subscribe(new Consumer<Observable<Integer>>() {
//                    @Override
//                    public void accept(Observable<Integer> integerObservable) throws Exception {
//                        Log.e(TAG, "accept: " + integerObservable.hashCode() + ": " + integerObservable);
//                    }
//                });

        Observable.range(1, 10)
                .window(3)
                .subscribe(observable
                        -> observable.subscribe(integer
                        -> Log.e(TAG, "window: " + observable.hashCode() + ": " + integer)));

    }

    /**
     * 根据指定的规则对源进行过滤
     *
     * @param view
     */
    public void filter(View view) {
        Observable.range(1, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 5;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: filter: " + integer);
            }
        });
    }

    /**
     * 获取源中指定位置的数据
     * <p>
     * elementAt & firstElement & lastElement
     *
     * @param view
     */
    public void elementAt(View view) {
        Observable.range(1, 10)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: elementAt: " + integer);
                    }
                });
    }

    /**
     * 对源中重复的数据进行过滤
     * <p>
     * distinct & distinctUntilChanged
     *
     * @param view
     */
    public void distinct(View view) {
        Observable.just(1, 2, 2, 6, 6, 5, 5, 8, 9, 10)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: distinct: " + integer);
                    }
                });
    }

    /**
     * skip 用于过滤掉数据的前n项
     * <p>
     * skip & skipLast & skipUntil & skipWhile
     *
     * @param view
     */
    public void skip(View view) {
        Observable.range(1, 5)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: skip: " + integer);
                    }
                });
    }

    /**
     * 和 skip 方法对应，表示按照某种规则进行选择操作
     *
     * @param view
     */
    public void take(View view) {
        Observable.range(1, 5)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: take: " + integer);
                    }
                });
    }

    /**
     * startWith 在指定的数据源之前插入数据
     *
     * @param view
     */
    public void startWith(View view) {
        Observable.range(1, 5)
                .startWith(0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * marge
     *
     * @param view
     */
    public void merge(View view) {
        Observable.merge(Observable.range(1, 5), Observable.range(6, 5))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: merge:" + integer);
                    }
                });
    }

    /**
     * concat
     *
     * @param view
     */
    public void concat(View view) {
        Observable.concat(Observable.range(1, 5), Observable.range(6, 5))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: concat: " + integer);
                    }
                });
    }

    /**
     * do 系列
     *
     * @param view
     */
    public void doSeries(View view) {
        Observable.range(1, 5)
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.e(TAG, "accept: doOnEach: " + integerNotification);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "run: doOnComplete");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "run: doFinally");
                    }
                })
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: doAfterNext: " + integer);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e(TAG, "accept: Subscribe");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "run: terminate");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * timeout
     *
     * @param view
     */
    public void timeout(View view) {
        Observable.interval(1000, 200, TimeUnit.MILLISECONDS)
                .timeout(500, TimeUnit.MILLISECONDS, Observable.rangeLong(1, 5))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "accept: aLong: " + aLong);
                    }
                });
    }


    /**
     * retry
     *
     * @param view
     */
    public void retry(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(0);
                emitter.onError(new Throwable("Error 1"));
            }
        }).retry(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: throwable: " + throwable.getMessage());
            }
        });
    }


    /**
     * all 要发射的数据项是否全部满足指定的要求
     *
     * @param view
     */
    public void all(View view) {
        Observable.range(5, 5).all(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 5;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.e(TAG, "accept: aBoolean：" + aBoolean);
            }
        });
    }

    /**
     * toSortedList
     *
     * @param view
     */
    public void toSortedList(View view) {
        Observable.range(1, 5).toSortedList(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        }).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                Log.e(TAG, "accept: integers: " + integers);
            }
        });
    }

    /**
     * to 转换
     *
     * @param view
     */
    public void toConvert(View view) {
        Observable.range(1, 5)
                .to((Function<Observable<Integer>, Observable>) integerObservable
                        -> integerObservable).subscribe(o -> Log.e(TAG, "accept: " + o.toString()));
    }
}
