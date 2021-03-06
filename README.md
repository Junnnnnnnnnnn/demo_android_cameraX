# ๐[Android] Jetpack cameraX demo

## โ๏ธ Study

- https://yotdark.tistory.com/39
- [CameraX ์ฌ์ฉํ์ฌ ์ฌ์ง ์ดฌ์ ๋ฐ ์ ์ฅ(1)](https://github.com/Junnnnnnnnnnn/android_study/blob/master/Jetpack_cameraX/CameraX%20%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC%20%EC%82%AC%EC%A7%84%20%EC%B4%AC%EC%98%81%20%EB%B0%8F%20%EC%A0%80%EC%9E%A5%20(1).md)
- [CameraX ์ฌ์ฉํ์ฌ ์ฌ์ง ์ดฌ์ ๋ฐ ์ ์ฅ(2)](https://github.com/Junnnnnnnnnnn/android_study/blob/master/Jetpack_cameraX/CameraX%20%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC%20%EC%82%AC%EC%A7%84%20%EC%B4%AC%EC%98%81%20%EB%B0%8F%20%EC%A0%80%EC%9E%A5%20(2).md)
- [CameraX ์ฌ์ฉํ์ฌ ์ฌ์ง ์ดฌ์ ๋ฐ ์ ์ฅ(3)](https://github.com/Junnnnnnnnnnn/android_study/blob/master/Jetpack_cameraX/CameraX%20%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC%20%EC%82%AC%EC%A7%84%20%EC%B4%AC%EC%98%81%20%EB%B0%8F%20%EC%A0%80%EC%9E%A5%20(3).md)

## ๐ Setting

- CompileSdk = 30
- Minsdk = 24
- TargetSdk = 30

## ๐คจ Why

- MediaStore.ACTION_IMAGE_CAPTURE ๋ฅผ ์ฌ์ฉํ๋ค ์นด๋ฉ๋ผ ์ปค์คํ์ด ํ์ํด ๊ฐ๋ฐ
- ๊ธฐ์กด ์ฌ์ง ์ดํ์ ํตํด ์ฌ์ง์ ์ฐ์ผ๋ฉด ๋ฏธ๋ฆฌ๋ณด๊ธฐ๊ฐ ๋์ค๊ณ  ์ ์ฅ ์ทจ์๋ฅผ ๋ฌป๋๋ฐ ์ด ๊ธฐ๋ฅ์ด ๋ถํ์ํจ

## ๐ Try 

- Jetpack cameraX ์ฌ์ฉํด๋ณด๊ธฐ
- ํ๋ฉด ์ ํ์ Activity ์ฌ์คํ ๋ฐฉ์ง
- ํ๋ฉด ๋น์จ ํฝ์ค ํด๋ณด๊ธฐ (19:6)

## โ๏ธ Gradle ์ค์ 

```
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
  implementation "androidx.camera:camera-core:1.0.2"
}
```

## โ๏ธ manifest ์ค์ 

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

  - ๊ถํ ์ค์ 

    ```xml
    <uses-feature android:name="android.hardware.camera.any" />
    <uses-permission android:name="android.permission.CAMERA" />
    ```

  - ์ธ๋ก ๊ณ ์  ์์ฑ ์ถ๊ฐ

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

  - ์ธ๋ถ์ ์ฅ์ path ์ถ๊ฐ

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

## โ๏ธ ์ธ๋ถ ์ ์ฅ์ path ์ค์ ํ๊ธฐ

- res/xml/file_paths.xml

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <paths>
      <external-path name="my_images" path="Android/data/com.yourpath.example/files/Pictures" />
  </paths>
  ```

## โ๏ธ ๋ฏธ๋ฆฌ๋ณด๊ธฐ View ์ ์ฌ์ง ์ดฌ์ View๋ฅผ ๋ด์ Layout ๊ตฌ์ฑ

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

## โ๏ธ CameraActivity ํด๋์ค ์์ฑ

### โ๏ธ onCreate ์ ํ์ ๊ถํ ํ์ธ

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
      Toast.makeText(this@MainActivity, "์นด๋ฉ๋ผ ๊ถํ์ด ์์ต๋๋ค",Toast.LENGTH_SHORT).show()
      finish()
    }
  }
}
```

### โ๏ธ startCamera ํจ์ ์์ฑ

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
                imageCapture = ImageCapture.Builder().apply {
		    setTargetAspectRatio(AspectRatio.RATIO_16_9)
		}.build()

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
                    Log.e(TAG,"๋ฐ์ธ๋ฉ ์คํจ",e)
                }
            }, ContextCompat.getMainExecutor(this@MainActivity))
    }

