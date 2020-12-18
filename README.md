# PermissionGood
AOP动态权限申请
### 添加依赖
```
	dependencies {
	        implementation 'com.github.wo5813288:PermissionGood:Tag'
	}
  ```
  ### 添加
  ```
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  ### 使用流程
  在module中添加插件
  ```
  apply plugin: 'android-aspectjx'
  ```
  ### 使用示例
  ```
  
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
  ```
  
