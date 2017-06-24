package cn.haohao.dbbook.presentation.adapter

import android.support.v7.widget.AppCompatRatingBar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.haohao.dbbook.R
import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_book_list.view.*

/**
 * Created by HaohaoChang on 2017/6/12.
 */
class BookListAdapter(private var books: ArrayList<BookInfoResponse>,
                      private var columns: Int,
                      private var itemClick: (View, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        val TYPE_EMPTY = 0
        val TYPE_DEFAULT = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindView(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        if (viewType == TYPE_DEFAULT){
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book_list, parent, false)
            return BookListViewHolder(itemView)
        } else {
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            return EmptyViewHolder(itemView)
        }

    }

    override fun getItemCount(): Int {
        if (books.isEmpty())
            return 1
        return books.size
    }

    override fun getItemViewType(position: Int): Int {
        if (books.isEmpty()) {
            return TYPE_EMPTY
        } else {
            return TYPE_DEFAULT
        }
    }

    fun getItemColumnSpan(position: Int): Int {
        return when(getItemViewType(position)) {
            TYPE_DEFAULT -> 1
            else -> columns
        }
    }

    fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BookListViewHolder) {
            val book = books[position]
            val itemView = holder.itemView
            Glide.with(holder.itemView.context)
                    .load(book.images.large)
                    .into(itemView.iv_book_img)
            itemView.tv_book_title.text = book.title
            itemView.ratingBar_hots.rating = book.rating.average.toFloat() / 2
            itemView.tv_hots_num.text  = book.rating.average
            itemView.tv_book_info.text = book.infoString
            itemView.tv_book_description.text = "\u3000${book.summary}"
            itemView.setOnClickListener {
                itemClick(itemView.iv_book_img, position)
            }
        }
    }

    fun clear() {
        books.clear()
    }

    fun onRefreshData(newData: List<BookInfoResponse>) {
        if (!books.isEmpty()) {
            clear()
        }
        onLoadMoreData(newData)
    }

    fun onLoadMoreData(moreData: List<BookInfoResponse>) {
        books.addAll(moreData)
        notifyDataSetChanged()
    }

    class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}