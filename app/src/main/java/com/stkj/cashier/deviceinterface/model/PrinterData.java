package com.stkj.cashier.deviceinterface.model;

/**
 * 打印机一行文本
 */
public class PrinterData {

    public static final int SIZE_NORMAL = 0;
    public static final int SIZE_SMALL = -1;
    public static final int SIZE_LARGE = 1;

    public static final int ALIGN_LEFT = -1;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_RIGHT = 1;

    //文字大小
    private int size = SIZE_NORMAL;
    //文字对齐
    private int align = ALIGN_LEFT;
    //字体加粗
    private boolean bold;
    private String text;

    public PrinterData() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBold() {
        return bold;
    }

    public static PrinterData newTitlePrintData(String title) {
        PrinterData printerData = new PrinterData();
        printerData.bold = true;
        printerData.align = ALIGN_CENTER;
        printerData.size = SIZE_LARGE;
        printerData.text = title;
        return printerData;
    }

    public static PrinterData newContentPrintData(String content) {
        PrinterData printerData = new PrinterData();
        printerData.bold = false;
        printerData.align = ALIGN_LEFT;
        printerData.size = SIZE_NORMAL;
        printerData.text = content;
        return printerData;
    }

    public static PrinterData newCenterContentPrintData(String content) {
        PrinterData printerData = new PrinterData();
        printerData.bold = false;
        printerData.align = ALIGN_CENTER;
        printerData.size = SIZE_NORMAL;
        printerData.text = content;
        return printerData;
    }

    public static PrinterData newDivideLinePrintData() {
        PrinterData printerData = new PrinterData();
        printerData.bold = false;
        printerData.align = ALIGN_CENTER;
        printerData.size = SIZE_NORMAL;
        printerData.text = "--------------------------------\n";
        return printerData;
    }
}
