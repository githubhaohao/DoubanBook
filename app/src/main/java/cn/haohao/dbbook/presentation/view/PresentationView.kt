package cn.haohao.dbbook.presentation.view

/**
 * Created by HaohaoChang on 2017/6/15.
 */
interface PresentationView {
    fun onError(error: String)
    fun showProgressView()
    fun hideProgressView()
}