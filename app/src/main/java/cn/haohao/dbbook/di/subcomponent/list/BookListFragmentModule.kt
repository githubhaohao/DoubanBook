package cn.haohao.dbbook.di.subcomponent.list

import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.domain.interactor.GetBookListInteractor
import cn.haohao.dbbook.presentation.fragment.BookListFragment
import cn.haohao.dbbook.presentation.mapper.BookListMapper
import cn.haohao.dbbook.presentation.presenter.BookListPresenter
import cn.haohao.dbbook.presentation.view.BookListView
import dagger.Module
import dagger.Provides

/**
 * Created by HaohaoChang on 2017/6/12.
 */
@Module
class BookListFragmentModule(val fragment: BookListFragment) {

    @Provides @ActivityScope
    fun provideBookListView(): BookListView = fragment

    @Provides @ActivityScope
    fun provideBookListMapper(): BookListMapper = BookListMapper()

    @Provides @ActivityScope
    fun provideBookListPresenter(bookListView: BookListView,
                                 getBookListInteractor: GetBookListInteractor,
                                 bookListMapper: BookListMapper): BookListPresenter
            = BookListPresenter(bookListView, getBookListInteractor, bookListMapper)
}