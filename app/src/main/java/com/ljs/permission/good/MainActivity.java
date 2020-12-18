package com.ljs.permission.good;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.Manifest.permission;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.good.permission.annotation.PermissionCancel;
import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @PermissionNeed(value = {permission.ACCESS_FINE_LOCATION},requestCode = 1)
    public void singlePermission(View view) {
        Log.e("wqs","允许了单个权限");
    }

    @PermissionNeed(value = {permission.WRITE_EXTERNAL_STORAGE,permission.CAMERA},requestCode = 2)
    public void multiplePermission(View view) {
        Log.e("wqs","全部允许了多个权限");
    }
    //永久拒绝了
    @PermissionDenied
    public void permissionDenied(int requestCode){
        Log.e("wqs", "永久拒绝了: ==="+requestCode );
    }

    //拒绝
    @PermissionCancel
    public void permissionCancel(int requestCode){
        Log.e("wqs", "取消授予权限: ==="+requestCode );
    }

}