# 🏃[Android] Jetpack cameraX demo

-------

## 🤨 Why

- MediaStore.ACTION_IMAGE_CAPTURE 를 사용하다 카메라 커스텀이 필요해 개발
- 기존 사진 어플을 통해 사진을 찍으면 미리보기가 나오고 저장 취소를 묻는데 이 기능이 불필요함

-------

## 🙋 Try 

- Jetpack cameraX 사용해보기
- 화면 전환시 Activity 재실행 방지
- 화면 비율 픽스 해보기 (19:6)

------

## ✏️ Gradle 설정

```json
plugins {
	  ...
  	id 'kotlin-android-extensions'
}

apply plugin: 'kotlin-android-extensions'

compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}

dependencies {
  implementation "androidx.camera:camera-camera2:1.0.1"
  implementation "androidx.camera:camera-lifecycle:1.0.1"
  implementation "androidx.camera:camera-view:1.0.0-alpha27"
}
```

---------

## ✏️ 미리보기 View 와 사진 촬영 View를 담은 Layout 구성

- activity_main,xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/camera_capture_button"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginBottom="50dp"
        android:elevation="2dp"
        android:scaleType="fitCenter"
        android:text="Take Photo"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent" />

    <androidx.camera.view.PreviewView
        android:id="@+id/viewFinder"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

-----

## ✏️ Main 클래스 생성

- onCreate 시 필수 권한 확인

  ```kotlin
  override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_main)
          if(allPermissionsGranted()){
              startCamera()
          }else{
              ActivityCompat.requestPermissions(
                  this@MainActivity,
                  REQUIRED_PERMISSIONS,
                  REQUEST_CODE_PERMISSIONS
              )
          }
      }
  
  private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if(requestCode == REQUEST_CODE_PERMISSIONS){
      if(allPermissionsGranted()){
        startCamera()
      }else{
        Toast.makeText(this@MainActivity, "카메라 권한이 없습니다",Toast.LENGTH_SHORT).show()
        finish()
      }
    }
  }
  ```

  

