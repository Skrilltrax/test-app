package com.example.visitor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import java.io.File
import java.io.FileOutputStream
import java.sql.Timestamp


class CameraActivity : AppCompatActivity() {

    private lateinit var cameraKitView: CameraKitView
    private lateinit var switchCamera: ImageView
    private lateinit var captureImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_camera)
        findViews()
        cameraKitView.requestPermissions(this)
    }

    private fun findViews() {
        cameraKitView = findViewById(R.id.camera)
        captureImage = findViewById(R.id.capture)
        switchCamera = findViewById(R.id.switch_camera)

        switchCamera.setOnClickListener {
            if (cameraKitView.facing == CameraKit.FACING_BACK) {
                cameraKitView.facing = CameraKit.FACING_FRONT
            } else {
                cameraKitView.facing = CameraKit.FACING_BACK
            }
        }

        captureImage.setOnClickListener {

            Log.d("img", "0")

            cameraKitView.captureImage { _, p1 ->
                val savedPhoto = File(Environment.getExternalStorageDirectory(), "photo${Timestamp(System.currentTimeMillis())}.jpg")
                try {
                    val outputStream = FileOutputStream(savedPhoto.path)
                    outputStream.write(p1)
                    outputStream.close()
                } catch (e: java.io.IOException) {
                    e.printStackTrace()
                }
                val data = Intent().apply {
                    putExtra("image", savedPhoto.path)
                }
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        cameraKitView.onStart()
    }

    override fun onResume() {
        super.onResume()
        cameraKitView.onResume()
    }

    override fun onPause() {
        cameraKitView.onPause()
        super.onPause()
    }

    override fun onStop() {
        cameraKitView.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}