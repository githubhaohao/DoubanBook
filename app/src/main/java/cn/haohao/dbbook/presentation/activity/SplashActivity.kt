package cn.haohao.dbbook.presentation.activity

import android.content.Intent
import cn.haohao.dbbook.R
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash

class SplashActivity : AwesomeSplash() {

    override fun initSplash(configSplash: ConfigSplash) {
        configSplash.backgroundColor = R.color.colorPrimary
        configSplash.animCircularRevealDuration = 2000
        configSplash.revealFlagX = Flags.REVEAL_RIGHT
        configSplash.revealFlagY = Flags.REVEAL_BOTTOM

        configSplash.logoSplash = R.drawable.ic_app_splash
        configSplash.animLogoSplashDuration = 2000
        configSplash.animLogoSplashTechnique = Techniques.Bounce

        configSplash.pathSplash = ""
        configSplash.logoSplash = R.drawable.ic_app_splash
        configSplash.animPathStrokeDrawingDuration = 3000
        configSplash.pathSplashStrokeSize = 3
        configSplash.pathSplashStrokeColor = R.color.colorAccent
        configSplash.animPathFillingDuration = 3000
        configSplash.pathSplashFillColor = R.color.colorAccent

        configSplash.titleSplash = getString(R.string.app_name)
        configSplash.titleTextColor = R.color.white
        configSplash.titleTextSize = 30f
        configSplash.animTitleDuration = 3000
        configSplash.animTitleTechnique = Techniques.FadeInDown
        //configSplash.setTitleFont("fonts/myfont.ttf")
    }

    override fun animationsFinished() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
