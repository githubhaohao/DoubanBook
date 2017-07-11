package cn.haohao.dbbook

import android.app.Application
import cn.haohao.dbbook.di.ApplicationComponent
import cn.haohao.dbbook.di.ApplicationModule
import cn.haohao.dbbook.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary

/**
 * Created by HaohaoChang on 2017/6/9.
 */
class App : Application() {

    companion object {
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        //LeakCanary.install(this)
    }

    fun initDagger() {
        graph = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }


}