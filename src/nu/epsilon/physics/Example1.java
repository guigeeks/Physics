// AndEngine examples intended for educational purposes only.
// Copyright (c) 2011 Martin Gunnarsson and PŠr Sikš, Epsilon Information Technology AB.
//
// Use at your own risk, don't reproduce without permission. Give credit where credit is due :-)

package nu.epsilon.physics;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * Simple example loading a tiled texture, creating a sprite from each tile, and
 * displaying them all on the screen.
 * 
 * See previous example for more detailed comments. 
 * 
 * @author Martin Gunnarsson <martin.gunnarsson@epsilon.nu>
 */
public class Example1 extends BaseGameActivity {
	
	/**
	 * Viewport width in pixels.
	 */
	private static final int CAMERA_WIDTH = 480;
	
	/**
	 * Viewport height in pixels.
	 */
	private static final int CAMERA_HEIGHT = 320;
	
	/**
	 * Number of blocks (tiles) in image.
	 */
	private static final int BLOCKS = 7;
	
	/**
	 * Tiled texture region for our sprites.
	 */
	private TiledTextureRegion tileRegions;
	
	/**
	 * Create and configure a game engine instance.
	 * 
	 * @return An Engine object to use for this AndEngine app.
	 */
	@Override
	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final RatioResolutionPolicy ratio = new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, ratio, camera));
	}
	
	/**
	 * Load all the assets (graphics, music, sound effects etc) that we're going
	 * to use.
	 */
	@Override
	public void onLoadResources() {
		Texture texture = new Texture(256, 32, TextureOptions.BILINEAR);
		
		// Loads an image with multiple frames, places them on our texture and
		// returns a TiledTextureRegion. The BLOCKS+1 is because the image
		// contains an extra frame that won't be used here.
		this.tileRegions = TextureRegionFactory
				.createTiledFromAsset(texture, this, "gfx/blocks.png", 0, 0, BLOCKS + 1, 1);
		this.mEngine.getTextureManager().loadTexture(texture);
	}
	
	/**
	 * Create the initial scene for this example.
	 * 
	 * @return A scene object.
	 */
	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0, 0, 0));
		
		// Loop through the tiles in the TiledTextureRegion created above and
		// create a sprite for each one of them.
		for (int i = 0; i < BLOCKS; i++) {
			// We have to clone the TextureTileRegion objects for each sprite, as
			// changing the tile index would change the index for all of the tiles
			// otherwise. This is not a heavy operation, the tile regions are
			// lightweight objects.
			TiledSprite sprite = new TiledSprite(68 + i * 52, 144, this.tileRegions.clone());
			
			// Tell the sprite what tile to use
			sprite.setCurrentTileIndex(i);
			
			scene.getTopLayer().addEntity(sprite);
		}
		
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
