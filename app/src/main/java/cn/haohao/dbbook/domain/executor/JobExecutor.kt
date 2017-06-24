package cn.haohao.dbbook.domain.executor

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class JobExecutor : ThreadExecutor {

    private val threadPoolExecutor = ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,
            LinkedBlockingDeque(), JobThreadFactory())

    override fun execute(command: Runnable?) {
        threadPoolExecutor.execute(command)
    }
}