package com.stkj.cashier.cbgfacepass.model;

/**
 * 添加到本地数据库包装类
 */
public class AddLocalFacePassInfoWrapper {

    public static final int STATE_SUCCESS = 1;
    public static final int STATE_ERROR = -1;
    //卡禁用状态
    public static final int STATE_FORBID = 2;

    private long localDatabaseCount;

    private int currentIndex;
    private int totalSize;

    private int status;
    private String statusMsg;
    private FacePassPeopleInfo facePassPeopleInfo;

    public AddLocalFacePassInfoWrapper(FacePassPeopleInfo facePassPeopleInfo) {
        this.facePassPeopleInfo = facePassPeopleInfo;
    }

    public FacePassPeopleInfo getFacePassPeopleInfo() {
        return facePassPeopleInfo;
    }

    public void setFacePassPeopleInfo(FacePassPeopleInfo facePassPeopleInfo) {
        this.facePassPeopleInfo = facePassPeopleInfo;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isEndIndex() {
        return currentIndex != 0 && totalSize != 0 && currentIndex == totalSize;
    }

    public long getLocalDatabaseCount() {
        return localDatabaseCount;
    }

    public void setLocalDatabaseCount(long localDatabaseCount) {
        this.localDatabaseCount = localDatabaseCount;
    }
}
