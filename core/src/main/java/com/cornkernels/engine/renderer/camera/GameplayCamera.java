package com.cornkernels.engine.renderer.camera;

import com.badlogic.gdx.math.Vector2;

public interface GameplayCamera extends CameraController {
    Vector2 screenToWorld(float screenX, float screenY);
}
