package com.stkj.cashier.common.camera;

import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.stkj.cashier.R;
import com.stkj.cashier.common.permissions.PermissionHelper;
import com.stkj.cashier.common.permissions.callback.PermissionCallback;
import com.stkj.cashier.common.storage.MediaHelper;
import com.stkj.cashier.common.ui.dialog.AppAlertDialog;
import com.stkj.cashier.common.ui.fragment.BaseRecyclerFragment;
import com.stkj.cashier.common.ui.widget.shapelayout.ShapeImageView;
import com.stkj.cashier.common.utils.FragmentUtils;
import com.stkj.cashier.glide.GlideApp;
import com.stkj.cashier.util.rxjava.AutoDisposeUtils;
import com.stkj.cashier.util.rxjava.DefaultDisposeObserver;
import com.stkj.cashier.util.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SimpleCameraFragment extends BaseRecyclerFragment {

    private SurfaceView surfaceView;
    private ShapeImageView ivCenter;
    private TextView tvCaptureStatus;
    private ImageView ivCamera;
    private ImageView ivClose;
    private ImageView ivSwitch;
    private CameraHelper cameraHelper;
    private boolean canSwitchCamera;
    private boolean enableCapture;
    private OnSimpleCameraListener simpleCameraListener;
    private int displayOrientation = -1;
    private int cameraId = -1;

    public void setCanSwitchCamera(boolean canSwitchCamera) {
        this.canSwitchCamera = canSwitchCamera;
    }

    public void setEnableCapture(boolean enableCapture) {
        this.enableCapture = enableCapture;
    }

    public void setSimpleCameraListener(OnSimpleCameraListener simpleCameraListener) {
        this.simpleCameraListener = simpleCameraListener;
    }

    public void setDisplayOrientation(int displayOrientation) {
        this.displayOrientation = displayOrientation;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_simple_camera;
    }

    @Override
    protected void initViews(View rootView) {
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        tvCaptureStatus = (TextView) findViewById(R.id.tv_capture_status);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivSwitch = (ImageView) findViewById(R.id.iv_switch);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.safeRemoveFragment(getParentFragmentManager(), SimpleCameraFragment.this);
            }
        });
        if (canSwitchCamera) {
            ivSwitch.setVisibility(View.VISIBLE);
            ivSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraHelper.switchCamera();
                }
            });
        } else {
            ivSwitch.setVisibility(View.GONE);
        }
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableCapture) {
                    if (cameraHelper.isCaptureStarted()) {
                        cameraHelper.stopCaptureVideo();
                    } else {
                        cameraHelper.takePicture();
                    }
                } else {
                    cameraHelper.takePicture();
                }
            }
        });
        ivCamera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (enableCapture) {
                    cameraHelper.startCaptureVideo();
                }
                return true;
            }
        });
        ivCenter = (ShapeImageView) findViewById(R.id.iv_center);
        ivCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCenter.setVisibility(View.GONE);
            }
        });
        tvCaptureStatus.setText(enableCapture ? "点击拍照,长按录像" : "点击拍照");
    }

    @Override
    protected void onFragmentResume(boolean isFirstOnResume) {
        SimpleCameraPermission simpleCameraPermission = new SimpleCameraPermission();
        simpleCameraPermission.setEnableCapture(enableCapture);
        PermissionHelper.with(mActivity)
                .requestPermission(simpleCameraPermission, new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        Schedulers.io().scheduleDirect(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLong("取消授权，无法使用相机");
                    }
                });
    }

    private void initData() {
        cameraHelper = new CameraHelper(mActivity);
        cameraHelper.setCameraId(cameraId);
        cameraHelper.setDisplayOrientation(displayOrientation);
        cameraHelper.prepare(surfaceView);
        cameraHelper.setCameraHelperCallback(new CameraHelper.OnCameraHelperCallback() {
            @Override
            public void onTakePictureSuccess(String picPath) {
                if (simpleCameraListener != null) {
                    simpleCameraListener.onTakePicture(picPath);
                    FragmentUtils.safeRemoveFragment(getParentFragmentManager(), SimpleCameraFragment.this);
                } else {
                    ivCenter.setVisibility(View.VISIBLE);
                    GlideApp.with(SimpleCameraFragment.this).load(picPath).into(ivCenter);
                    MediaHelper.insertPicToGallery(picPath);
                    cameraHelper.startPreview();
                }
            }

            @Override
            public void onTakePictureError(String message) {
                if (simpleCameraListener != null) {
                    simpleCameraListener.onTakePictureError(message);
                    FragmentUtils.safeRemoveFragment(getParentFragmentManager(), SimpleCameraFragment.this);
                } else {
                    AppAlertDialog.build().setTitleText("拍照失败")
                            .setContentText(message)
                            .setConfirmText("确定")
                            .show(mActivity);
                    cameraHelper.startPreview();
                }
            }

            @Override
            public void onCaptureVideoSuccess(String videoPath) {
                if (simpleCameraListener != null) {
                    simpleCameraListener.onCaptureVideo(videoPath);
                    FragmentUtils.safeRemoveFragment(getParentFragmentManager(), SimpleCameraFragment.this);
                } else {
                    refreshStopCaptureStatus();
                    ivCenter.setVisibility(View.VISIBLE);
                    GlideApp.with(SimpleCameraFragment.this).load(videoPath).into(ivCenter);
                    MediaHelper.insertVideoToMedia(videoPath);
                }
            }

            @Override
            public void onCaptureVideoError(String message) {
                if (simpleCameraListener != null) {
                    simpleCameraListener.onCaptureVideoError(message);
                    FragmentUtils.safeRemoveFragment(getParentFragmentManager(), SimpleCameraFragment.this);
                } else {
                    refreshStopCaptureStatus();
                    AppAlertDialog.build().setTitleText("录像失败")
                            .setContentText(message)
                            .setConfirmText("确定")
                            .show(mActivity);
                }
            }

            @Override
            public void onCaptureVideoStart() {
                refreshStartCaptureStatus();
            }
        });
    }

    private DefaultDisposeObserver<Long> captureStatusObserver;
    private long captureTime;

    private void refreshStartCaptureStatus() {
        refreshStopCaptureStatus();
        tvCaptureStatus.setText("正在录制");
        ivCamera.setImageResource(R.mipmap.icon_record_press);
        captureTime = 0;
        captureStatusObserver = new DefaultDisposeObserver<Long>() {
            @Override
            protected void onSuccess(Long aLong) {
                captureTime++;
                tvCaptureStatus.setText("正在录制: " + captureTime + "秒");
            }
        };
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDisposeUtils.onDestroyDispose(this))
                .subscribe(captureStatusObserver);
    }

    private void refreshStopCaptureStatus() {
        if (captureStatusObserver != null) {
            captureStatusObserver.dispose();
            captureStatusObserver = null;
        }
        tvCaptureStatus.setText("点击拍照,长按录像");
        ivCamera.setImageResource(R.mipmap.icon_record_normal);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraHelper != null) {
            cameraHelper.onClear();
        }
    }
}
