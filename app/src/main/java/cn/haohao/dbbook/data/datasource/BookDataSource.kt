package cn.haohao.dbbook.data.datasource

import cn.haohao.dbbook.data.entity.http.BookListResponse
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import retrofit2.Response
import rx.Observable

/**
 * Created by HaohaoChang on 2017/6/9.
 */
interface BookDataSource {

    fun requestBookList(queryKey: String?, tag: String?, start: Int, count: Int, fields: String)
            : Observable<Response<BookListResponse>>

    fun requestBookDetail(bookId: String, start: Int, count: Int, fields: String)
            : Observable<Response<BookReviewsListResponse>>

    fun requestRecommendedBooks(seriesId:String, start: Int, count: Int, fields: String)
            : Observable<Response<BookSeriesListResponse>>
}