```

- ProcessCameraProvider ๋ฆฌ์ค๋ ๋ฑ๋กํ๊ธฐ

  ```kotlin
  val cameraProviderFuture = ProcessCameraProvider.getInstance(this@MainActivity)
      cameraProviderFuture.addListener(/*๋ฆฌ์ค๋ ๋ฑ๋ก*/)
  ```

- imageAnalysis ์ด๊ธฐํ ํ๊ธฐ

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

- preview ์ด๊ธฐํ

  ```kotlin
  val preview = Preview.Builder()
  		.build()
  		.also {
  		  it.setSurfaceProvider(viewFinder.surfaceProvider)
  		}
  ```

- imageCapture ์ด๊ธฐํ

  ```kotlin
  imageCapture = ImageCapture.Builder()
  		.build()
  ```

- cameraSelector ์ค์ 

  ```kotlin
  val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
  ```

- Provider lifecycle ๋ฐ์ธ๋ฉ ํ๊ธฐ

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
  	Log.e(TAG,"๋ฐ์ธ๋ฉ ์คํจ",e)
  }
  ```

- excutor ๋ฑ๋ก

  ```kotlin
  ContextCompat.getMainExecutor(this@MainActivity)
  ```

### โ๏ธ takePhoto ์์ฑ

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

                    Toast.makeText(this@MainActivity,"์ ์ฅ๋์์ต๋๋ค",Toast.LENGTH_SHORT).show()
                    Log.d(TAG,"์ฌ์ง์ด ์ ์ ์ดฌ์ ๋ฌ์ต๋๋ค. $saveUri")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG,"์ดฌ์์ ์คํจ ํ์ต๋๋ค",exception)
                }

            }
        )
    }

```

- ์ฌ์ง ์ดฌ์ ์ค๋น ํ๊ธฐ

  ```kotlin
  val imageCapture = imageCapture ?: return
  val photoFile = File(
  		outputDirectory,
  		SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
  		.format(System.currentTimeMillis()) + ".png"
  )
  val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
  ```

- ์ฌ์ง ์ฐ๊ณ  ์ฝ๋ฐฑ ๋ฐ๊ธฐ

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
  
              Toast.makeText(this@MainActivity,"์ ์ฅ๋์์ต๋๋ค",Toast.LENGTH_SHORT).show()
              Log.d(TAG,"์ฌ์ง์ด ์ ์ ์ดฌ์ ๋ฌ์ต๋๋ค. $saveUri")
          }
  
          override fun onError(exception: ImageCaptureException) {
              Log.e(TAG,"์ดฌ์์ ์คํจ ํ์ต๋๋ค",exception)
          }
  
      }
  )
  ```

## โ๏ธ ์นด๋ฉ๋ผ view๋ ํฝ์ค ๋๊ณ  UI๋ง ๊ฐ๋ก ์ธ๋ก ํ์  ์ปจํธ๋กค ํ๊ธฐ

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
                            Log.d(TAG,"๊ฐ๋ก ::: $orientation")
                            animFlag = 1
                            setRotate(cameraButton.id,"Button",90f, 300)
                        }
                        Surface.ROTATION_90
                    }
                    else -> {
                        if(animFlag == 1){
                            Log.d(TAG,"์ธ๋ก ::: $orientation")
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

  

