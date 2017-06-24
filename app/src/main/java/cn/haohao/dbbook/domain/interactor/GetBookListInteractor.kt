package cn.haohao.dbbook.domain.interactor

import cn.haohao.dbbook.data.datasource.BookDataSource
import cn.haohao.dbbook.data.entity.http.BookListResponse
import cn.haohao.dbbook.domain.entity.RequestListParams
import cn.haohao.dbbook.domain.executor.PostExecutionThread
import cn.haohao.dbbook.domain.executor.ThreadExecutor
import retrofit2.Response
import rx.Observable

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class GetBookListInteractor(val bookDataSource: BookDataSource,
                            threadExecutor: ThreadExecutor,
                            postExecutionThread: PostExecutionThread) :
        Interactor<Response<BookListResponse>, RequestListParams>(threadExecutor, postExecutionThread) {

    override fun buildObservable(params: RequestListParams): Observable<Response<BookListResponse>> =
            bookDataSource.requestBookList(
                    params.queryKey,
                    params.tag,
                    params.start,
                    params.count,
                    params.fields
            )
}