package com.good.permission.aop;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.good.permission.PermissionRequestFragment;
import com.good.permission.annotation.PermissionCancel;
import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * aop类，处理添加注解的方法
 */
@Aspect
public class PermissionAspect {

    /**
     * 注解切入点
     * @param permission 要执行哪个注解类
     */
    @Pointcut("execution(@com.good.permission.annotation.PermissionNeed * *(..)) && @annotation(permission)")
    public void requestPermission(PermissionNeed permission){

    }

    /**
     * 具体处理的逻辑
     */
    @Around("requestPermission(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final PermissionNeed permission){
        //得到当前上下文对象
        final Object obj = joinPoint.getThis();
        Context context = (Context) obj;
        //开始权限申请
        PermissionRequestFragment.startRequest((FragmentActivity) context, permission.requestCode(), permission.value(),
                new IPermission() {
                    @Override
                    public void onPermissionGranted() {
                        //权限全部被授予 ，执行后面的方法
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionCancel() {
                        PermissionUtil.invokePermissionAnnotation(obj, PermissionCancel.class,permission.requestCode());
                    }

                    @Override
                    public void onPermissionDenied() {
                        PermissionUtil.invokePermissionAnnotation(obj, PermissionDenied.class,permission.requestCode());
                    }
                });

    }

}
