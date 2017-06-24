package cn.haohao.dbbook.presentation.adapter

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.presentation.activity.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.item_book_comment.view.*

/**
 * Created by HaohaoChang on 2017/6/22.
 */
class BookCommentAdapter(private var reviewResponse: BookReviewsListResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_EMPTY = 0
        const val TYPE_DEFAULT = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is BookCommentViewHolder) {
            val reviews = reviewResponse.reviews
            if (reviews.isEmpty()) {
                holder.itemView.visibility = View.GONE
                return
            }
            val review = reviews[position]
            Glide.with(BaseActivity.instance)
                    .load(review.author.avatar)
                    .asBitmap()
                    .centerCrop()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            val drawable = RoundedBitmapDrawableFactory.create(BaseActivity.instance?.resources, resource)
                            drawable?.let {
                                drawable.isCircular = true
                                holder.itemView.iv_avatar.setImageDrawable(drawable)
                            }
                        }

                    })
            holder.itemView.tv_user_name.text = review.author.name
            review.rating?.let {
                holder.itemView.rating_hots.rating = review.rating.value.toFloat()
                holder.itemView.tv_comment_content.text = review.summary
                holder.itemView.tv_favorite_num.text = review.votes.toString()
                holder.itemView.tv_update_time.text = review.updated.split(" ")[0]
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        TYPE_EMPTY -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            EmptyViewHolder(itemView)
        }
        else -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book_comment, parent, false)
            BookCommentViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        var count = 1
        if (reviewResponse != null && reviewResponse.reviews.isNotEmpty()) {
            count = reviewResponse.reviews.size
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        if (reviewResponse == null || reviewResponse.reviews.isEmpty()) {
            return TYPE_EMPTY
        } else {
            return TYPE_DEFAULT
        }
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class BookCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}