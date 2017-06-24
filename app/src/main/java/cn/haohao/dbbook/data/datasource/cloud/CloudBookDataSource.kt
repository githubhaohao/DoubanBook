package cn.haohao.dbbook.data.datasource.cloud

import cn.haohao.dbbook.data.BookService
import cn.haohao.dbbook.data.datasource.BookDataSource
import cn.haohao.dbbook.data.entity.http.BookListResponse
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import retrofit2.Response
import rx.Observable

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class CloudBookDataSource(val bookService: BookService) : BookDataSource {
    override fun requestBookList(queryKey: String?, tag: String?, start: Int, count: Int, fields: String): Observable<Response<BookListResponse>> {
        return bookService.getBookList(queryKey, tag, start, count, fields)
    }

    override fun requestBookDetail(bookId: String, start: Int, count: Int, fields: String): Observable<Response<BookReviewsListResponse>> {
        return bookService.getBookDetail(bookId, start, count, fields)
    }

    override fun requestRecommendedBooks(seriesId: String, start: Int, count: Int, fields: String): Observable<Response<BookSeriesListResponse>> {
        return bookService.getBookSeries(seriesId, start, count, fields)
    }
}