package com.ultimatestorytelling.backend.common.page;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PagingButtonInfo {

    private int currentPage;
    private int startPage;
    private int endPage;

    public PagingButtonInfo(int currentPage, int startPage, int endPage) {
        this.currentPage = currentPage;
        this.startPage = startPage;
        this.endPage = endPage;
    }

}
