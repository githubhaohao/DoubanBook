package cn.haohao.dbbook.presentation.view

import cn.haohao.dbbook.data.entity.http.BookInfoResponse

/**
 * Created by HaohaoChang on 2017/6/10.
 */
interface BookListView : PresentationView {
    fun refreshData(books: List<BookInfoResponse>)
    fun addData(books: List<BookInfoResponse>)
}