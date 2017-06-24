package cn.haohao.dbbook.di.subcomponent.comment

import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.domain.interactor.GetBookDetailInteractor
import cn.haohao.dbbook.presentation.activity.BookCommentActivity
import cn.haohao.dbbook.presentation.presenter.BookDetailPresenter
import cn.haohao.dbbook.presentation.view.BookDetailView
import dagger.Module
import dagger.Provides

/**
 * Created by HaohaoChang on 2017/6/22.
 */
@Module
class BookCommentActivityModule(val activity: BookCommentActivity) {
    @Provides @ActivityScope
    fun provideBookDetailView(): BookDetailView = activity

    @Provides @ActivityScope
    fun provideBookDetailPresenter(view: BookDetailView,
                                   getBookDetailInteractor: GetBookDetailInteractor): BookDetailPresenter
            = BookDetailPresenter(view, getBookDetailInteractor)



}