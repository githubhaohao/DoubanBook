package cn.haohao.dbbook.domain.executor

import rx.Scheduler

/**
 * Created by HaohaoChang on 2017/6/9.
 */
interface PostExecutionThread {
    fun getScheduler(): Scheduler
}