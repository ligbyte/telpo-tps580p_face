package com.stkj.cashier.cbgfacepass.data;

import com.tencent.mmkv.MMKV;

import java.util.HashSet;
import java.util.Set;

public class FacePassDateBaseMMKV {

    public static final String MMKV_NAME = "face_pass_db_setting";
    public static final String KEY_DEPARTMENT_LIST = "people_department_list";
    public static final String KEY_ACCOUNT_TYPE_LIST = "people_account_type_list";

    public static Set<String> getDepartmentList() {
        MMKV mmkv = getMMKV();
        return mmkv.getStringSet(KEY_DEPARTMENT_LIST, new HashSet<>());
    }

    public static void putDepartmentList(Set<String> departmentList) {
        getMMKV().putStringSet(KEY_DEPARTMENT_LIST, departmentList);
    }

    public static void removeDepartmentList() {
        getMMKV().remove(KEY_DEPARTMENT_LIST);
    }

    public static Set<String> getAccountTypeList() {
        MMKV mmkv = getMMKV();
        return mmkv.getStringSet(KEY_ACCOUNT_TYPE_LIST, new HashSet<>());
    }

    public static void putAccountTypeList(Set<String> accountTypeList) {
        getMMKV().putStringSet(KEY_ACCOUNT_TYPE_LIST, accountTypeList);
    }

    public static void removeAccountTypeList() {
        getMMKV().remove(KEY_ACCOUNT_TYPE_LIST);
    }

    public static MMKV getMMKV() {
        return MMKV.mmkvWithID(MMKV_NAME);
    }

}
