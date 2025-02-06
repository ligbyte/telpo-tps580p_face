package com.stkj.cashier.cbgfacepass.model;

import java.util.List;

public class FacePassPeopleListInfo {

    private List<FacePassPeopleInfo> Results;
    private int totalCount;
    private int totalPage;
    private int pageIndex;
    private int pageSize;

    public FacePassPeopleListInfo() {
    }

    public List<FacePassPeopleInfo> getResults() {
        return Results;
    }

    public void setResults(List<FacePassPeopleInfo> results) {
        Results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}