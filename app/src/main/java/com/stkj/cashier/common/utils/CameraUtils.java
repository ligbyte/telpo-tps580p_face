package com.stkj.cashier.common.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 相机工具类
 */
public class CameraUtils {
    public static final int CAMERA_FACING_BACK = 0;
    public static final int CAMERA_FACING_FRONT = 1;

    /**
     * 查找前置摄像头Id
     */
    public static List<Integer> getFontCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        List<Integer> fontNumList = new ArrayList<Integer>();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING_FRONT) {
                fontNumList.add(i);
            }
        }
        return fontNumList;
    }

    /**
     * 查找后摄像头Id
     */
    public static List<Integer> getBackCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        List<Integer> backNumList = new ArrayList<Integer>();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING_BACK) {
                backNumList.add(i);
            }
        }
        return backNumList;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static List<String> getDualCameraList(Context context) {
        //获取管理类
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            //获取所有逻辑ID
            String[] cameraIdList = manager.getCameraIdList();
            //获取逻辑摄像头下拥有多个物理摄像头的类 作为双镜类
            for (String id : cameraIdList) {
                CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(id);
                Set<String> physicalCameraIds = cameraCharacteristics.getPhysicalCameraIds();
                Log.d("CameraUtils", "逻辑ID：" + id + " 下的物理ID: " + Arrays.toString(physicalCameraIds.toArray()));
                if (physicalCameraIds.size() >= 2) {
                    List<String> dualCameraList = new ArrayList<>();
                    Object[] objects = physicalCameraIds.toArray();
                    dualCameraList.add(String.valueOf(objects[0]));
                    dualCameraList.add(String.valueOf(objects[1]));
                    return dualCameraList;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCameraPreviewOrientation(Activity activity, boolean isFrontCamera) {
        int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        if (isFrontCamera) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Log.i("CameraUtils", "prepare degrees: " + degrees);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}
