package com.stkj.cashier.scan;


public interface ScanCodeCallback {
     void startScan();
     void stopScan();
     void refund();
}
