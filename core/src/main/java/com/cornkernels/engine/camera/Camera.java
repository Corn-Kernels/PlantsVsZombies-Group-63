package com.cornkernels.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Camera {

    private final OrthographicCamera orthographicCamera;
    private final Viewport viewport;
    private Rectangle worldBounds;

    public Camera(float worldWidth, float worldHeight) {
        orthographicCamera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, orthographicCamera);
        orthographicCamera.setToOrtho(false, worldWidth, worldHeight);
    }

    public void setWorldBounds(Rectangle bounds) {
        this.worldBounds = bounds;
        centerOnBounds();
    }

    private void centerOnBounds() {
        if (worldBounds == null) return;
        orthographicCamera.position.set(
            worldBounds.x + worldBounds.width / 2f,
            worldBounds.y + worldBounds.height / 2f,
            0
        );
        orthographicCamera.update();
    }

    public void resizeViewport(int screenWidth, int screenHeight) {
        viewport.update(screenWidth, screenHeight, true);
        centerOnBounds();
    }

    public void update() {
        orthographicCamera.update();
    }

    public Matrix4 getCombined() {
        return orthographicCamera.combined;
    }
}
