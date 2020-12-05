package com.fypcuiatk.feeclearanceapp

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.fypcuiatk.feeclearanceapp.Helpers.CameraPreview
import java.io.File
import java.io.FileOutputStream

private const val TAG = "CameraActivity"

class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null

    private var mCameraPreview: CameraPreview? = null

    private lateinit var captureImageBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //
        captureImageBtn = findViewById(R.id.captureBtn)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add a receipt"

        mCamera = getCameraInstance()
        mCamera?.parameters?.setRotation(90)

        mCameraPreview = mCamera?.let {
            CameraPreview(this, it)
        }

        mCameraPreview?.also {
            val preview = findViewById<FrameLayout>(R.id.liveViewContainer)
            val size = mCamera?.parameters?.previewSize
            preview.layoutParams = ConstraintLayout.LayoutParams(size!!.width, size!!.height)
            preview.addView(it)
        }

        captureImageBtn.setOnClickListener(View.OnClickListener {

            mCamera?.takePicture(null, null, Camera.PictureCallback { data, _ ->

                val f = File(getExternalFilesDir(null), "image.jpeg")
                Log.d(TAG, "onCreate: location to store image is : ${f.absoluteFile}")

                try {
                    val os: FileOutputStream = FileOutputStream(f);
                    os.write(data);
                    os.close();
                } catch (e: Exception) {
                    Log.d(TAG, "onCreate: Error while storing image: ${e.message}")
                }

            })

        })



    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus)
            hideSystemUI()
    }

    /** A safe way to get an instance of the Camera object. */
    private fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "getCameraInstance: Camera not available: ${e.message}.")
        }
        return c; // returns null if camera is unavailable
    }

    private fun releaseCamera() {
        mCamera?.release()
        mCamera = null
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}