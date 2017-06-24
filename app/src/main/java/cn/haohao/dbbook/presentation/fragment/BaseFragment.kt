package cn.haohao.dbbook.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.haohao.dbbook.App
import cn.haohao.dbbook.di.ApplicationComponent

/**
 * Created by HaohaoChang on 2017/6/12.
 */
abstract class BaseFragment : Fragment() {
    lateinit var rootView: View

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies(App.graph)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initRootView(inflater, container, savedInstanceState)
        initEvents()
        initData(savedInstanceState == null)
        return rootView
    }

    abstract fun initRootView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)

    abstract fun initEvents()

    abstract fun initData(isSavedNull: Boolean)

    abstract fun injectDependencies(applicationComponent: ApplicationComponent)


}