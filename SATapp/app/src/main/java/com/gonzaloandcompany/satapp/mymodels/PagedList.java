package com.gonzaloandcompany.satapp.mymodels;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PagedList<T> {
    private int page = 0;
    private List<T> results;
    private int totalResults = 0;
    private int totalPages = 0;

    public PagedList(int page, List<T> results, int totalResults, int totalPages) {
        this.page = page;
        this.results = new ArrayList<>();
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }


}
