package com.good.permission;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.good.permission.aop.IPermission;
import com.good.permission.util.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PermissionRequestFragment extends Fragment implements Runnable {

    public static final String REQUEST_PERMISSION = "request_permission";
    public static final String REQUEST_CODE = "request_code";
    private static FragmentManager fragmentManager;
    private static IPermission mIPermission;

    /**
     * 初始化一个fragment
     *
     * @param activity
     * @param requestCOde
     * @param permissions
     * @param iPermission
     * @return
     */
    public static void startRequest(FragmentActivity activity,
                                                         int requestCOde, String[] permissions, IPermission iPermission) {
        mIPermission = iPermission;
        PermissionRequestFragment fragment = new PermissionRequestFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(REQUEST_PERMISSION, permissions);
        bundle.putInt(REQUEST_CODE, requestCOde);
        fragment.setArguments(bundle);
        //添加一个fragment
        fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, REQUEST_CODE);
        fragmentTransaction.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            int requestCode = arguments.getInt(REQUEST_CODE);
            String[] permissions = arguments.getStringArray(REQUEST_PERMISSION);
            List<String> strings = Arrays.asList(permissions);
            if(strings.contains(REQUEST_PERMISSION)&&!PermissionUtil.isOverMarshmallow()){
                strings.remove(Manifest.permission.REQUEST_INSTALL_PACKAGES);
                requestPermission(requestCode, (String[]) strings.toArray());
            }else{
                requestPermission(requestCode, permissions);
            }


        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 开始申请权限
     * @param requestCode
     * @param permissions
     */
    private void requestPermission(int requestCode, String[] permissions) {
        if(PermissionUtil.hasPermission(requireActivity(),permissions)|| Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            //如果权限都已经被授予了
            mIPermission.onPermissionGranted();
            removeThisPage();
        }else{
            //如果有没有授予的权限
            requestPermissions(permissions,requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtil.hasPermission(requireActivity(),permissions)){
            //如果权限全部被用户授予了
            mIPermission.onPermissionGranted();
        }else if(PermissionUtil.shouldShowRequestPermissionRationale(requireActivity(),permissions)){
            //有权限被拒绝
            mIPermission.onPermissionCancel();
        }else{
            //权限被永久禁止了
            mIPermission.onPermissionDenied();
        }
        removeThisPage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==getArguments().getInt(REQUEST_CODE)){
            // 需要延迟执行，不然有些华为机型授权了但是获取不到权限
            new Handler(Looper.getMainLooper()).postDelayed(this, 300);
        }
    }

    /**
     * 结束当前权限页面
     */
    private void removeThisPage(){
        fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(REQUEST_CODE);
        if(fragmentByTag!=null) {
            fragmentTransaction.remove(fragmentByTag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void run() {
        // 请求其他危险权限
        requestPermission(getArguments().getInt(REQUEST_CODE),getArguments().getStringArray(REQUEST_PERMISSION));
    }
}
