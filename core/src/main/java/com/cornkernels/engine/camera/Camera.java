package com.cornkernels.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends OrthographicCamera {

    public Camera() {
        super();
    }

    public Camera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public void updateCamera(float delta, float roomWidth, float roomHeight) {

    }
}
