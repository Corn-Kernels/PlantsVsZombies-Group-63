package com.cornkernels.engine.renderer.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HudCamera {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Rectangle virtualBounds;

    public HudCamera(float virtualWidth, float virtualHeight) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        camera.setToOrtho(false, virtualWidth, virtualHeight);
        virtualBounds = new Rectangle(0, 0, virtualWidth, virtualHeight);
    }

    public void resize(int screenWidth, int screenHeight) {
        viewport.update(screenWidth, screenHeight, true);
    }

    public Matrix4 getCombined() {
        return camera.combined;
    }

    public Vector2 screenToHud(float screenX, float screenY) {
        Vector3 result = viewport.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(result.x, result.y);
    }

    public Rectangle getVirtualBounds() {
        return virtualBounds;
    }

    public void applyViewport() {
        viewport.apply();
    }
}
