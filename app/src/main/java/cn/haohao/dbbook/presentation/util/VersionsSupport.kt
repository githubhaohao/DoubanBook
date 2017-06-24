package cn.haohao.dbbook.presentation.util

import android.os.Build

/**
 * Created by HaohaoChang on 2017/6/10.
 */
fun supportsKitKat(code: () -> Unit) {
    supportsVersion(code, 19)
}

fun supportsLollipop(code: () -> Unit) {
    supportsVersion(code, 21)
}

private fun supportsVersion(code: () -> Unit, sdk: Int) {
    if (Build.VERSION.SDK_INT >= sdk) {
        code.invoke()
    }
}