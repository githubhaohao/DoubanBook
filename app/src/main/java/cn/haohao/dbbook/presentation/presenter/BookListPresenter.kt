package cn.haohao.dbbook.presentation.presenter

import android.util.Log
import cn.haohao.dbbook.data.entity.http.BookListResponse
import cn.haohao.dbbook.domain.entity.RequestListParams
import cn.haohao.dbbook.domain.interactor.GetBookListInteractor
import cn.haohao.dbbook.presentation.mapper.BookListMapper
import cn.haohao.dbbook.presentation.view.BookListView
import retrofit2.Response
import rx.Subscriber
import java.net.UnknownHostException

/**
 * Created by HaohaoChang on 2017/6/12.
 */
class BookListPresenter(override val view: BookListView,
                        val bookListInteractor: GetBookListInteractor,
                        val bookListMapper: BookListMapper) : Presenter<BookListView, RequestListParams> {

    override fun execute(params: RequestListParams) {
        view.showProgressView()
        bookListInteractor.execute(object : Subscriber<Response<BookListResponse>>() {
            override fun onCompleted() {
            }

            override fun onNext(t: Response<BookListResponse>) {
                val index = t.body().start
                if (index == 0) {
                    view.refreshData(bookListMapper.transform(t.body()))
                } else {
                    view.addData(bookListMapper.transform(t.body()))
                }
                view.hideProgressView()

            }

            override fun onError(e: Throwable?) {
                e?.let {
                    view.onError(e.localizedMessage)
                }
                view.hideProgressView()
            }

        },params)

    }

    override fun cancel() {
        bookListInteractor.cancel()
    }
}