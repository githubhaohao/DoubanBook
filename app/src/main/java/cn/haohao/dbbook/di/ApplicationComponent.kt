package cn.haohao.dbbook.di

import cn.haohao.dbbook.di.subcomponent.comment.BookCommentActivityComponent
import cn.haohao.dbbook.di.subcomponent.comment.BookCommentActivityModule
import cn.haohao.dbbook.di.subcomponent.detail.BookDetailActivityComponent
import cn.haohao.dbbook.di.subcomponent.detail.BookDetailActivityModule
import cn.haohao.dbbook.di.subcomponent.list.BookListFragmentComponent
import cn.haohao.dbbook.di.subcomponent.list.BookListFragmentModule
import cn.haohao.dbbook.di.subcomponent.list.SearchActivityComponent
import cn.haohao.dbbook.di.subcomponent.list.SearchActivityModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by HaohaoChang on 2017/6/9.
 */
@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        DataModule::class,
        DomainModule::class
))
interface ApplicationComponent {
    fun plus(module: BookListFragmentModule): BookListFragmentComponent
    fun plus(module: BookDetailActivityModule): BookDetailActivityComponent
    fun plus(module: BookCommentActivityModule): BookCommentActivityComponent
    fun plus(module: SearchActivityModule): SearchActivityComponent
}