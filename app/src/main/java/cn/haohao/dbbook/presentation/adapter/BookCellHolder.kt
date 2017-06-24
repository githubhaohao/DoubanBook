package cn.haohao.dbbook.presentation.adapter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.AppCompatRatingBar
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import cn.haohao.dbbook.presentation.activity.BaseActivity
import cn.haohao.dbbook.presentation.activity.BookDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable

/**
 * Created by HaohaoChang on 2017/6/22.
 */
class BookCellHolder(val mBookInfoResponse: BookInfoResponse) {
    val mContentView: View by lazy { LayoutInflater.from(BaseActivity.instance).inflate(R.layout.item_book_series_cell, null, false) }
    private var iv_book_img: ImageView
    private var tv_title: TextView
    private var ratingBar_hots: AppCompatRatingBar
    private var tv_hots_num: TextView

    init {
        iv_book_img = mContentView.findViewById(R.id.iv_book_img) as ImageView
        tv_title = mContentView.findViewById(R.id.tv_title) as TextView
        ratingBar_hots = mContentView.findViewById(R.id.ratingBar_hots) as AppCompatRatingBar
        tv_hots_num = mContentView.findViewById(R.id.tv_hots_num) as TextView

        initEvent()
    }

    private fun initEvent() {
        Glide.with(BaseActivity.instance)
                .load(mBookInfoResponse.images.large)
                .into(iv_book_img)
        tv_title.text = mBookInfoResponse.title
        ratingBar_hots.rating = mBookInfoResponse.rating.average.toFloat() / 2
        tv_hots_num.text = mBookInfoResponse.rating.average
        mContentView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(BookInfoResponse.serialVersionName, mBookInfoResponse)
            val bitmap = (iv_book_img.drawable as GlideBitmapDrawable).bitmap
            bundle.putParcelable("book_img", bitmap)
            val intent = Intent(BaseActivity.instance, BookDetailActivity::class.java)
            intent.putExtras(bundle)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.instance, iv_book_img, BaseActivity.BOOK_DETAIL_TRANSITION_NAME)
                BaseActivity.instance?.startActivity(intent, options.toBundle())
            } else {
                BaseActivity.instance?.startActivity(intent)
            }
        }
    }
}