package cn.haohao.dbbook.data.entity.http;

import java.util.ArrayList;


public class BookSeriesListResponse extends BookListResponse {
    public BookSeriesListResponse() {
        this.books = new ArrayList<>();
    }
}
