package cn.haohao.dbbook.domain.interactor

import cn.haohao.dbbook.data.BookService
import cn.haohao.dbbook.data.datasource.BookDataSource
import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import cn.haohao.dbbook.domain.entity.RequestSeriesParams
import cn.haohao.dbbook.domain.executor.PostExecutionThread
import cn.haohao.dbbook.domain.executor.ThreadExecutor
import retrofit2.Response
import rx.Observable

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class GetRecommendedBooksInteractor(val bookDataSource: BookDataSource,
                                    threadExecutor: ThreadExecutor,
                                    postExecutionThread: PostExecutionThread) :
        Interactor<Response<BookSeriesListResponse>, RequestSeriesParams>(threadExecutor, postExecutionThread) {
    override fun buildObservable(params: RequestSeriesParams): Observable<Response<BookSeriesListResponse>> =
            bookDataSource.requestRecommendedBooks(
                    params.seriesId,
                    params.start,
                    params.count,
                    params.fields
            )
}