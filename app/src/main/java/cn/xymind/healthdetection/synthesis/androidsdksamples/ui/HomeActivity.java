package cn.xymind.healthdetection.synthesis.androidsdksamples.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.blankj.utilcode.util.ToastUtils;
import java.util.ArrayList;

import cn.xymind.healthdetection.synthesis.androidsdksamples.R;
import cn.xymind.healthdetection.synthesis.androidsdksamples.app.DemoApp;


public class HomeActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
    };

    private static final int APPLY_PERMISSION = 1000;

    private EditText httpEt;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        InitView();
        InitData();

        // 显示loading加载框
        showLoadingDialog();
    }

    private void InitView() {
        httpEt = this.findViewById(R.id.httpEt);


        findViewById(R.id.btn_finally).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String httpUrl = httpEt.getText().toString().trim();
                if (!TextUtils.isEmpty(httpUrl)) {
                    if (httpUrl.contains("http")) {
                        if (httpUrl.substring(httpUrl.length() - 1).equals("/")) {
                            DemoApp.config.setServer(httpUrl, "YOUR_URL", false);
                        } else {
                            DemoApp.config.setServer(httpUrl + "/", "YOUR_URL", false);
                        }
                    } else {
                        if (httpUrl.substring(httpUrl.length() - 1).equals("/")) {
                            DemoApp.config.setServer("http://" + httpUrl, "YOUR_URL", false);
                        } else {
                            DemoApp.config.setServer("http://" + httpUrl + "/", "YOUR_URL", false);
                        }
                    }
                }
                Intent intent = new Intent(HomeActivity.this, SwitchActivity.class);

                startActivity(intent);
            }
        });
    }

    private void InitData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                ToastUtils.showLong("ANDROID VERSION R OR ABOVE ,HAVE MANAGE_EXTERNAL_STORAGE GRANTED!");
            } else {
                ToastUtils.showLong("ANDROID VERSION R OR ABOVE ,NO HAVE MANAGE_EXTERNAL_STORAGE GRANTED!");
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1001);
            }
        }
        checkAndRequestPermissions(PERMISSIONS, APPLY_PERMISSION);


    }

    protected Boolean checkAndRequestPermissions(String[] permissions, int requestCode) {
        ArrayList<String> requestPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermission.add(permission);
            }
        }

        if (requestPermission.size() == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(this, requestPermission.toArray(new String[requestPermission.size()]), requestCode);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("权限被禁止。\n请在【设置-应用信息-权限】中重新授权").setPositiveButton("确定", (dialog12, which) -> {
                    dialog12.dismiss();
                }).setNegativeButton("取消", (dialog1, which) -> {
                    dialog1.dismiss();
                }).create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            }
        }
    }

    private void showLoadingDialog() {
        loadingDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        loadingDialog.setContentView(R.layout.loading_dialog); // 假设你有一个loading_dialog.xml布局文件
        loadingDialog.setCancelable(false); // 点击所有区域无法取消

        // 设置全屏
        Window window = loadingDialog.getWindow();
        if (window != null) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        loadingDialog.show();

        // 5秒后自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        }, 5000);
    }
}
