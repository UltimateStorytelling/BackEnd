package com.ultimatestorytelling.backend.common.page;

import org.springframework.data.domain.Page;

public class Pagenation {

    public static PagingButtonInfo getPaginButtonInfo(Page page){

        int currentPage = page.getNumber() + 1; // 페이지 번호 1부터 시작
        int startPage; // 현재 페이지
        int endPage; // 마지막 페이지
        int defaultButtonCount = 10; //버튼 갯수

        startPage = (int) (Math.ceil((double) currentPage / defaultButtonCount) -1) * defaultButtonCount +1;
        endPage = startPage + defaultButtonCount - 1;

        if(page.getTotalPages() < endPage){
            endPage = page.getTotalPages();
        }

        if(page.getTotalPages() == 0 && endPage == 0) {
            endPage = startPage;
        }

        return new PagingButtonInfo(currentPage, startPage, endPage);
    }
}
