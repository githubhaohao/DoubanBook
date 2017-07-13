package cn.haohao.dbbook.presentation.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.graphics.Palette
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.data.entity.http.BookSeriesListResponse
import cn.haohao.dbbook.di.ApplicationComponent
import cn.haohao.dbbook.di.subcomponent.detail.BookDetailActivityModule
import cn.haohao.dbbook.domain.entity.RequestDetailParams
import cn.haohao.dbbook.domain.entity.RequestSeriesParams
import cn.haohao.dbbook.presentation.adapter.BookDetailAdapter
import cn.haohao.dbbook.presentation.presenter.BookDetailPresenter
import cn.haohao.dbbook.presentation.presenter.RecommendedBooksPresenter
import cn.haohao.dbbook.presentation.util.Blur
import cn.haohao.dbbook.presentation.util.share
import cn.haohao.dbbook.presentation.util.showToast
import cn.haohao.dbbook.presentation.util.supportsLollipop
import cn.haohao.dbbook.presentation.view.BookDetailView
import cn.haohao.dbbook.presentation.view.RecommendedBooksView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.activity_book_detail.*
import org.jetbrains.anko.backgroundColor
import javax.inject.Inject


class BookDetailActivity : BaseActivity(), BookDetailView, RecommendedBooksView, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "BookDetailActivity"
        const val COMMENT_FIELDS = "id,rating,author,title,updated,comments,summary,votes,useless"
        const val SERIES_FIELDS = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,series"
        const val REVIEWS_COUNT = 5
        const val SERIES_COUNT = 6
        const val PAGE = 0
    }

    private lateinit var mBookInfoResponse: BookInfoResponse
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mBookDetailAdapter: BookDetailAdapter
    private lateinit var mReviewsListResponse: BookReviewsListResponse
    private lateinit var mSeriesListResponse: BookSeriesListResponse

    @Inject
    lateinit var mBookDetailPresenter: BookDetailPresenter

    @Inject
    lateinit var mRecommendedBooksPresenter: RecommendedBooksPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        injectToThis()
        setUpToolBar()
        initEvents()

    }

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        super.injectDependencies(applicationComponent)
        applicationComponent.plus(BookDetailActivityModule(this))
                .injectTo(this)

    }

    override fun onRefresh() {
        swipeRefreshLayout.postDelayed({
            swipeRefreshLayout.isRefreshing = false
        }, 2000)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_book_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                android.R.id.home -> {
                    supportFinishAfterTransition()
                    true
                }
                R.id.action_share -> {
                    val sb = StringBuilder(getString(R.string.your_friend))
                    sb.append("给你分享了一本书，名叫《")
                            .append(mBookInfoResponse.title)
                            .append("》，快来看看吧")
                    share(sb.toString(), null)
                    true
                }
                else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        mBookDetailPresenter.cancel()
        mRecommendedBooksPresenter.cancel()
        super.onDestroy()
    }

    //BookDetailView

    override fun onError(error: String) {
        showToast(error)
        Log.e(TAG, error)
    }

    override fun showDetailData(data: Any) {
        if (data is BookReviewsListResponse) {
            mReviewsListResponse.total = data.total
            mReviewsListResponse.reviews.addAll(data.reviews)
            mBookDetailAdapter.notifyDataSetChanged()
            mBookInfoResponse.series?.let {
                mRecommendedBooksPresenter.execute(RequestSeriesParams(mBookInfoResponse.series.id, PAGE * SERIES_COUNT, 6, SERIES_FIELDS))
            }
        }
    }

    override fun showProgressView() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgressView() {
        swipeRefreshLayout.isRefreshing = false
    }

    //RecommendedBooksView

    override fun onGetRecommendedBooksError(error: String) {
        showToast(error)
        Log.e(TAG, error)
    }

    override fun showRecommendedBooks(recommendedBooks: BookSeriesListResponse) {
        mSeriesListResponse.total = recommendedBooks.total
        mSeriesListResponse.books.addAll(recommendedBooks.books)
        mBookDetailAdapter.notifyDataSetChanged()

    }

    fun setUpToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = null
    }

    fun initEvents() {
        makeStatusBarTransparent()

        mSeriesListResponse = BookSeriesListResponse()
        mReviewsListResponse = BookReviewsListResponse()
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL


        mBookInfoResponse = intent.getSerializableExtra(BookInfoResponse.serialVersionName) as BookInfoResponse
        collapsingToolbarLayout.title = mBookInfoResponse.title
        swipeRefreshLayout.setOnRefreshListener(this)

        mBookDetailAdapter = BookDetailAdapter(this, mBookInfoResponse, mReviewsListResponse, mSeriesListResponse)

        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = mBookDetailAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()

        setUpTransition()

        val coverImgBitmap = intent.getParcelableExtra<Bitmap>("book_img")
        coverImgBitmap?.let {
            iv_book_img.setImageBitmap(coverImgBitmap)
            iv_book_bg.setImageBitmap(Blur.apply(coverImgBitmap))
            iv_book_bg.alpha = 0.6F
            Palette.from(coverImgBitmap).generate { palette ->
                val darkVibrantColor = palette.getDarkVibrantColor(R.attr.colorPrimary)
                collapsingToolbarLayout.setContentScrimColor(darkVibrantColor)
                collapsingToolbarLayout.setStatusBarScrimColor(darkVibrantColor)
            }
        }

        coverImgBitmap?:let {
            Glide.with(this)
                    .load(mBookInfoResponse.images.large)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>?) {
                            iv_book_img.setImageBitmap(resource)
                            iv_book_bg.setImageBitmap(Blur.apply(resource))
                            iv_book_bg.alpha = 0.6F
                        }
                    })
        }

        mBookDetailPresenter.execute(RequestDetailParams(mBookInfoResponse.id, PAGE * REVIEWS_COUNT, REVIEWS_COUNT, COMMENT_FIELDS))

    }

    @SuppressLint("NewApi")
    fun setUpTransition() {
        //supportPostponeEnterTransition()
        supportsLollipop {
            iv_book_img.transitionName = BOOK_DETAIL_TRANSITION_NAME
        }
    }

    private fun makeStatusBarTransparent() {
        supportsLollipop {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
