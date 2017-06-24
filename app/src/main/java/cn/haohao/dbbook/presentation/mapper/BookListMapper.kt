package cn.haohao.dbbook.presentation.mapper

import cn.haohao.dbbook.data.entity.http.BookInfoResponse
import cn.haohao.dbbook.data.entity.http.BookListResponse

/**
 * Created by HaohaoChang on 2017/6/12.
 */
class BookListMapper {
    fun transform(bookListResponse: BookListResponse): List<BookInfoResponse> = bookListResponse.books
}