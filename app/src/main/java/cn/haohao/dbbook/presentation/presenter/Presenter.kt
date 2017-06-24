package cn.haohao.dbbook.presentation.presenter

/**
 * Created by HaohaoChang on 2017/6/12.
 */
interface Presenter<out T, Params> {
    val view: T

    fun execute(params: Params)

    fun cancel()
}