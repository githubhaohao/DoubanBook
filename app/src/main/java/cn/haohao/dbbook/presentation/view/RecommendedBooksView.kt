package cn.haohao.dbbook.presentation.view

import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse

/**
 * Created by HaohaoChang on 2017/6/15.
 */
interface RecommendedBooksView {
    fun onGetRecommendedBooksError(error: String)
    fun showRecommendedBooks(recommendedBooks: BookSeriesListResponse)
}