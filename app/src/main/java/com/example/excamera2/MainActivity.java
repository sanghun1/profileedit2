package com.example.excamera2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    RelativeLayout relativeLayout;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한 체크
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("카메라 권한을 거부하셨습니다.")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

        getWindow().setFormat(PixelFormat.UNKNOWN);

        relativeLayout = findViewById(R.id.main_layout);
        Button buttonStopCameraPreview = (Button)findViewById(R.id.stopcamerapreview);

        findViewById(R.id.cam_btn).setOnClickListener(this);
        findViewById(R.id.img_btn).setOnClickListener(this);

        buttonStopCameraPreview.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(camera != null && previewing){
                    camera.stopPreview();
                    camera.release();
                    camera = null;

                    previewing = false;
                }
            }});
    }

    @Override
    public void onClick(View view) {

        ObjectAnimator anim = ObjectAnimator.ofFloat(R.id.main_layout, "translationX", 500f);
        anim.setRepeatCount(1);
        anim.setDuration(500);

        switch (view.getId()){
            case R.id.img_btn:
                relativeLayout.animate().translationX(0f)
                        .setDuration(250)
                        .start();

                findViewById(R.id.trans_border).animate().translationX(0f)
                        .setDuration(250)
                        .start();
                break;

            case R.id.cam_btn:
                relativeLayout.animate().translationX(-1080f)
                        .setDuration(250)
                        .start();

                findViewById(R.id.trans_border).animate().translationX(540f)
                        .setDuration(250)
                        .start();
                break;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Toast.makeText(MainActivity.this, "surfaceCreated", Toast.LENGTH_LONG).show();
        camera = Camera.open();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null){

            camera.setDisplayOrientation(rotate());

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    public int rotate(){
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0;
                break;

            case Surface.ROTATION_90: degrees = 90;
                break;

            case Surface.ROTATION_180: degrees = 180;
                break;

            case Surface.ROTATION_270: degrees = 270;
                break;
        }

        int result  = (90 - degrees + 360) % 360;

        return result;
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "권한이 허용됨", Toast.LENGTH_SHORT).show();
            surfaceView = findViewById(R.id.surfaceview);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(MainActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            camera = Camera.open();

//            Camera.Parameters params = camera.getParameters();
//            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//            camera.setParameters(params);

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            camera.setDisplayOrientation(rotate());
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "권한이 거부됨\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

}