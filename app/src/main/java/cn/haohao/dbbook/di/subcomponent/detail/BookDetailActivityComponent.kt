package cn.haohao.dbbook.di.subcomponent.detail

import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.presentation.activity.BookDetailActivity
import dagger.Subcomponent

/**
 * Created by HaohaoChang on 2017/6/16.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(BookDetailActivityModule::class))
interface BookDetailActivityComponent {
    fun injectTo(activity: BookDetailActivity)
}