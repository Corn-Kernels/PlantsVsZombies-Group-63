package com.cornkernels.engine.renderer.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Camera implements GameplayCamera {

    private static final float MIN_WORLD_WIDTH = 1f;

    private final OrthographicCamera orthographicCamera;
    private final ExtendViewport viewport;
    private final Interpolation panInterpolation = Interpolation.smooth;
    private Rectangle worldBounds;
    private boolean panning = false;
    private float panStartX;
    private float panTargetX;
    private float panElapsed;
    private float panDuration;
    private Runnable onPanComplete;

    public Camera(float mapWorldHeight) {
        orthographicCamera = new OrthographicCamera();
        viewport = new ExtendViewport(MIN_WORLD_WIDTH, mapWorldHeight, orthographicCamera);
    }

    public void setWorldBounds(Rectangle bounds) {
        this.worldBounds = bounds;
        snapToLeftEdge();
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
        viewport.update(screenWidth, screenHeight, false);
        clampPosition();
        orthographicCamera.update();
    }

    @Override
    public void snapToLeftEdge() {
        panning = false;
        orthographicCamera.position.x = getLeftEdgeX();
        orthographicCamera.position.y = worldBounds.y + worldBounds.height / 2f;
        orthographicCamera.update();
    }

    @Override
    public void snapToRightEdge() {
        panning = false;
        orthographicCamera.position.x = getRightEdgeX();
        orthographicCamera.position.y = worldBounds.y + worldBounds.height / 2f;
        orthographicCamera.update();
    }

    @Override
    public void panToRightEdge(float duration, Runnable onComplete) {
        startPan(getRightEdgeX(), duration, onComplete);
    }

    @Override
    public void panToLeftEdge(float duration, Runnable onComplete) {
        startPan(getLeftEdgeX(), duration, onComplete);
    }

    @Override
    public Vector2 screenToWorld(float screenX, float screenY) {
        Vector3 result = viewport.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(result.x, result.y);
    }

    private void startPan(float targetX, float duration, Runnable onComplete) {
        panStartX = orthographicCamera.position.x;
        panTargetX = targetX;
        panElapsed = 0f;
        panDuration = Math.max(0.0001f, duration);
        onPanComplete = onComplete;
        panning = true;
    }

    public boolean isPanning() {
        return panning;
    }

    public void update(float delta) {
        if (panning) {
            panElapsed += delta;
            float t = Math.min(panElapsed / panDuration, 1f);
            orthographicCamera.position.x = MathUtils.lerp(panStartX, panTargetX, panInterpolation.apply(t));

            if (t >= 1f) {
                panning = false;
                orthographicCamera.position.x = panTargetX;
                Runnable callback = onPanComplete;
                onPanComplete = null;
                if (callback != null) callback.run();
            }
        }

        clampPosition();
        orthographicCamera.update();
    }


    private void clampPosition() {
        if (worldBounds == null) return;
        float left = getLeftEdgeX();
        float right = getRightEdgeX();

        orthographicCamera.position.x = left > right
            ? worldBounds.x + worldBounds.width / 2f
            : MathUtils.clamp(orthographicCamera.position.x, left, right);

        orthographicCamera.position.y = worldBounds.y + worldBounds.height / 2f;
    }

    public float getLeftEdgeX() {
        return worldBounds.x + Math.min(viewport.getWorldWidth() / 2f, worldBounds.width / 2f);
    }

    public float getRightEdgeX() {
        return worldBounds.x + worldBounds.width - Math.min(viewport.getWorldWidth() / 2f, worldBounds.width / 2f);
    }


    public Matrix4 getCombined() {
        return orthographicCamera.combined;
    }


}
