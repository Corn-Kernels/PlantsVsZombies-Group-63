package com.cornkernels.engine.utility;

public class SkinCreator {

//    public static @NonNull Skin createCustomSliderSkin() {
//        Skin skin = new Skin();
//
//        int trackWidth = 200;
//        int trackHeight = 4;
//        int radius = trackHeight / 2;
//
//        Color grayTrack = new Color(0.3f, 0.3f, 0.3f, 1f);
//        Color whiteProgress = Color.WHITE;
//
//        Pixmap bgPixmap = new Pixmap(trackWidth, trackHeight, Pixmap.Format.RGBA8888);
//        bgPixmap.setColor(grayTrack);
//        bgPixmap.fillCircle(radius, radius, radius);
//        bgPixmap.fillCircle(trackWidth - radius, radius, radius);
//        bgPixmap.fillRectangle(radius, 0, trackWidth - (radius * 2), trackHeight);
//
//        Texture bgTexture = new Texture(bgPixmap);
//        bgPixmap.dispose();
//
//        NinePatch bgPatch = new NinePatch(bgTexture, radius + 1, radius + 1, 0, 0);
//        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(bgPatch);
//
//        Pixmap beforePixmap = new Pixmap(trackWidth, trackHeight, Pixmap.Format.RGBA8888);
//        beforePixmap.setColor(whiteProgress);
//        beforePixmap.fillCircle(radius, radius, radius);
//        beforePixmap.fillRectangle(radius, 0, trackWidth - radius, trackHeight);
//
//        Texture beforeTexture = new Texture(beforePixmap);
//        beforePixmap.dispose();
//
//        NinePatch beforePatch = new NinePatch(beforeTexture, radius, 0, 0, 0);
//        NinePatchDrawable beforeDrawable = new NinePatchDrawable(beforePatch);
//
//        Texture knobTexture = new Texture(Gdx.files.internal(AssetPaths.TEXTURE_UI_EMPTY.getPath()));
//        knobTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(new TextureRegion(knobTexture));
//
//        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
//
//        sliderStyle.background = backgroundDrawable;
//        sliderStyle.knobBefore = beforeDrawable;
//        sliderStyle.knobAfter = knobDrawable;
//
//        skin.add("default-horizontal", sliderStyle, Slider.SliderStyle.class);
//        return skin;
//    }
}
