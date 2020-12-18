package com.good.permission.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PermissionUtil {

    /**
     * 是否是6.0以上版本
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    /**
     * 判断当前传入的单个 权限是否已经被授予
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context,String permission){
        return ActivityCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查传去的权限数组有没有被授予
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(Context context,String... permissions){
        for (String permission : permissions) {
            if(permission.equals(Manifest.permission.REQUEST_INSTALL_PACKAGES)){
                continue;
            }
            if(!hasPermission(context,permission)){
                //如果有一个返回false，则认为没有被全部授予
                return false;
            }
        }
        return true;
    }

    /**
     * 用户申请权限以后的回调结果，检查有没有被全部授予
     * @return
     */
    public static boolean verifyPermission(int[] grantResults){
        for (int i: grantResults) {
            if(i!=PackageManager.PERMISSION_GRANTED){
                //有期中一项没有被授予
                return false;
            }
        }
        return true;
    }

    /**
     * 展示申请权限的原因
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity,String... permissions){
        for (String permission:permissions){
            //如果这里返回true 则说明用户拒绝了权限申请，但是没有点击永久拒绝，需要展示原因
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                return true;
            }
        }
        return false;
    }

    public static boolean isHasInstallPermission(Context context){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //大于8.0系统
           return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 获取被注解的方法，并执行
     * @param object
     * @param requestCode
     */
    public static void invokePermissionAnnotation(Object object, Class<? extends Annotation> annotationClass, int requestCode){
        //获取类对象
        Class<?> cls = object.getClass();
        //获取到类中更多所有方法
        Method[] declaredMethods = cls.getDeclaredMethods();
        //过滤出被注定注解类注解过的方法
        for (Method method:declaredMethods){
            if(method.isAnnotationPresent(annotationClass)){
                int length = method.getParameterTypes().length;
                if(length!=1){
                    //该方法是否只有一个参数
                   throw new RuntimeException("有且只能有一个参数");
                }
                method.setAccessible(true);
                try {
                    method.invoke(object,requestCode);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
