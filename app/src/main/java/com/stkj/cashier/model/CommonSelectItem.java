package com.stkj.cashier.model;

public class CommonSelectItem {
    private String type;
    private String name;
    private boolean isSelect;

    public CommonSelectItem(int type, String name) {
        this.type = String.valueOf(type);
        this.name = name;
    }

    public CommonSelectItem(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public CommonSelectItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public int getTypeInt() {
        int type = 0;
        try {
            type = Integer.parseInt(getType());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
