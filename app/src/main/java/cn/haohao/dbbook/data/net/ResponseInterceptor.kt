package cn.haohao.dbbook.data.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by HaohaoChang on 2017/6/9.
 *
 * 针对那些服务器不支持缓存策略的情况下，使用强制修改响应头，达到缓存的效果
 * 响应拦截只不过是出于规范，向服务器发出请求，至于服务器搭不搭理我们我们不管他，我们在响应里面做手脚，有网没有情况下的缓存策略
 */
class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val originalResponse = chain.proceed(request)
        var maxAge = 60 * 60 * 24
        return originalResponse.newBuilder()
                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build()


    }
}