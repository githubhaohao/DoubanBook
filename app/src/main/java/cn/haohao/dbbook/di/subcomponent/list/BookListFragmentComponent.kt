package cn.haohao.dbbook.di.subcomponent.list

import cn.haohao.dbbook.di.scope.ActivityScope
import cn.haohao.dbbook.presentation.fragment.BookListFragment
import dagger.Subcomponent

/**
 * Created by HaohaoChang on 2017/6/12.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(BookListFragmentModule::class))
interface BookListFragmentComponent {
    fun injectTo(fragment: BookListFragment)
}