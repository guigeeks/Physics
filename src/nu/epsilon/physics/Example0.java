// AndEngine examples intended for educational purposes only.
// Copyright (c) 2011 Martin Gunnarsson and PŠr Sikš, Epsilon Information Technology AB.
//
// Use at your own risk, don't reproduce without permission.

package nu.epsilon.physics;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * Simple example showing how to get started with AndEngine. Loads an image and
 * displays it on the screen.
 * 
 * @author Martin Gunnarsson <martin.gunnarsson@epsilon.nu>
 */
public class Example0 extends BaseGameActivity {
	
	/**
	 * Viewport width in pixels.
	 */
	private static final int CAMERA_WIDTH = 480;
	
	/**
	 * Viewport height in pixels.
	 */
	private static final int CAMERA_HEIGHT = 320;
	
	/**
	 * Texture region for our sprite.
	 */
	private TextureRegion region;
	
	/**
	 * Create and configure a game engine instance.
	 * 
	 * @return An Engine object to use for this AndEngine app.
	 */
	@Override
	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		// RatioResolutionPolicy tells AndEngine how to behave on screens with
		// different aspect ratios
		final RatioResolutionPolicy ratio = new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, ratio, camera));
	}
	
	/**
	 * Load all the assets (graphics, music, sound effects etc) that we're going
	 * to use.
	 */
	@Override
	public void onLoadResources() {
		// Create an empty texture to place loaded images onto. OpenGL textures
		// must be 2^m*2^n pixels big.
		Texture texture = new Texture(512, 128);
		
		// Load an image and place it on the texture. The returned texture region
		// contains the location and size of the image on the texture. We'll use
		// this later on when drawing the image. This step can be repeated if we
		// need to load multiple images. Just change the last arguments (x and y
		// position on the texture). Also see BuildableTexture.
		region = TextureRegionFactory.createFromAsset(texture, this, "gfx/epsilon.png", 0, 0);
		
		// Load the texture into video memory, so that OpenGL can use it for
		// drawing.
		this.mEngine.getTextureManager().loadTexture(texture);
	}
	
	/**
	 * Create the initial scene for this example.
	 * 
	 * @return A scene object.
	 */
	@Override
	public Scene onLoadScene() {
		// Create a scene object with one layer and a white background
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(1f, 1f, 1f));
		
		// Create a sprite and provide it with its location on the screen and a
		// texture region to get its graphics from. See above.
		Sprite sprite = new Sprite(90, 122, this.region);
		
		// Add sprite to scene and return it.
		scene.getTopLayer().addEntity(sprite);
		return scene;
	}
	
	/**
	 * Called once onLoadEngine, onLoadResources and onLoadScene has finished
	 * running. We don't really have to do anything here.
	 */
	@Override
	public void onLoadComplete() {
		// Intentionally left blank
	}
	
}
