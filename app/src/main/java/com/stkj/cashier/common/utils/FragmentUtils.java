package com.stkj.cashier.common.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentUtils {

    public static void safeAddFragment(FragmentManager fragmentManager, Fragment fragment, @NonNull String tag) {
        try {
            fragmentManager.beginTransaction()
                    .add(fragment, tag)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void safeAddFragment(FragmentManager fragmentManager, Fragment fragment, int containViewId) {
        try {
            fragmentManager.beginTransaction()
                    .add(containViewId, fragment)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void safeReplaceFragment(FragmentManager fragmentManager, Fragment fragment, int containViewId, @NonNull String tag) {
        try {
            fragmentManager.beginTransaction()
                    .replace(containViewId, fragment, tag)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void safeReplaceFragment(FragmentManager fragmentManager, Fragment fragment, int containViewId) {
        try {
            fragmentManager.beginTransaction()
                    .replace(containViewId, fragment)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void safeRemoveFragment(FragmentManager fragmentManager, Fragment fragment) {
        try {
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void safeAttachFragment(FragmentManager fragmentManager, Fragment fragment) {
        try {
            fragmentManager.beginTransaction()
                    .attach(fragment)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void safeDetachFragment(FragmentManager fragmentManager, Fragment fragment) {
        try {
            fragmentManager.beginTransaction()
                    .detach(fragment)
                    .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
