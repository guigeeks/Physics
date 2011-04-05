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
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Adds Box2D physics to Example1.
 * 
 * See previous examples for more detailed comments.
 * 
 * @author Martin Gunnarsson <martin.gunnarsson@epsilon.nu>
 */
public class Example2 extends BaseGameActivity {
	
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
	 * The (Box2D) physics world that runs our simulation.
	 */
	protected PhysicsWorld physicsWorld;
	
	/**
	 * Fixture definition, holds physical properties for our objects.
	 */
	protected FixtureDef fixtureDefinition = PhysicsFactory.createFixtureDef(10, 0.5f, 0.5f);
	
	/**
	 * Create and configure a game engine instance.
	 * 
	 * @return An Engine object to use for this AndEngine app.
	 */
	@Override
	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final RatioResolutionPolicy ratio = new RatioResolutionPolicy(camera.getWidth(), camera.getHeight());
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, ratio, camera));
	}
	
	/**
	 * Load all the assets (graphics, music, sound effects etc) that we're going
	 * to use.
	 */
	@Override
	public void onLoadResources() {
		Texture texture = new Texture(256, 32, TextureOptions.BILINEAR);
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
		
		// Creates physics world that will run our simulation
		this.physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		
		for (int i = 0; i < BLOCKS; i++) {
			TiledSprite sprite = new TiledSprite(68 + i * 52, 144, this.tileRegions.clone());
			sprite.setCurrentTileIndex(i);
			scene.getTopLayer().addEntity(sprite);
			
			// Disables AndEngine's internal "physics" support
			sprite.setUpdatePhysics(false);
			
			// Creates the physics counterpart of the sprite, called a body.
			Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyType.DynamicBody, fixtureDefinition);
			
			// Registers a connection between the sprite and the body, so that this
			// specific sprite will move and rotate along with this specific body.
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body));
		}
		
		// Register the physics world as an update handler, enabling the physics
		// simulation
		scene.registerUpdateHandler(physicsWorld);
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
