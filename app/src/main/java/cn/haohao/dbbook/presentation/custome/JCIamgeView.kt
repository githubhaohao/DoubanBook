package cn.haohao.dbbook.presentation.custome

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by HaohaoChang on 2017/6/12.
 */
class JCIamgeView : ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        setMeasuredDimension(width, width)

    }
}