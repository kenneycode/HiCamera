package io.github.kenneycode.hicamera

import android.graphics.SurfaceTexture

interface ICamera {

    fun start()
    fun stop()
    fun getPreviewSize(): Size
    fun setSurfaceTexture(surfaceTexture: SurfaceTexture)

}