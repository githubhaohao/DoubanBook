package cn.haohao.dbbook.domain.interactor

import cn.haohao.dbbook.domain.executor.PostExecutionThread
import cn.haohao.dbbook.domain.executor.ThreadExecutor
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by HaohaoChang on 2017/6/9.
 */
abstract class Interactor<T, Params>(val executor: ThreadExecutor, val postExecutionThread: PostExecutionThread) {
    private val mCompositeSubscription: CompositeSubscription = CompositeSubscription()

    abstract fun buildObservable(params: Params): Observable<T>

    open fun execute(subscriber: Subscriber<T>, params: Params) {
        val observable = buildObservable(params)
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.from(executor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber))
    }

    open fun cancel() {
        if (mCompositeSubscription.hasSubscriptions()) mCompositeSubscription.unsubscribe()
    }

}