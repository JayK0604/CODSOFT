package com.example.flashlightapp

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (!hasFlash) {
            Toast.makeText(this, "Your device does not have a camera flash.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to access the camera.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val flashlightToggle = findViewById<ToggleButton>(R.id.flashlightToggle)
        flashlightToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                turnOnFlashlight()
            } else {
                turnOffFlashlight()
            }
        }
    }

    private fun turnOnFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to turn on the flashlight.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun turnOffFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to turn off the flashlight.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        // Turn off the flashlight when the app is paused
        turnOffFlashlight()
    }
}
