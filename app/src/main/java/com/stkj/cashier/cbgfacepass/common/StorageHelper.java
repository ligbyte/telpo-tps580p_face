package com.stkj.cashier.cbgfacepass.common;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.stkj.cashier.App;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class StorageHelper {

    public static String APP_PREFIX_TAG = "androidapp";
    public static String PUB_DIR_DCIM_PREFIX_TAG = Environment.DIRECTORY_DCIM + "/";
    public static String PUB_DIR_DOWNLOAD_PREFIX_TAG = Environment.DIRECTORY_DOWNLOADS + "/";

    public static String CONTENT_DOCUMENT_BASE_PATH = "content://com.android.externalstorage.documents/document/primary:";

    //缓存文件目录
    public static String getDirCachePath() {
        return "/" + APP_PREFIX_TAG + "cache/";
    }

    //分享文件目录
    public static String getDirSharePath() {
        return "/" + APP_PREFIX_TAG + "share/";
    }

    //下载文件目录
    public static String getDirDownloadPath() {
        return "/" + APP_PREFIX_TAG + "download/";
    }

    //下载目录名称(android 10 +)
    public static String getRelativeExternalDirPath() {
        return "/" + APP_PREFIX_TAG + "/";
    }

    /**
     * 创建分享文件
     *
     * @param shareFileName 文件名字
     */
    public static File createShareFile(String shareFileName) {
        return createExternalFile(getDirSharePath(), shareFileName);
    }

    /**
     * 创建缓存文件
     *
     * @param cacheFileName 文件名字
     */
    public static File createCacheFile(String cacheFileName) {
        return createExternalFile(getDirCachePath(), cacheFileName);
    }

    /**
     * 创建下载文件
     *
     * @param downloadFileName 文件名字
     */
    public static File createDownloadFile(String downloadFileName) {
        return createExternalFile(getDirDownloadPath(), downloadFileName);
    }

    /**
     * 创建外置存储app内的文件 不需要外置存储权限
     *
     * @param parentFilePath 父目录
     * @param fileName       文件名字
     */
    private static File createExternalFile(String parentFilePath, String fileName) {
        String storageFileRootPath = getExternalFileRootPath();
        File parentDir = new File(storageFileRootPath + parentFilePath);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        File cacheFile = new File(parentDir, fileName);
        return cacheFile;
    }

    /**
     * 获取指定外置目录路径
     */
    public static String getExternalCustomDirPath(String dirName) {
        return getExternalDirPath("/" + dirName + "/");
    }

    /**
     * 获取缓存目录路径
     */
    public static String getExternalCacheDirPath() {
        return getExternalDirPath(getDirCachePath());
    }

    /**
     * 获取分享目录路径
     */
    public static String getExternalShareDirPath() {
        return getExternalDirPath(getDirSharePath());
    }

    /**
     * 获取下载目录路径
     */
    public static String getExternalDownloadDirPath() {
        return getExternalDirPath(getDirDownloadPath());
    }

    /**
     * 获取存储目录
     *
     * @param dirPath /目录名称/
     */
    private static String getExternalDirPath(String dirPath) {
        String storageFileRootPath = getExternalFileRootPath();
        File dir = new File(storageFileRootPath + dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取文件 (先获取外置目录[/Android/data/x.x.x/files/]，失败获取app安装文件目录[data/data/x.x.x/files])
     */
    private static String getExternalFileRootPath() {
        File externalFile = App.instance.getApplicationContext().getExternalFilesDir(null);
        if (externalFile != null) {
            return externalFile.getAbsolutePath();
        } else {
            return App.instance.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    // ----------- api < android 10  start-------------

    /**
     * 获取系统常用文件夹（下载目录）(api < android 10)
     */
    public static String getPublicDownloadsDirPath() {
        return getPublicDirPath(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取系统常用文件夹（相册目录）(api < android 10)
     */
    public static String getPublicDCIMDirPath() {
        return getPublicDirPath(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取系统常用文件夹
     *
     * @param dirType Environment.DIRECTORY_xxxx
     */
    private static String getPublicDirPath(String dirType) {
        File dcimFile = Environment.getExternalStoragePublicDirectory(dirType);
        File realDir = new File(dcimFile.getAbsolutePath() + getRelativeExternalDirPath());
        if (!realDir.exists()) {
            realDir.mkdirs();
        }
        return realDir.getAbsolutePath();
    }


    /**
     * 获取系统常用文件夹（下载目录）(api >= android 10)
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String getRelativeDownloadsDirPath() {
        return Environment.DIRECTORY_DOWNLOADS + getRelativeExternalDirPath();
    }

    /**
     * 获取系统常用文件夹（相册目录）(api >= android 10)
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String getRelativeDCIMDirPath() {
        return Environment.DIRECTORY_DCIM + getRelativeExternalDirPath();
    }

    // ----------- api >= android 10  end-------------

    public static String getContentPubDownloadDirPath() {
        return CONTENT_DOCUMENT_BASE_PATH + Environment.DIRECTORY_DOWNLOADS + Uri.encode("/" + APP_PREFIX_TAG);
    }

    public static String getContentPubDCIMDirPath() {
        return CONTENT_DOCUMENT_BASE_PATH + Environment.DIRECTORY_DCIM + Uri.encode("/" + APP_PREFIX_TAG);
    }





}
