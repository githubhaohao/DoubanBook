package cn.haohao.dbbook.domain.executor

import java.util.concurrent.ThreadFactory

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class JobThreadFactory : ThreadFactory {

    private var counter: Int = 0

    override fun newThread(r: Runnable): Thread = Thread(r, "job-thread: ${counter++}")
}