package com.cornkernels.engine.renderer.camera;

public interface CameraController {

    void panToRightEdge(float duration, Runnable onComplete);

    void panToLeftEdge(float duration, Runnable onComplete);

    void snapToLeftEdge();

    void snapToRightEdge();

    boolean isPanning();
}
