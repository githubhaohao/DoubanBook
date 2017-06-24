package cn.haohao.dbbook.di

import android.content.Context
import cn.haohao.dbbook.App
import cn.haohao.dbbook.di.qualifier.ApplicationQualifier
import cn.haohao.dbbook.domain.executor.JobExecutor
import cn.haohao.dbbook.domain.executor.MainThread
import cn.haohao.dbbook.domain.executor.PostExecutionThread
import cn.haohao.dbbook.domain.executor.ThreadExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by HaohaoChang on 2017/6/10.
 */
@Module
class ApplicationModule(private val app: App) {

    @Provides @Singleton
    fun provideApplication(): App = app

    @Provides @Singleton @ApplicationQualifier
    fun provideApplicationContext(): Context = app

    @Provides @Singleton
    fun provideThreadExecutor(): ThreadExecutor =
            JobExecutor()

    @Provides @Singleton
    fun providePostExeThread(): PostExecutionThread =
            MainThread()

}