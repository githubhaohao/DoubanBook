package cn.haohao.dbbook.di

import android.content.Context
import android.support.v7.app.AppCompatActivity
import cn.haohao.dbbook.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by HaohaoChang on 2017/6/12.
 */
@Module
abstract class ActivityModule(protected val activity: AppCompatActivity) {

    @Provides @ActivityScope
    fun provideActivity(): AppCompatActivity = activity

    @Provides @ActivityScope
    fun provideActivityContext(): Context = activity.baseContext

}