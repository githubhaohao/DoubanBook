package cn.haohao.dbbook.di.subcomponent.list

import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.presentation.activity.SearchActivity
import dagger.Subcomponent

/**
 * Created by HaohaoChang on 2017/6/23.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(SearchActivityModule::class))
interface SearchActivityComponent {
    fun injectTo(activity: SearchActivity)
}