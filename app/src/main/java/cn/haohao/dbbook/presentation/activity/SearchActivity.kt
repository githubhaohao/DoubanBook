package cn.haohao.dbbook.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import cn.haohao.dbbook.di.ApplicationComponent
import cn.haohao.dbbook.di.subcomponent.list.SearchActivityModule
import cn.haohao.dbbook.domain.entity.RequestListParams
import cn.haohao.dbbook.presentation.adapter.BookListAdapter
import cn.haohao.dbbook.presentation.presenter.BookListPresenter
import cn.haohao.dbbook.presentation.util.showToast
import cn.haohao.dbbook.presentation.view.BookListView
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_search_layout.*
import javax.inject.Inject

class SearchActivity : BaseActivity(), BookListView, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "SearchActivity"
        const val FIELDS = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13"
    }
    private var count = 20
    private var page = 0
    private lateinit var queryKey: String
    private var isLoadAll = false
    private lateinit var mBookListAdapter: BookListAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private val mBookInfoResList = ArrayList<BookInfoResponse>()
    private var spanCount = 1

    @Inject
    lateinit var mBookListPresenter: BookListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_layout)
        injectToThis()
        initSearchView()
        initEvent()
        val handler = Handler()
        handler.postDelayed({
            searchView.showSearch(true)
        }, 500)
    }

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        super.injectDependencies(applicationComponent)
        applicationComponent.plus(SearchActivityModule(this))
                .injectTo(this)
    }

    override fun onError(error: String) {
        Log.e(TAG, error)
    }

    override fun showProgressView() {
        swipeRefreshWidget.isRefreshing = true
    }

    override fun hideProgressView() {
        swipeRefreshWidget.isRefreshing = false
    }

    override fun refreshData(books: List<BookInfoResponse>) {
        if (books.isEmpty()) {
            isLoadAll = true
        } else {
            isLoadAll = false
            mBookListAdapter.onRefreshData(books)
        }
        page = 1

    }

    override fun addData(books: List<BookInfoResponse>) {
        if (books.isEmpty()) {
            isLoadAll = true
        } else {
            isLoadAll = false
            page ++
            mBookListAdapter.onLoadMoreData(books)
        }
    }

    override fun onRefresh() {
        mBookListPresenter.execute(RequestListParams(queryKey, null, 0, count, FIELDS))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false)
                }
            }

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        mBookListPresenter.cancel()
        super.onDestroy()
    }

    private fun loadMore() {
        if (!isLoadAll) {
            if (!swipeRefreshWidget.isRefreshing) {
                mBookListPresenter.execute(RequestListParams(queryKey, null, page * count, count, FIELDS))
            }
        } else {
            showToast(resources.getString(R.string.no_more))
        }
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!TextUtils.isEmpty(query)) {
                    queryKey = query
                    title = "$query 的搜索结果"
                    initRefresh()
                    onRefresh()

                } else {
                    Snackbar.make(searchView, R.string.keyword_is_empty, Snackbar.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {

            }
        })

    }

    private fun initEvent() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        title = "搜索"
        toolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.ic_action_navigation_arrow_back_inverted)
        swipeRefreshWidget.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4)
        mLayoutManager = GridLayoutManager(this, spanCount)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = mLayoutManager

        mBookListAdapter = BookListAdapter(mBookInfoResList, spanCount) {
            view: View, position: Int  -> navigateToDetail(view, position)
        }
        recyclerView.adapter = mBookListAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    fun initRefresh() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastVisibleItem: Int = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mBookListAdapter.itemCount) {
                    loadMore()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
            }
        })
        swipeRefreshWidget.setOnRefreshListener(this)
    }

    fun navigateToDetail(imageView: View, position: Int) {
        val bookCoverImageView: ImageView = imageView as ImageView
        val bundle = Bundle()
        bundle.putSerializable(BookInfoResponse.serialVersionName, mBookInfoResList[position])
        val glideBitmapDrawable = bookCoverImageView.drawable as GlideBitmapDrawable
        glideBitmapDrawable?.let {
            val bitmap = glideBitmapDrawable.bitmap
            bundle.putParcelable("book_img", bitmap)
        }
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtras(bundle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val optionCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(BaseActivity.instance, imageView, BaseActivity.BOOK_DETAIL_TRANSITION_NAME)
            startActivity(intent, optionCompat.toBundle())
        } else {
            startActivity(intent)
        }

    }
}
