package com.adanur.donttriangle;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.adanur.donttriangle.SceneManager.AllScenes;

public class GameActivity extends BaseGameActivity {

	Scene scene,menuscene;
	protected static final int CAMERA_WIDTH = 480;
	protected static final int CAMERA_HEIGHT = 800;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTexureRegion;
	SceneManager sceneManager;
	Camera mCamera;
	myDatabase db=new myDatabase(this);
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		// TODO Auto-generated method stub
		sceneManager=new SceneManager(this, mEngine, mCamera,db);
		sceneManager.loadGameResources();
		//loadGfx();
		// resource
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		// TODO Auto-generated method stub
		
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());

	}
	@Override
	public void onBackPressed()
	{
	    
	    if(sceneManager.getCurrentScene() == AllScenes.GAME && mEngine.getScene().getChildScene()==null){
	    	
			sceneManager.createGamePauseScene();
			
			
	    }
	    else{
	        //this.finish();
	    }
	}
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		// TODO Auto-generated method stub

		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				// TODO Auto-generated method stub
				sceneManager.createMenuScene();
				sceneManager.setCurrentScene(AllScenes.MENU);
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}
