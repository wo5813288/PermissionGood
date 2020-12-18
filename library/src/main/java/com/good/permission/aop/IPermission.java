package com.good.permission.aop;

public interface IPermission {
    /**
     * 权限全部授予成功后的回调
     */
    void onPermissionGranted();

    /**
     * 权限被拒绝
     */
    void onPermissionCancel();

    /**
     * 权限被永久拒绝
     */
    void onPermissionDenied();
}
