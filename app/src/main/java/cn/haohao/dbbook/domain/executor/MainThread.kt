package cn.haohao.dbbook.domain.executor

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class MainThread : PostExecutionThread {
    override fun getScheduler(): Scheduler = AndroidSchedulers.mainThread()
}