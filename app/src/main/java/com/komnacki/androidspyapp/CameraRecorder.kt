//package com.komnacki.androidspyapp
//
//import android.R
//import android.content.Intent
//import android.graphics.Camera
//import android.os.Bundle
//import android.view.SurfaceHolder
//import android.view.SurfaceView
//
//
//class CameraRecorder : SurfaceHolder.Callback {
//    private val TAG = "Recorder"
//    var mSurfaceView: SurfaceView? = null
//    var mSurfaceHolder: SurfaceHolder? = null
//    var mCamera: Camera? = null
//    var mPreviewRunning = false
//
//    /** Called when the activity is first created.  */
//    fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.main)
//        mSurfaceView = findViewById(R.id.surfaceView1) as SurfaceView?
//        mSurfaceHolder = mSurfaceView!!.holder
//        mSurfaceHolder.addCallback(this)
//        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
//        val btnStart: Button = findViewById(R.id.StartService) as Button
//        btnStart.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                val intent = Intent(this@CameraRecorder, RecorderService::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startService(intent)
//                finish()
//            }
//        })
//        val btnStop: Button = findViewById(R.id.StopService) as Button
//        btnStop.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                stopService(Intent(this@CameraRecorder, RecorderService::class.java))
//            }
//        })
//    }
//
//    fun surfaceCreated(holder: SurfaceHolder?) {}
//
//    fun surfaceChanged(
//        holder: SurfaceHolder?,
//        format: Int,
//        width: Int,
//        height: Int
//    ) {
//    }
//
//    fun surfaceDestroyed(holder: SurfaceHolder?) {
//        // TODO Auto-generated method stub
//    }
//}