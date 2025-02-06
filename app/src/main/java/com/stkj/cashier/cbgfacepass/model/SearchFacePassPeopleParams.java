package com.stkj.cashier.cbgfacepass.model;

/**
 * 搜索人脸库参数
 */
public class SearchFacePassPeopleParams {

    private int requestOffset;
    private String accountType;
    private String department;
    private String searchKey;

    public SearchFacePassPeopleParams() {
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public int getRequestOffset() {
        return requestOffset;
    }

    public void setRequestOffset(int requestOffset) {
        this.requestOffset = requestOffset;
    }

    public void resetParams() {
        requestOffset = 0;
        accountType = "全部人员";
        department = "全部部门";
        searchKey = "";
    }
}
