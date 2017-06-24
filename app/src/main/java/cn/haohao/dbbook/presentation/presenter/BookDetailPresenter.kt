package cn.haohao.dbbook.presentation.presenter

import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.domain.entity.RequestDetailParams
import cn.haohao.dbbook.domain.interactor.GetBookDetailInteractor
import cn.haohao.dbbook.presentation.view.BookDetailView
import org.jetbrains.anko.getStackTraceString
import retrofit2.Response
import rx.Subscriber

/**
 * Created by HaohaoChang on 2017/6/15.
 */
class BookDetailPresenter(override val view: BookDetailView,
                          val bookDetailInteractor: GetBookDetailInteractor) : Presenter<BookDetailView, RequestDetailParams> {
    override fun execute(params: RequestDetailParams) {
        view.showProgressView()
        bookDetailInteractor.execute(object : Subscriber<Response<BookReviewsListResponse>>(){
            override fun onNext(t: Response<BookReviewsListResponse>?) {
                t?.let {
                    if (t.isSuccessful) {
                        view.showDetailData(t.body())
                    }
                }
                view.hideProgressView()

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.let {
                    view.onError(e.getStackTraceString())
                }
                view.hideProgressView()

            }

        }, params)

    }

    override fun cancel() {
        bookDetailInteractor.cancel()
    }
}