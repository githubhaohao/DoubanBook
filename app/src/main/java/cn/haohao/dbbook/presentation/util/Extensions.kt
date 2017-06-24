package cn.haohao.dbbook.presentation.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.widget.Toast
import cn.haohao.dbbook.R

/**
 * Created by HaohaoChang on 2017/6/10.
 */
const val WEICHAT_KEY = "Kdescription"

inline fun <reified T : Activity> Activity.navigate(id: String, sharedView: View? = null,
                                                    transitionName: String? = null) {
    val intent = Intent(this, T::class.java)
    intent.putExtra("id", id)

    var options: ActivityOptionsCompat? = null

    if (sharedView != null && transitionName != null) {
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, transitionName)
    }

    ActivityCompat.startActivity(this, intent, options?.toBundle())
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg, duration).show()
}

fun Context.networkIsConnected(): Boolean {
    val connectivity: ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivity.let {
        val info = connectivity.activeNetworkInfo
        info?.let {
            if (info.isConnected && info.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }

    }
    return false
}

fun Context.share(content: String, uri: Uri?) {
    val shareIntent = Intent()

    if (uri != null) {
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type =  "image/*"
        shareIntent.putExtra("sms_body", content)
    } else {
        shareIntent.type = "text/plain"
    }
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_TEXT, content)
    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    shareIntent.putExtra(WEICHAT_KEY, content)
    this.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_dialog_title)))
}

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

