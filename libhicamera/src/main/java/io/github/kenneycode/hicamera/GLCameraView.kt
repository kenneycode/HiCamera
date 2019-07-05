package io.github.kenneycode.hicamera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import io.github.kenneycode.glkit.GLTextureView
import io.github.kenneycode.glkit.GLUtils

/**
 *
 *      Coded by kenney
 *
 *      http://www.github.com/kenneycode
 *
 **/

class GLCameraView : GLTextureView, GLTextureView.Callback, SurfaceTexture.OnFrameAvailableListener {

    init {
        super.callback = this
    }

    private var oesTexture = 0
    private var st: SurfaceTexture? = null
    private lateinit var camera: ICamera
    private var pendingInitTask: Runnable? = null
    var renderCallback: RenderCallback? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bindCamera(camera: ICamera) {
        post {
            this.camera = camera
            if (glThread != null) {
                glThread?.post(Runnable {
                    init()
                })
            } else {
                pendingInitTask = Runnable {
                    init()
                }
            }
        }
    }

    private fun init() {
        renderCallback?.onInit()
        oesTexture = GLUtils.createOESTexture()
        st = SurfaceTexture(oesTexture)
        st?.setOnFrameAvailableListener(this)
        camera.setSurfaceTexture(st!!)
        camera.start()
    }

    override fun onInit() {
        pendingInitTask?.run()
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        surfaceTexture?.updateTexImage()
        requestRender()
    }

    override fun onRender(width: Int, height: Int) {
        val stMatrix = FloatArray(16)
        st?.getTransformMatrix(stMatrix)
        renderCallback?.onRenderFrame(oesTexture, stMatrix, camera.getPreviewSize(), Size(width, height))
    }

    override fun onRelease() {
        camera.stop()
        st?.release()
        GLUtils.deleteTexture(oesTexture)
        renderCallback?.onRelease()
    }

    interface RenderCallback {

        fun onInit()
        fun onRenderFrame(oesTexture: Int, stMatrix: FloatArray, cameraPreviewSize: Size, surfaceSize: Size)
        fun onRelease()
    }

}