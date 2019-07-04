package io.github.kenneycode.hicamera

import android.graphics.SurfaceTexture
import android.hardware.Camera


class CameraWrapper: ICamera {

    private lateinit var camera: Camera
    private lateinit var surfaceTexture: SurfaceTexture

    override fun start() {
        val cameraId = getCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
        camera = Camera.open(cameraId)
        setPreviewSize(camera.parameters)
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        camera.setDisplayOrientation(info.orientation)
        camera.setPreviewTexture(surfaceTexture)
        camera.startPreview()
    }

    override fun stop() {
        camera.stopPreview()
        camera.release()
    }


    override fun setSurfaceTexture(surfaceTexture: SurfaceTexture) {
        this.surfaceTexture = surfaceTexture
    }

    override fun getPreviewSize(): Size {
        val size = camera.parameters.previewSize
        val previewSize = Size()
        previewSize.width = size.width
        previewSize.height = size.height
        return previewSize
    }

    private fun getCameraId(facing : Int) : Int {
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == facing) {
                return i
            }
        }
        return -1
    }

    private fun setPreviewSize(parameters: Camera.Parameters) {
        val previewSizeList = mutableListOf<Camera.Size>()
        for (size in parameters.supportedPreviewSizes) {
            previewSizeList.add(size)
        }
        parameters.setPreviewSize(previewSizeList[0].width, previewSizeList[0].height)
    }

}
