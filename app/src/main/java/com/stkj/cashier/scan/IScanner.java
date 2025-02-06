package com.stkj.cashier.scan;

import com.stkj.cashier.scan.scan.ScanCallBack;

public interface IScanner {

    public void startScan();

    public void setCallBack(ScanCallBack pScanCallBack);

    public void sendData(byte[] pBytes);

    void playSound(boolean pPlay);

    public void stopScan();
}
