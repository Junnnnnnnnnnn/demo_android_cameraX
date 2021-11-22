package com.yotdark.example_camerax

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private val button: Button by lazy {
        findViewById(R.id.button1)
    }

    private val imageView: ImageView by lazy {
        findViewById(R.id.imageView1)
    }

    private lateinit var cameraActivityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
                intent.putExtra("extension","png")
                intent.putExtra("ratio",AspectRatio.RATIO_16_9)
            cameraActivityResult.launch(intent)
        }

        mainCameraActivityResult()
    }

    private fun mainCameraActivityResult(){

        cameraActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == 200){
                it.data?.let { intent ->
                    intent.getStringExtra("path")?.let { path ->
                        imageView.run {
                            setImageBitmap(urlToBitmap(File(path).absolutePath))
                        }
                    }
                }
            }
        }
    }



    private fun urlToBitmap(url: String) = BitmapFactory.decodeFile(url)


}