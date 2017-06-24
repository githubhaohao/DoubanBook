package cn.haohao.dbbook.presentation.presenter

import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import cn.haohao.dbbook.domain.entity.RequestSeriesParams
import cn.haohao.dbbook.domain.interactor.GetRecommendedBooksInteractor
import cn.haohao.dbbook.presentation.view.RecommendedBooksView
import retrofit2.Response
import rx.Subscriber

/**
 * Created by HaohaoChang on 2017/6/15.
 */
class RecommendedBooksPresenter(override val view: RecommendedBooksView,
                                val getRecommendedBooksInteractor: GetRecommendedBooksInteractor) : Presenter<RecommendedBooksView, RequestSeriesParams> {
    override fun execute(params: RequestSeriesParams) {
        getRecommendedBooksInteractor.execute(object : Subscriber<Response<BookSeriesListResponse>>(){
            override fun onNext(t: Response<BookSeriesListResponse>?) {
                t?.let {
                    if (t.isSuccessful) {
                        view.showRecommendedBooks(t.body())
                    }
                }
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    view.onGetRecommendedBooksError(e.localizedMessage)
                }
            }

            override fun onCompleted() {
            }

        }, params)

    }

    override fun cancel() {
        getRecommendedBooksInteractor.cancel()
    }
}