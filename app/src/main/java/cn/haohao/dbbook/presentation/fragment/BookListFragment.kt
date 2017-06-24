package cn.haohao.dbbook.presentation.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import cn.haohao.dbbook.di.ApplicationComponent
import cn.haohao.dbbook.di.subcomponent.list.BookListFragmentModule
import cn.haohao.dbbook.domain.entity.RequestListParams
import cn.haohao.dbbook.presentation.activity.BaseActivity
import cn.haohao.dbbook.presentation.activity.BookDetailActivity
import cn.haohao.dbbook.presentation.adapter.BookListAdapter
import cn.haohao.dbbook.presentation.presenter.BookListPresenter
import cn.haohao.dbbook.presentation.view.BookListView
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import javax.inject.Inject

/**
 * Created by HaohaoChang on 2017/6/12.
 */
class BookListFragment : BaseFragment(), BookListView, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val FIELDS = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,series"
        val KEY_TAG = "key_tag"
        val SPAN_COUNT = 1
        val TAG = "BookListFragment"

        fun getInstance(tag: String): Fragment {
            val args = Bundle()
            args.putString(KEY_TAG, tag)
            val fragment = BookListFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var page = 0
    private var count = 20
    private var bookTag = "hot"
    private var books = ArrayList<BookInfoResponse>()
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var bookListPresenter: BookListPresenter

    override fun initRootView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) {
        rootView = inflater?.inflate(R.layout.recycler_content, container, false) as View
        bookTag = arguments.getString(KEY_TAG)

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initEvents() {
        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        refreshLayout = rootView.findViewById(R.id.swipe_refresh_widget) as SwipeRefreshLayout
        refreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4)
        gridLayoutManager = GridLayoutManager(context, SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return bookListAdapter.getItemColumnSpan(position)
            }
        }

        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager

        //adapter
        bookListAdapter = BookListAdapter(books, SPAN_COUNT){
            view: View, i: Int -> navigateToDetail(view, i)
        }
        recyclerView.adapter = bookListAdapter

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addOnScrollListener(RecyclerViewScrollListener())
        refreshLayout.setOnRefreshListener(this)
    }

    override fun initData(isSavedNull: Boolean) {
    }

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.plus(BookListFragmentModule(this))
                .injectTo(this)
        onRefresh()

    }

    override fun onError(error: String) {
        Log.e(TAG, error)

    }

    override fun showProgressView() {
        refreshLayout.isRefreshing = true
    }

    override fun hideProgressView() {
        refreshLayout.isRefreshing = false
    }

    override fun refreshData(books: List<BookInfoResponse>) {
        bookListAdapter.onRefreshData(books)
        page = 1
    }

    override fun addData(books: List<BookInfoResponse>) {
        bookListAdapter.onLoadMoreData(books)
        page ++
    }

    override fun onRefresh() {
        bookListPresenter.execute(RequestListParams(null, bookTag, 0, count, FIELDS))
    }

    override fun onDestroyView() {
        bookListPresenter.cancel()
        super.onDestroyView()
    }

    fun navigateToDetail(imageView: View, position: Int) {
        val bookCoverImageView: ImageView = imageView as ImageView
        val bundle = Bundle()
        bundle.putSerializable(BookInfoResponse.serialVersionName, books[position])
        val glideBitmapDrawable = bookCoverImageView.drawable as GlideBitmapDrawable
        glideBitmapDrawable?.let {
            val bitmap = glideBitmapDrawable.bitmap
            bundle.putParcelable("book_img", bitmap)
        }
        val intent = Intent(context, BookDetailActivity::class.java)
        intent.putExtras(bundle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val optionCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(BaseActivity.instance, imageView, BaseActivity.BOOK_DETAIL_TRANSITION_NAME)
            context.startActivity(intent, optionCompat.toBundle())
        } else {
            context.startActivity(intent)
        }

    }

    fun loadMore(){
        if (!refreshLayout.isRefreshing && !books.isEmpty()) {
            bookListPresenter.execute(RequestListParams(null, bookTag, page * count, count, FIELDS))
        }

    }

    inner class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

        private var lastVisibleItem: Int = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    lastVisibleItem + 1 == bookListAdapter.itemCount) {
                loadMore()

            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
        }

    }
}