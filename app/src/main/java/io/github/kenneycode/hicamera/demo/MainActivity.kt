package io.github.kenneycode.hicamera.demo

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.opengl.GLES30
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import io.github.kenneycode.funrenderer.common.Keys
import io.github.kenneycode.funrenderer.common.RenderChain
import io.github.kenneycode.funrenderer.io.Texture
import io.github.kenneycode.funrenderer.renderer.CropRenderer
import io.github.kenneycode.funrenderer.renderer.OES2RGBARenderer
import io.github.kenneycode.funrenderer.renderer.ScreenRenderer
import io.github.kenneycode.hicamera.CameraWrapper
import io.github.kenneycode.hicamera.GLCameraView
import io.github.kenneycode.hicamera.Size
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            runDemo()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        if (requestCode == 1) {
            grantResults?.let {
                if (it[0] == PackageManager.PERMISSION_GRANTED) {
                    runDemo()
                }
            }
        }
    }

    private fun runDemo() {
        val cameraWrapper = CameraWrapper()
        cameraView.bindCamera(cameraWrapper)
        cameraView.renderCallback = object : GLCameraView.RenderCallback {

            private val oes2RGBARenderer = OES2RGBARenderer()
            private val cropRenderer = CropRenderer()
            private val effectRenderer = EffectRenderer()
            private val screenRenderer = ScreenRenderer()
            private lateinit var renderChain: RenderChain

            override fun onInit() {
                renderChain = RenderChain.create()
                        .addRenderer(oes2RGBARenderer)
                        .addRenderer(cropRenderer)
                        .addRenderer(effectRenderer)
                        .addRenderer(screenRenderer)
                renderChain.init()
            }

            override fun onRenderFrame(oesTexture: Int, stMatrix: FloatArray, cameraPreviewSize: Size, surfaceSize: Size) {
                GLES30.glClearColor(0f, 0f, 0f, 1f)
                GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
                val input = Texture(oesTexture, cameraPreviewSize.height, cameraPreviewSize.width, false)
                val data = mutableMapOf<String, Any>()
                data[Keys.ST_MATRIX] = stMatrix
                data[Keys.CROP_RATIO] = surfaceSize.width.toFloat() / surfaceSize.height
                data[Keys.SURFACE_WIDTH] = surfaceSize.width
                data[Keys.SURFACE_HEIGHT] = surfaceSize.height
                renderChain.render(input, data)

                Log.e("", "")
            }

            override fun onRelease() {
                renderChain.release()
            }

        }
    }

}
