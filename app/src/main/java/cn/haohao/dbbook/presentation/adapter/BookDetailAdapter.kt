package cn.haohao.dbbook.presentation.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.AppCompatRatingBar
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import cn.haohao.dbbook.presentation.activity.BaseActivity
import cn.haohao.dbbook.presentation.activity.BookCommentActivity
import cn.haohao.dbbook.presentation.util.share
import cn.haohao.dbbook.presentation.util.showToast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.hymane.expandtextview.ExpandTextView
import java.util.*


/**
 * Created by HaohaoChang on 2017/6/15.
 */
class BookDetailAdapter(private val context: Context,
                        private val mBookInfo: BookInfoResponse,
                        private val mReviewsListResponse: BookReviewsListResponse?,
                        private val mSeriesListResponse: BookSeriesListResponse?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 标记信息是否展开
    private var isOpen: Boolean = false

    companion object {
        const val TYPE_BOOK_INFO = 0
        const val TYPE_BOOK_BRIEF = 1
        const val TYPE_BOOK_COMMENT = 2
        const val TYPE_BOOK_RECOMMEND = 3
        const val HEADER_COUNT = 2
        const val PROGRESS_DELAY_MIN_TIME = 500
        const val PROGRESS_DELAY_SIZE_TIME = 1000

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        Log.e("Adapter", "---------------")
        if (holder is BookInfoHolder) {
            holder.ratingBarHots.rating = mBookInfo.rating.average.toFloat() / 2
            holder.tvHotsNum.text = mBookInfo.rating.average
            holder.tvCommentNum.text = "${mBookInfo.rating.numRaters} 人点评"
            holder.tvBookInfo.text = mBookInfo.infoString
            holder.rlMoreInfo.setOnClickListener {
                if (isOpen) {
                    ObjectAnimator.ofFloat(holder.ivMoreInfo, "rotation", 90F, 0F).start()
                    holder.progressBar.visibility = View.GONE
                    holder.llPublishInfo.visibility = View.GONE
                    isOpen = false
                } else {
                    ObjectAnimator.ofFloat(holder.ivMoreInfo, "rotation", 0F, 90F).start()
                    holder.progressBar.visibility = View.VISIBLE
                    object : Handler() {
                        override fun handleMessage(msg: Message?) {
                            super.handleMessage(msg)
                            if (isOpen) {
                                holder.progressBar.visibility = View.GONE
                                holder.llPublishInfo.visibility = View.VISIBLE
                            }
                        }
                    }.sendEmptyMessageDelayed(0, getDelayTime().toLong())
                    isOpen = true
                }

            }

            if (mBookInfo.author.isNotEmpty()) {
                holder.tvAuthor.text = "作者：${mBookInfo.author[0]}"
            }

            holder.tvPublisher.text = "出版社：${mBookInfo.publisher}"

            if (mBookInfo.subtitle.isEmpty()) {
                holder.tvSubtitle.visibility = View.GONE
            }
            holder.tvSubtitle.text = "副标题：${mBookInfo.subtitle}"
            if (mBookInfo.origin_title.isEmpty()) {
                holder.tvOriginTitle.visibility = View.GONE
            }
            holder.tvOriginTitle.text = "原作名：${mBookInfo.origin_title}"

            if (mBookInfo.translator.isNotEmpty()) {
                holder.tvTranslator.text = "译者：${mBookInfo.translator[0]}"
            } else {
                holder.tvTranslator.visibility = View.GONE
            }

            holder.tvPublishDate.text = "出版年：${mBookInfo.pubdate}"
            holder.tvPages.text = "页数：${mBookInfo.pages}"
            holder.tvPrice.text = "定价：${mBookInfo.price}"
            holder.tvBinding.text = "装帧：${mBookInfo.binding}"
            holder.tvIsbn.text = "ISBN：${mBookInfo.isbn13}"
        } else if (holder is BookBriefHolder) {
            if (mBookInfo.summary.isNotEmpty()) {
                holder.etvBrief.content = mBookInfo.summary
            } else {
                holder.etvBrief.content = "暂无简介"
            }
        } else if (holder is BookCommentHolder) {

            if (mReviewsListResponse != null) {
                val reviews = mReviewsListResponse.reviews
                if (position == HEADER_COUNT) {
                    holder.tvCommentTitle.visibility = View.VISIBLE
                } else if (position == reviews.size + 1) {
                    holder.tvMoreComment.visibility = View.VISIBLE
                    holder.tvMoreComment.text = "更多评论${mReviewsListResponse.total}条"
                    holder.tvMoreComment.setOnClickListener {
                        val intent = Intent(BaseActivity.instance, BookCommentActivity::class.java)
                        intent.putExtra("book_id", mBookInfo.id)
                        intent.putExtra("book_name", mBookInfo.title)
                        BaseActivity.instance?.startActivity(intent)
                    }
                }

                Glide.with(context)
                        .load(reviews[position - HEADER_COUNT].author.avatar)
                        .asBitmap()
                        .centerCrop()
                        .into(object : BitmapImageViewTarget(holder.ivAvatar){
                            override fun setResource(resource: Bitmap?) {
                                val circularBitmapDrawable =
                                     RoundedBitmapDrawableFactory.create(context.resources, resource)
                                circularBitmapDrawable.isCircular = true
                                holder.ivAvatar.setImageDrawable(circularBitmapDrawable)
                            }
                        })
                if (reviews[position - HEADER_COUNT] == null) {
                    return
                }
                val bookReviewRes = reviews[position - HEADER_COUNT]
                holder.tvUserName.text = bookReviewRes.author.name
                bookReviewRes.rating?.let {
                    holder.ratingBarHots.rating = bookReviewRes.rating.value.toFloat()
                }
                holder.tvCommentContent.text = bookReviewRes.summary
                holder.tvFavoriteNum.text = bookReviewRes.votes.toString()
                holder.tvUpdateTime.text = bookReviewRes.updated.split(" ")[0]

            } else {
                holder.itemView.visibility = View.GONE
            }

        } else if (holder is BookSeriesHolder && mSeriesListResponse != null ) {
            val cells = mSeriesListResponse.books
            if (cells.isEmpty()) {
                holder.itemView.visibility = View.GONE
            } else {
                holder.llSeriesContent.removeAllViews()
                cells.forEach {
                    holder.llSeriesContent.addView(BookCellHolder(it).mContentView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var itemView: View
        return when (viewType) {
            TYPE_BOOK_INFO -> {
                itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_book_info, parent, false)
                return BookInfoHolder(itemView)
            }
            TYPE_BOOK_BRIEF -> {
                itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_book_brief, parent, false)
                return BookBriefHolder(itemView)
            }

            TYPE_BOOK_COMMENT -> {
                itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_book_comment, parent, false)
                return BookCommentHolder(itemView)
            }

            TYPE_BOOK_RECOMMEND -> {
                itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_book_series, parent, false)
                return BookSeriesHolder(itemView)
            }
            else -> null
        }
    }

    override fun getItemCount(): Int {
        var count = HEADER_COUNT
        mReviewsListResponse?.let {
            if (mReviewsListResponse.reviews.isNotEmpty()) count += mReviewsListResponse.reviews.size
        }

        mSeriesListResponse?.let {
            if (!mSeriesListResponse.books.isEmpty()) {
                count += 1
            }
        }

        return count
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> TYPE_BOOK_INFO
        1 -> TYPE_BOOK_BRIEF
        in 2 until if(mReviewsListResponse == null)
                       HEADER_COUNT
                    else mReviewsListResponse.reviews.size + HEADER_COUNT -> TYPE_BOOK_COMMENT
        else -> TYPE_BOOK_RECOMMEND
    }

    fun getDelayTime(): Int
            = Random().nextInt(PROGRESS_DELAY_SIZE_TIME) + PROGRESS_DELAY_MIN_TIME

    class BookInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingBarHots: AppCompatRatingBar by lazy { itemView.findViewById(R.id.ratingBar_hots) as AppCompatRatingBar }
        val tvHotsNum: TextView by lazy { itemView.findViewById(R.id.tv_hots_num) as TextView }
        val tvCommentNum: TextView  by lazy { itemView.findViewById(R.id.tv_comment_num) as TextView }
        val tvBookInfo: TextView  by lazy { itemView.findViewById(R.id.tv_book_info) as TextView }
        val ivMoreInfo: ImageView  by lazy { itemView.findViewById(R.id.iv_more_info) as ImageView }
        val rlMoreInfo: RelativeLayout by lazy { itemView.findViewById(R.id.rl_more_info) as RelativeLayout }
        val tvAuthor: TextView  by lazy { itemView.findViewById(R.id.tv_author) as TextView }
        val tvPublisher: TextView  by lazy { itemView.findViewById(R.id.tv_publisher) as TextView }
        val tvSubtitle: TextView  by lazy { itemView.findViewById(R.id.tv_subtitle) as TextView }
        val tvOriginTitle: TextView  by lazy { itemView.findViewById(R.id.tv_origin_title) as TextView }
        val tvTranslator: TextView  by lazy { itemView.findViewById(R.id.tv_translator) as TextView }
        val tvPublishDate: TextView  by lazy { itemView.findViewById(R.id.tv_publish_date) as TextView }
        val tvPages: TextView  by lazy { itemView.findViewById(R.id.tv_pages) as TextView }
        val tvPrice: TextView  by lazy { itemView.findViewById(R.id.tv_price) as TextView }
        val tvBinding: TextView  by lazy { itemView.findViewById(R.id.tv_binding) as TextView }
        val tvIsbn: TextView  by lazy { itemView.findViewById(R.id.tv_isbn) as TextView }
        val llPublishInfo: LinearLayout  by lazy { itemView.findViewById(R.id.ll_publish_info) as LinearLayout }
        val progressBar: ProgressBar  by lazy { itemView.findViewById(R.id.progressBar) as ProgressBar }
    }

    class BookBriefHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etvBrief: ExpandTextView by lazy { itemView.findViewById(R.id.etv_brief) as ExpandTextView }
    }

    class BookCommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCommentTitle: TextView by lazy { itemView.findViewById(R.id.tv_comment_title) as TextView }
        val ivAvatar: ImageView by lazy { itemView.findViewById(R.id.iv_avatar) as ImageView }
        val tvUserName: TextView by lazy { itemView.findViewById(R.id.tv_user_name) as TextView }
        val ratingBarHots: AppCompatRatingBar by lazy { itemView.findViewById(R.id.rating_hots) as AppCompatRatingBar }
        val tvCommentContent: TextView by lazy { itemView.findViewById(R.id.tv_comment_content) as TextView }
        val ivFavorite: ImageView by lazy { itemView.findViewById(R.id.iv_favorite) as ImageView }
        val tvFavoriteNum: TextView by lazy { itemView.findViewById(R.id.tv_favorite_num) as TextView }
        val tvUpdateTime: TextView by lazy { itemView.findViewById(R.id.tv_update_time) as TextView }
        val llComment: LinearLayout by lazy { itemView.findViewById(R.id.ll_comment) as LinearLayout }
        val tvMoreComment: TextView by lazy { itemView.findViewById(R.id.tv_more_comment) as TextView }
    }

    class BookSeriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCommentTitle: TextView by lazy { itemView.findViewById(R.id.tv_series_comment_title) as TextView }
        val llSeriesContent: LinearLayout by lazy { itemView.findViewById(R.id.ll_series_content) as LinearLayout }
        val hsvSeries: HorizontalScrollView by lazy { itemView.findViewById(R.id.hsv_series) as HorizontalScrollView }
    }


}