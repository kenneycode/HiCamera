## HiCamera

HiCamera是一个Android上的`OpenGL`特效相机库，可以方便地实现用`OpenGL`渲染相机特效。

使用方法：

- 创建`GLCameraView`用于显示相机画面，可直接写在`xml`中。

- 将`GLCameraView`与相机绑定，绑定的相机类需实现`ICamera`接口，用于控制相机的启动、停止、设置`SurfaceTexture`及获取设置的预览大小。

- 设置`GLCameraView`的`renderCallback`用于渲染相机帧。

本库依赖于我的另一个库[GLKit](https://github.com/kenneycode/GLKit)用于提供`GL`环境，本工程中包括了`demo`，可参考，渲染时用了我的另一个库[FunRenderer](https://github.com/kenneycode/FunRenderer)，但并不依赖于它，开发者可自行开发渲染。

