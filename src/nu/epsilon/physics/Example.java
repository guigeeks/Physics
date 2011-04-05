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
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class Example extends BaseGameActivity {

	protected static final int WIDTH = 480;
	protected static final int HEIGHT = 320;
	protected static final int BLOCKS = 7;

	protected Texture texture;
	protected TiledTextureRegion tileRegions;

	public Engine onLoadEngine() {
		final Camera camera = new Camera(0, 0, WIDTH, HEIGHT);
		final RatioResolutionPolicy ratio = new RatioResolutionPolicy(
				camera.getWidth(), camera.getHeight());
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				ratio, camera));
	}

	public void onLoadResources() {
		this.texture = new Texture(256, 32);
		this.tileRegions = TextureRegionFactory.createTiledFromAsset(
				this.texture, this, "gfx/blocks.png", 0, 0, BLOCKS, 1);
		this.mEngine.getTextureManager().loadTexture(this.texture);
	}

	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0, 0, 0));

		for (int i = 0; i < BLOCKS; i++) {
			TiledSprite sprite = new TiledSprite(94 + i * 52, 144,
					this.tileRegions.clone());
			sprite.setCurrentTileIndex(i);
			scene.getTopLayer().addEntity(sprite);
		}

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

}
