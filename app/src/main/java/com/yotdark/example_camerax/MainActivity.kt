package com.yotdark.example_camerax

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var animFlag: Int = 0
    private val cameraButton: Button by lazy {
        findViewById(R.id.camera_capture_button)
    }
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val orientationEventListener by lazy {
        object : OrientationEventListener(this@MainActivity) {
            override fun onOrientationChanged(orientation: Int) {
                if(orientation == -1){
                    return
                }
//                Log.d(TAG,"onOrientationChanged 나옴?! $orientation")
                val rotation = when (orientation) {
                    in 45 until 135 -> {
                        Surface.ROTATION_270
                    }
                    in 135 until 225 -> Surface.ROTATION_180
                    in 250 until 315 -> {
                        if(orientation > 310){
                            return
                        }
                        if(animFlag == 0){
                            Log.d(TAG,"가로 ::: $orientation")
                            animFlag = 1
                            setRotate(cameraButton.id,"Button",90f, 300)
                        }
                        Surface.ROTATION_90
                    }
                    else -> {
                        if(orientation > 240){
                            return
                        }
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

        camera_capture_button.setOnClickListener {
            Log.d(TAG,"클릭했")
            takePhoto()

        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

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
    private fun takePhoto(){
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
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

    private fun getOutputDirectory(): File {
        Log.d(LOG_TAG, "DIRECTORY ::: ${Environment.DIRECTORY_PICTURES}")
        return getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



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


    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }



    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val LOG_TAG = "============================>"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}