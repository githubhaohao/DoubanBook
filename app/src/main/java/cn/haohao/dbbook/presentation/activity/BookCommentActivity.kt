package cn.haohao.dbbook.presentation.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookReviewResponse
import cn.haohao.dbbook.data.entity.http.BookReviewsListResponse
import cn.haohao.dbbook.di.ApplicationComponent
import cn.haohao.dbbook.di.subcomponent.comment.BookCommentActivityModule
import cn.haohao.dbbook.domain.entity.RequestDetailParams
import cn.haohao.dbbook.presentation.adapter.BookCommentAdapter
import cn.haohao.dbbook.presentation.presenter.BookDetailPresenter
import cn.haohao.dbbook.presentation.util.showToast
import cn.haohao.dbbook.presentation.view.BookDetailView
import kotlinx.android.synthetic.main.activity_book_comments_layout.*
import java.util.ArrayList
import javax.inject.Inject

class BookCommentActivity : BaseActivity(), BookDetailView, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val COMMENT_FIELDS = "id,rating,author,title,updated,comments,summary,votes,useless"
        const val TAG = "BookCommentActivity"
    }
    private var count = 20
    private var page = 0
    private lateinit var bookId: String
    private lateinit var bookName: String
    private lateinit var mBookCommentAdapter: BookCommentAdapter
    private lateinit var mBookReviewsListRes: BookReviewsListResponse
    private var isLoadAll = false

    @Inject
    lateinit var mBookDetailPresenter: BookDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_comments_layout)
        injectToThis()
        initEvent()
    }

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        super.injectDependencies(applicationComponent)
        applicationComponent.plus(BookCommentActivityModule(this))
                .injectTo(this)
    }

    override fun onError(error: String) {
        Log.e(TAG, error)
        showToast(error)
    }

    override fun showDetailData(data: Any) {
        if (data is BookReviewsListResponse) {
            if (data.start == 0 ) {
                mBookReviewsListRes.reviews = emptyList()
            }
            mBookReviewsListRes.total = data.total
            val newReviews = ArrayList<BookReviewResponse>()
            newReviews.addAll(mBookReviewsListRes.reviews)
            newReviews.addAll(data.reviews)
            mBookReviewsListRes.reviews = newReviews

            mBookCommentAdapter.notifyDataSetChanged()
            if (data.total > page * count) {
                page ++
                isLoadAll = false
            } else {
                isLoadAll = true
            }
        }
    }

    override fun showProgressView() {
        swipeRefreshWidget.isRefreshing = true
    }

    override fun hideProgressView() {
        swipeRefreshWidget.isRefreshing = false
    }

    override fun onRefresh() {
        page = 0
        mBookDetailPresenter.execute(RequestDetailParams(bookId, page * count, count, COMMENT_FIELDS))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mBookDetailPresenter.cancel()
        super.onDestroy()
    }

    private fun loadMore() {
        if (isLoadAll) {
            Snackbar.make(toolbar,  R.string.no_more_comment, Snackbar.LENGTH_SHORT).show()
        } else {
            mBookDetailPresenter.execute(RequestDetailParams(bookId, page * count, count, COMMENT_FIELDS))
        }
    }

    private fun initEvent() {
        mBookReviewsListRes = BookReviewsListResponse()
        mBookReviewsListRes.reviews = emptyList()
        swipeRefreshWidget.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4)

        mBookCommentAdapter = BookCommentAdapter(mBookReviewsListRes)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mBookCommentAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        bookName = intent.getStringExtra("book_name")
        bookId = intent.getStringExtra("book_id")
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.ic_action_navigation_arrow_back_inverted)
        title = "$bookName${getString(R.string.comment_of_book)}"

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastVisibleItem: Int = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mBookCommentAdapter.getItemCount()) {
                    loadMore()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            }
        })

        swipeRefreshWidget.setOnRefreshListener(this)
        onRefresh()
    }

}
