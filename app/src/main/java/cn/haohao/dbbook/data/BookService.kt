package cn.haohao.dbbook.data

import cn.haohao.dbbook.data.entity.http.BookListResponse
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by HaohaoChang on 2017/6/9.
 */
interface BookService {

    companion object {
        val BASE_URL = "https://api.douban.com/v2/"
    }

    @GET("book/search")
    fun getBookList(@Query("q") q: String?, @Query("tag") tag: String?, @Query("start") start: Int, @Query("count") count: Int, @Query("fields") fields: String): Observable<Response<BookListResponse>>

    @GET("book/{bookId}/reviews")
    fun getBookDetail(@Path("bookId") bookId: String, @Query("start") start: Int, @Query("count") count: Int, @Query("fields") fields: String): Observable<Response<BookReviewsListResponse>>

    @GET("book/series/{seriesId}/books")
    fun getBookSeries(@Path("seriesId") seriesId: String, @Query("start") start: Int, @Query("count") count: Int, @Query("fields") fields: String): Observable<Response<BookSeriesListResponse>>
}