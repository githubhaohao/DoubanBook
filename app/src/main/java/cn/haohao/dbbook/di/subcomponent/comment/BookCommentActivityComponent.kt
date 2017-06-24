package cn.haohao.dbbook.di.subcomponent.comment

import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.presentation.activity.BookCommentActivity
import dagger.Subcomponent

/**
 * Created by HaohaoChang on 2017/6/22.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(BookCommentActivityModule::class))
interface BookCommentActivityComponent {
    fun injectTo(activity: BookCommentActivity)
}