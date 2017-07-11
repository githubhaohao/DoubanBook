package cn.haohao.dbbook.presentation.activity

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.haohao.dbbook.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import android.support.v7.app.AppCompatDelegate
import android.view.Gravity
import cn.haohao.dbbook.presentation.util.showToast
import java.util.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val aboutPage = AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_avator)
                .setDescription(getString(R.string.description))
                .addGroup("Connect me")
                .addEmail("haohaochang86@gmail.com", "Send email to me")
                .addWebsite("http://haohaochang.cn/")
                .addFacebook("haohao_chang", "Like me on Facebook")
                .addTwitter("haohao_chang", "Like me on Twitter")
                .addGitHub("githubhaohao", "Give me a star")
                .addGroup("Open Source Library")
                .addWebsite("https://github.com/square/okhttp", "OkHttp3 from square")
                .addWebsite("https://github.com/square/retrofit", "Retrofit from square")
                .addWebsite("https://github.com/ReactiveX/RxKotlin", "RxKotlin from ReactiveX")
                .addWebsite("https://github.com/square/dagger", "Dagger from square")
                .addWebsite("https://github.com/bumptech/glide", "Glide from bumptech")
                .addWebsite("https://github.com/andremion/Floating-Navigation-View", "Floating-Navigation-View")
                .addGroup("Thanks")
                .addWebsite("http://stormzhang.com/", "Stormzhang")
                .addWebsite("https://github.com/WuXiaolong", "吴小龙同學")
                .addWebsite("https://www.douban.com/", "豆瓣")
                .addItem(getCopyRightsElement())
                .create()
        setContentView(aboutPage)
        title = "About Page"

    }

    fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR))
        copyRightsElement.title = copyrights
        copyRightsElement.iconDrawable = R.drawable.ic_copy_right
        copyRightsElement.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        copyRightsElement.iconNightTint = android.R.color.white
        copyRightsElement.gravity = Gravity.CENTER
        copyRightsElement.setOnClickListener{
            showToast(copyrights)
        }
        return copyRightsElement
    }

    fun simulateDayNight(currentSetting: Int) {
        val DAY = 0
        val NIGHT = 1
        val FOLLOW_SYSTEM = 3

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
