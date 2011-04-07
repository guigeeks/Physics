// AndEngine examples intended for educational purposes only.
// Copyright (c) 2011 Martin Gunnarsson and PŠr Sikš, Epsilon Information Technology AB.
//
// Use at your own risk, don't reproduce without permission. Give credit where credit is due :-)

package nu.epsilon.physics;

import java.util.Iterator;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.hardware.SensorManager;
import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Adds a touch listener to the scene to create an (invisible) explosion on
 * touch down.
 * 
 * See previous examples for more detailed comments.
 * 
 * @author Martin Gunnarsson <martin.gunnarsson@epsilon.nu>
 */
public class Example4 extends BaseGameActivity implements IOnSceneTouchListener {
	
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
	 * Maximum force of the explosion, very magical number
	 */
	private static final float FORCE = 20000f;
	
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
		
		// Register for touch events
		scene.setOnSceneTouchListener(this);
		
		this.physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		
		for (int i = 0; i < BLOCKS; i++) {
			TiledSprite sprite = new TiledSprite(68 + i * 52, 144, this.tileRegions.clone());
			sprite.setCurrentTileIndex(i);
			scene.getTopLayer().addEntity(sprite);
			
			sprite.setUpdatePhysics(false);
			Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyType.DynamicBody, fixtureDefinition);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body));
		}
		
		// Add ceiling
		final Shape ceiling = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		PhysicsFactory.createBoxBody(this.physicsWorld, ceiling, BodyType.StaticBody, this.fixtureDefinition);
		scene.getBottomLayer().addEntity(ceiling);
		
		// Add right wall
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
		PhysicsFactory.createBoxBody(this.physicsWorld, right, BodyType.StaticBody, this.fixtureDefinition);
		scene.getBottomLayer().addEntity(right);
		
		// Add floor
		final Shape floor = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		PhysicsFactory.createBoxBody(this.physicsWorld, floor, BodyType.StaticBody, this.fixtureDefinition);
		scene.getBottomLayer().addEntity(floor);
		
		// Add left wall
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		PhysicsFactory.createBoxBody(this.physicsWorld, left, BodyType.StaticBody, this.fixtureDefinition);
		scene.getBottomLayer().addEntity(left);
		
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
	
	/**
	 * Called for all touch events. We only want to know when the user begins a
	 * touch gesture (TouchEvent.ACTION_DOWN), all other events are ignored.
	 * 
	 * @param scene The scene.
	 * @param event The current touch event.
	 */
	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent event) {
		if (event.getAction() != TouchEvent.ACTION_DOWN) {
			return false;
		}
		
		// Get the location of the touch event
		float x = event.getX();
		float y = event.getY();
		
		// Loop through the physics connectors in the physics world
		Iterator<PhysicsConnector> iterator = physicsWorld.getPhysicsConnectorManager().iterator();
		while (iterator.hasNext()) {
			PhysicsConnector pc = iterator.next();
			
			Body body = pc.getBody();
			
			// Ignore static bodies, as they won't be affected by forces anyway
			if (body.getType() != BodyType.DynamicBody) {
				continue;
			}
			
			// Calculate the distance between the shape of the physics connector
			// and the touch event
			IShape shape = pc.getShape();
			float xDist = shape.getX() - x;
			float yDist = shape.getY() - y;
			float dist = FloatMath.sqrt(xDist * xDist + yDist * yDist);
			
			// Decrease the force with distance
			xDist /= dist * dist;
			yDist /= dist * dist;
			
			// And boom!
			body.applyLinearImpulse(new Vector2(xDist * FORCE, yDist * FORCE),
					new Vector2(body.getPosition().x, body.getPosition().y));
		}
		
		return true;
	}
}
