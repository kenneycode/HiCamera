package io.github.kenneycode.hicamera

import android.graphics.SurfaceTexture

/**
 *
 *      Coded by kenney
 *
 *      http://www.github.com/kenneycode
 *
 **/

interface ICamera {

    fun start()
    fun stop()
    fun getPreviewSize(): Size
    fun setSurfaceTexture(surfaceTexture: SurfaceTexture)

}