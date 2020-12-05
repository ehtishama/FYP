package com.fypcuiatk.feeclearanceapp.Helpers

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception

private const val TAG = "CameraPreview"

class CameraPreview(context: Context, private val camera: Camera) : SurfaceView(context),
    SurfaceHolder.Callback {

    private val mHolder = holder.apply {
        addCallback(this@CameraPreview)
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

    }

    // The surface has been created, tell the camera to draw the preview
    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            camera.setPreviewDisplay(holder)
            camera.setDisplayOrientation(90)

            camera.startPreview()
        }catch (e: Exception)
        {
            Log.d(TAG, "surfaceCreated: Error setting camera preview: ${e.message}")
        }
    }

    // surface changed e.g. rotated
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        if(mHolder.surface == null)
            return

        try {
            camera.stopPreview()
        }catch (e: Exception)
        {}


        try {
            camera.setPreviewDisplay(holder)
            camera.setDisplayOrientation(90)
            camera.startPreview()
        }catch (e: Exception)
        {
            Log.d(TAG, "surfaceCreated: Error setting camera preview: ${e.message}")
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }
}