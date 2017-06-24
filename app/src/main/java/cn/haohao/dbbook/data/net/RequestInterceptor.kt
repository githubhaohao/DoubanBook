package cn.haohao.dbbook.data.net

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Created by HaohaoChang on 2017/6/9.
 *
 * 向服务期请求数据缓存1个小时
 */
class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val tempCacheControl = CacheControl.Builder()
                .maxStale(5, TimeUnit.SECONDS)
                .build()
        request = request.newBuilder()
                .cacheControl(tempCacheControl)
                .build()

        return chain.proceed(request)

    }
}