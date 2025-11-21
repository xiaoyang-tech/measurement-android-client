package cn.xymind.healthdetection.synthesis.androidsdksamples;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.Delegate;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector;
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "MainActivity";
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private ImageReader imageReader;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private CaptureRequest.Builder previewRequestBuilder;
    private SurfaceHolder surfaceHolder;
    private int currentRotation = 0;  // 0: 默认, 90: 右旋, 180: 倒转, 270: 左旋
    private boolean isCameraFront = true;
    private FaceDetector faceDetector; // 添加人脸检测器
    private SurfaceView surfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        // 初始化人脸检测器
        FaceDetector.FaceDetectorOptions options = FaceDetector.FaceDetectorOptions.builder()
                .setBaseOptions(BaseOptions.builder()
                        .setModelAssetPath("blaze_face_short_range.tflite") // 确保不带路径前缀
                        .setDelegate(Delegate.CPU) // 先使用CPU模式测试稳定性
                        .build())
                .setRunningMode(RunningMode.IMAGE)
                .setMinDetectionConfidence(0.7f)
                .build();

// 添加错误处理
        try {
            faceDetector = FaceDetector.createFromOptions(this, options);
        } catch (Exception e) {
            Log.e(TAG, "人脸检测器初始化失败", e);
            // 回退方案
            try {
                options = FaceDetector.FaceDetectorOptions.builder()
                        .setBaseOptions(BaseOptions.builder()
                                .setModelAssetPath("face_detection_short_range.tflite")
                                .setDelegate(Delegate.CPU)
                                .build())
                        .setRunningMode(RunningMode.IMAGE)
                        .setMinDetectionConfidence(0.7f)
                        .build();
                faceDetector = FaceDetector.createFromOptions(this, options);
            } catch (Exception ex) {
                Log.e(TAG, "回退方案也失败", ex);
                Toast.makeText(this, "人脸检测功能不可用", Toast.LENGTH_LONG).show();
            }
        }

        // 初始化时调整尺寸
        adjustSurfaceViewSize();

        findViewById(R.id.btnCapture).setOnClickListener(v -> {
            if (cameraDevice == null || captureSession == null) {
                Toast.makeText(this, "相机未准备好", Toast.LENGTH_SHORT).show();
                return;
            }
            takePicture();
        });
        findViewById(R.id.btnRotate).setOnClickListener(v -> {
            currentRotation = (currentRotation + 90) % 360;
            Toast.makeText(this, "旋转至: " + currentRotation + "度", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustSurfaceViewSize();
        setCameraDisplayOrientation();
    }
    private void adjustSurfaceViewSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();

        // 计算最佳比例尺寸
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        float targetRatio = 9f / 16f; // 目标比例

        if (screenWidth > screenHeight) {
            // 横屏模式
            params.width = screenWidth;
            params.height = (int) (screenWidth * targetRatio);
        } else {
            // 竖屏模式
            params.width = screenWidth;
            params.height = (int) (screenWidth / targetRatio);
        }

        surfaceView.setLayoutParams(params);
    }

    private void setCameraDisplayOrientation() {
        if (cameraDevice == null) return;

        try {
            String cameraId = cameraManager.getCameraIdList()[isCameraFront ? 1 : 0];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

            int degrees = 0;
            switch (getWindowManager().getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }

            int result;
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                result = (sensorOrientation + degrees + currentRotation) % 360;
                result = (360 - result) % 360;
            } else {
                result = (sensorOrientation - degrees + currentRotation + 360) % 360;
            }

            if (previewRequestBuilder != null) {
                previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, result);
                captureSession.setRepeatingRequest(previewRequestBuilder.build(), null, backgroundHandler);
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "设置方向失败", e);
        }
    }


    // 修改拍照方法
    private void takePicture() {
        if (cameraDevice == null || captureSession == null) return;

        try {
            // 1. 先停止预览
            captureSession.stopRepeating();

            // 2. 创建ImageReader用于拍照
            imageReader = ImageReader.newInstance(
                    surfaceView.getWidth(),
                    surfaceView.getHeight(),
                    ImageFormat.JPEG, 2);

            // 3. 创建新的CaptureSession，包含ImageReader的Surface
            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            try {
                                // 4. 创建拍照请求
                                CaptureRequest.Builder captureBuilder =
                                        cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                                captureBuilder.addTarget(imageReader.getSurface());
                                captureBuilder.addTarget(surfaceHolder.getSurface());
                                captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                // 设置方向
                                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,
                                        getOrientation(getWindowManager().getDefaultDisplay().getRotation()));

                                // 5. 设置图片可用监听器
                                imageReader.setOnImageAvailableListener(reader -> {
                                    Image image = reader.acquireLatestImage();
                                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                                    byte[] data = new byte[buffer.remaining()];
                                    buffer.get(data);

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(currentRotation-90);
                                    if (isCameraFront) {
                                        matrix.postScale(-1, 1);
                                    }
                                    Bitmap rotatedBitmap = Bitmap.createBitmap(
                                            bitmap, 0, 0,
                                            bitmap.getWidth(), bitmap.getHeight(),
                                            matrix, true
                                    );

                                    // 人脸检测逻辑
                                    if (faceDetector != null) {
                                        MPImage mpImage = new BitmapImageBuilder(rotatedBitmap).build();
                                        FaceDetectorResult result = faceDetector.detect(mpImage);
                                        if (result.detections().size() > 0) {
                                            String faceCountText = "检测到 " + result.detections().size() + " 张人脸";
                                            Log.d(TAG, faceCountText);
                                            // 在UI线程上显示Toast
                                            runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                                    faceCountText, Toast.LENGTH_SHORT).show());
                                        }
                                    }

                                    saveImage(rotatedBitmap);
                                    image.close();
                                }, backgroundHandler);

                                // 6. 执行拍照
                                session.capture(captureBuilder.build(),
                                        new CameraCaptureSession.CaptureCallback() {
                                            @Override
                                            public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                                           @NonNull CaptureRequest request,
                                                                           @NonNull TotalCaptureResult result) {
                                                super.onCaptureCompleted(session, request, result);
                                                Log.d(TAG, "拍照完成");
                                                // 7. 拍照完成后恢复预览
                                                createCameraPreviewSession();
                                            }
                                        }, backgroundHandler);

                            } catch (CameraAccessException e) {
                                Log.e(TAG, "拍照失败", e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Log.e(TAG, "创建拍照会话失败");
                        }
                    }, backgroundHandler);

        } catch (CameraAccessException e) {
            Log.e(TAG, "拍照失败", e);
            // 出错时尝试恢复预览
            createCameraPreviewSession();
        }
    }

    private int getOrientation(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0: return 90;
            case Surface.ROTATION_90: return 0;
            case Surface.ROTATION_180: return 270;
            case Surface.ROTATION_270: return 180;
            default: return 0;
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
                backgroundThread = null;
                backgroundHandler = null;
            } catch (InterruptedException e) {
                Log.e(TAG, "停止后台线程失败", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放人脸检测器资源
        if (faceDetector != null) {
            faceDetector.close();
        }
    }

    private void saveImage(Bitmap bitmap) {
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this, "已保存: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder.getSurface() == null || cameraDevice == null) return;
        createCameraPreviewSession();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCamera();
    }

    private void closeCamera() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (surfaceHolder.getSurface().isValid()) {
            openCamera();
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void openCamera() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[isCameraFront ? 1 : 0];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    if (surfaceHolder.getSurface().isValid()) {
                        createCameraPreviewSession();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    cameraDevice.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "打开相机失败", e);
        }
    }

    private void createCameraPreviewSession() {
        if (cameraDevice == null || surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            Surface surface = surfaceHolder.getSurface();
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (cameraDevice == null) return;

                            captureSession = session;
                            try {
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                captureSession.setRepeatingRequest(
                                        previewRequestBuilder.build(), null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                Log.e(TAG, "创建预览会话失败", e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Toast.makeText(MainActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "创建预览会话失败", e);
        }
    }
}