package cn.haohao.dbbook.domain.interactor

import cn.haohao.dbbook.data.datasource.BookDataSource
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.domain.entity.RequestDetailParams
import cn.haohao.dbbook.domain.executor.PostExecutionThread
import cn.haohao.dbbook.domain.executor.ThreadExecutor
import retrofit2.Response
import rx.Observable

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class GetBookDetailInteractor(val bookDataSource: BookDataSource,
                              threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread) :
        Interactor<Response<BookReviewsListResponse>, RequestDetailParams>(threadExecutor, postExecutionThread) {
    override fun buildObservable(params: RequestDetailParams): Observable<Response<BookReviewsListResponse>> =
            bookDataSource.requestBookDetail(
                    params.bookId,
                    params.start,
                    params.count,
                    params.fields
            )
}