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

## ✏️ manifest 설정

- AndroidManifest.xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.yotdark.example_camerax">
      <uses-feature android:name="android.hardware.camera.any" />
      <uses-permission android:name="android.permission.CAMERA" />
      <application
          ...
          <activity
              android:name=".MainActivity"
              android:screenOrientation="portrait"
              android:exported="true">
              <intent-filter>
                  <action android:name="android.intent.action.MAIN" />
  
                  <category android:name="android.intent.category.LAUNCHER" />
              </intent-filter>
          </activity>
  
          <provider
              android:name="androidx.core.content.FileProvider"
              android:authorities="com.yotdark.example_camerax"
              android:exported="false"
              android:grantUriPermissions="true">
              <meta-data
                  android:name="android.support.FILE_PROVIDER_PATHS"
                  android:resource="@xml/file_paths" />
          </provider>
      </application>
  
  </manifest>
  ```

  - 권한 설정

    ```xml
    <uses-feature android:name="android.hardware.camera.any" />
    <uses-permission android:name="android.permission.CAMERA" />
    ```

  - 세로 고정 속성 추가

    ```xml
    <activity
    		android:name=".MainActivity"
    		android:screenOrientation="portrait"
    		android:exported="true">
    		<intent-filter>
    				<action android:name="android.intent.action.MAIN" />
    				<category android:name="android.intent.category.LAUNCHER" />
    		</intent-filter>
    </activity>
    ```

  - 외부저장소 path 추가

    ```xml
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="com.yotdark.example_camerax"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>
    ```

------

## ✏️ 외부 저장소 path 설정하기

- res/xml/file_paths.xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <paths>
      <external-path name="my_images" path="Android/data/com.yourpath.example/files/Pictures" />
  </paths>
  ```

--------

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

--------------

### ✏️ onCreate 시 필수 권한 확인

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

------------

### ✏️ startCamera 함수 작성

```kotlin
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this@MainActivity)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build().apply {
                        setAnalyzer(cameraExecutor, { image ->
                            val rotationDegrees = image.imageInfo.rotationDegrees
                            // insert your code here.
                        })
                    }

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder()
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this@MainActivity, 
                        cameraSelector, 
                        preview, 
                        imageCapture, 
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e(TAG,"바인딩 실패",e)
                }
            }, ContextCompat.getMainExecutor(this@MainActivity))
    }

```

- ProcessCameraProvider 리스너 등록하기

  ```kotlin
  val cameraProviderFuture = ProcessCameraProvider.getInstance(this@MainActivity)
      cameraProviderFuture.addListener(/*리스너 등록*/)
  ```

- imageAnalysis 초기화 하기

  ```kotlin
  imageAnalysis = ImageAnalysis.Builder()
  		.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
      .build().apply {
  				setAnalyzer(cameraExecutor, { image ->
  				val rotationDegrees = image.imageInfo.rotationDegrees
  				// insert your code here.
  			})
  }
  ```

- preview 초기화

  ```kotlin
  val preview = Preview.Builder()
  		.build()
  		.also {
  		  it.setSurfaceProvider(viewFinder.surfaceProvider)
  		}
  ```

- imageCapture 초기화

  ```kotlin
  imageCapture = ImageCapture.Builder()
  		.build()
  ```

- cameraSelector 설정

  ```kotlin
  val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
  ```

- Provider lifecycle 바인딩 하기

  ```kotlin
  try {
  		cameraProvider.unbindAll()
  		cameraProvider.bindToLifecycle(
  				this@MainActivity,
  				cameraSelector,
  				preview,
  				imageCapture,
  				imageAnalysis
  	)
  } catch (e: Exception) {
  	Log.e(TAG,"바인딩 실패",e)
  }
  ```

- excutor 등록

  ```kotlin
  ContextCompat.getMainExecutor(this@MainActivity)
  ```

--------

### ✏️ takePhoto 작성

```kotlin
    private fun takePhoto(){
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
                .format(System.currentTimeMillis()) + ".png"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, 
            ContextCompat.getMainExecutor(this@MainActivity),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val saveUri = Uri.fromFile(photoFile)
                    ExifInterface(photoFile.absolutePath).run{
                        getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL).run {
                            Log.d(TAG,"$this")
                        }
                    }

                    Toast.makeText(this@MainActivity,"저장되었습니다",Toast.LENGTH_SHORT).show()
                    Log.d(TAG,"사진이 정상 촬영 됬습니다. $saveUri")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG,"촬영에 실패 했습니다",exception)
                }

            }
        )
    }

```

- 사진 촬영 준비 하기

  ```kotlin
  val imageCapture = imageCapture ?: return
  val photoFile = File(
  		outputDirectory,
  		SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
  		.format(System.currentTimeMillis()) + ".png"
  )
  val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
  ```

- 사진 찍고 콜백 받기

  ```kotlin
  imageCapture.takePicture(
      outputOptions,
      ContextCompat.getMainExecutor(this@MainActivity),
      object: ImageCapture.OnImageSavedCallback{
          override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
              val saveUri = Uri.fromFile(photoFile)
              ExifInterface(photoFile.absolutePath).run{
                  getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL).run {
                      Log.d(TAG,"$this")
                  }
              }
  
              Toast.makeText(this@MainActivity,"저장되었습니다",Toast.LENGTH_SHORT).show()
              Log.d(TAG,"사진이 정상 촬영 됬습니다. $saveUri")
          }
  
          override fun onError(exception: ImageCaptureException) {
              Log.e(TAG,"촬영에 실패 했습니다",exception)
          }
  
      }
  )
  ```

-----

## ✏️ 카메라 view는 픽스 되고 UI만 가로 세로 회전 컨트롤 하기

```kotlin
private val orientationEventListener by lazy {
        object : OrientationEventListener(this@MainActivity) {
            override fun onOrientationChanged(orientation: Int) {
                if(orientation == -1){
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 250 until 315 -> {
                        if(animFlag == 0){
                            Log.d(TAG,"가로 ::: $orientation")
                            animFlag = 1
                            setRotate(cameraButton.id,"Button",90f, 300)
                        }
                        Surface.ROTATION_90
                    }
                    else -> {
                        if(animFlag == 1){
                            Log.d(TAG,"세로 ::: $orientation")
                            animFlag = 0
                            setRotate(cameraButton.id,"Button",-90f, 300)
                        }
                        Surface.ROTATION_0
                    }
                }

                imageAnalysis?.run { targetRotation = rotation }
                imageCapture?.run { targetRotation = rotation }
            }
        }
    }
```

- setRotate()

  ```kotlin
  private fun setRotate(id: Int, type: String, rotate: Float, duration: Long){
      var currentDegree = 0f
      val view = findViewById<Button>(id)
      when(type){
          "Button" -> currentDegree = view.rotation
          "ImageView" -> currentDegree = view.rotation
      }
      ObjectAnimator.ofFloat(view, View.ROTATION, currentDegree, currentDegree + rotate)
          .setDuration(duration)
          .start()
  
  }
  ```

  

