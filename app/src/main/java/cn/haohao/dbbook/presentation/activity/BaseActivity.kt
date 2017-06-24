package cn.haohao.dbbook.presentation.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.haohao.dbbook.App
import cn.haohao.dbbook.di.ApplicationComponent

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        var instance: BaseActivity? = null
        const val BOOK_DETAIL_TRANSITION_NAME = "book_cover_img"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //injectDependencies(App.graph)
    }

    override fun onResume() {
        super.onResume()
        instance = this
    }

    override fun onPause() {
        super.onPause()
        instance = null
    }

    fun injectToThis() {
        injectDependencies(App.graph)
    }

    open fun injectDependencies(applicationComponent: ApplicationComponent) {

    }

}
