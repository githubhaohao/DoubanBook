package cn.haohao.dbbook.di.subcomponent.detail

import cn.haohao.dbbook.di.ActivityModule
import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.domain.interactor.GetBookDetailInteractor
import cn.haohao.dbbook.domain.interactor.GetRecommendedBooksInteractor
import cn.haohao.dbbook.presentation.activity.BookDetailActivity
import cn.haohao.dbbook.presentation.presenter.BookDetailPresenter
import cn.haohao.dbbook.presentation.presenter.RecommendedBooksPresenter
import cn.haohao.dbbook.presentation.view.BookDetailView
import cn.haohao.dbbook.presentation.view.RecommendedBooksView
import dagger.Module
import dagger.Provides

/**
 * Created by HaohaoChang on 2017/6/16.
 */
@Module
class BookDetailActivityModule(val activity: BookDetailActivity) {

    @Provides @ActivityScope
    fun provideBookDetailView(): BookDetailView = activity

    @Provides @ActivityScope
    fun provideRecommendedBooksView(): RecommendedBooksView = activity

    @Provides @ActivityScope
    fun provideBookDetailPresenter(view: BookDetailView,
                                   getBookDetailInteractor: GetBookDetailInteractor): BookDetailPresenter
            = BookDetailPresenter(view, getBookDetailInteractor)

    @Provides @ActivityScope
    fun provideRecommendedBooksPresenter(view: RecommendedBooksView,
                                    getRecommendedBooksInteractor: GetRecommendedBooksInteractor): RecommendedBooksPresenter
            = RecommendedBooksPresenter(view, getRecommendedBooksInteractor)
}