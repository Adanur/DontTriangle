package com.adanur.donttriangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import android.graphics.Color;
import android.util.Log;
import app.shephertz.multiplayer.WarpController;

public class SceneManager {
	private AllScenes currentScene;
	private BaseGameActivity activity;
	Engine engine;
	Camera camera;
	private List<BitmapTextureAtlas> levelTA=new ArrayList<BitmapTextureAtlas>();
	List<ITextureRegion> levelTR=new ArrayList<ITextureRegion>();
	private List<Sprite> spriteLevel=new ArrayList<Sprite>();
	private List<Integer> levelDots=new ArrayList<Integer>();
	public Scene splashScene,gameScene,menuScene,levelScene,gameWinScene,gamePauseScene,gameOverScene,multiPlayerGameScene,multiLoadingScene;
	private PointsKoordinat koorNesne;
	private int koorIndis = 0,koorCount = 12,level,fark=(-1),cizgiCount=0,TiklananSekilIndis=0,levelCount=18,hamleSayisi=0,a=100,b=100;
	public Sprite sekil,aSprite[] = new Sprite[koorCount];
	private float TiklananSekilX = 0, TiklananSekilY = 0;	
	int[][] koordinatlar;
	public boolean[][] cizilenCizgiArray=new boolean[koorCount][koorCount];
	TriangleControl triControl=new TriangleControl(koorCount, cizilenCizgiArray);
	int[] ucgen=new int[3];
	private Line[] cizgi=new Line[100];
	private Line beforeLine=null;
	public boolean oyunBitti=false,sonuc,isCreatedResources=false,isCreateLevelTATR=false,
			hamleliOyunMu=false,isGameStarted = false,isMultiPlayGame=false,isUserTurn = false;
	private myDatabase db;
	private String[] levelPngArray=new String[20];
	private Random randomSayi;
	public CreateShape splashBack,splashText,splashLoadingText,point,pointModif,menuPlayButton,
					menuPlayHoverButton,menuMultiPlayButton,menuBack,pauseMainMenuButton,pauseResumeButton,
					winMainButton,winNextButton,OverMainButton,OverReplayButton,youWinText,youLoseText,pausedText,grayBack,
					waitUserText,gameStartText,loadingGif,yourTurn,rivalsTurn;
	public MultiplayerMetods MulMetdsNsn;
	public Modifiers modifNsn;
	public PlayedLevel plyLvlNesn;
	Font mFont;
	Text text;
	
	public enum AllScenes {
		SPLASH, MENU, GAME, LEVELS, MULTIGAME , MULTILOADING
	}
	public SceneManager(BaseGameActivity act, Engine eng, Camera cam,myDatabase db) {
		this.activity = act;
		this.engine = eng;
		this.camera = cam;
		this.db=db;
	}
	
	public void loadGameResources(){
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("gfx/");
		
		mFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 256, 256, this.activity.getAssets(), 
				"helsinki.ttf", 32f, true, org.andengine.util.adt.color.Color.BLACK_ABGR_PACKED_INT);
		mFont.load();
		
		  //m_Scene.attachChild(text);
		modifNsn= new Modifiers(this);
		plyLvlNesn = new PlayedLevel(this);
		splashText = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "splashtext.png", 0, 0);
		splashBack = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "splashBack.png", 0, 0);
		splashLoadingText= new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "loading.png", 0, 0);
		point = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "point.png", 0, 0);
		pointModif = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.NEAREST, this.activity, "point.png", 0, 0);
		
		createLevelTATR();
		
		menuPlayButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "play.png", 0, 0);
		menuMultiPlayButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "multiplay.png", 0, 0);
		menuPlayHoverButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "playhover.png", 0, 0);
		menuBack = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "menuBack.png", 0, 0);
		pauseMainMenuButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "mainmenuButton.png", 0, 0);
		pauseResumeButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "resumeButton.png", 0, 0);
		winMainButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "winMainMenuButton.png", 0, 0);
		winNextButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "nextLevelButton.png", 0, 0);
		OverMainButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "mainmenuButtonOver.png", 0, 0);
		OverReplayButton = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "playagainButtonOver.png", 0, 0);
		youWinText = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "win.png", 0, 0);
		youLoseText = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "lose.png", 0, 0);
		pausedText = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "paused.png", 0, 0);
		grayBack = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "grayBack.png", 0, 0);
		waitUserText = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "waitusertext.png", 0, 0);
		gameStartText = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "gamestartText.png", 0, 0);
		loadingGif = new CreateShape(this.activity.getTextureManager(), 1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "loadinggif.png", 0, 0);
		yourTurn = new CreateShape(this.activity.getTextureManager(), 512, 512, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "yourturn.png", 0, 0);
		rivalsTurn = new CreateShape(this.activity.getTextureManager(), 512, 512, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.activity, "rivalsTurn.png", 0, 0);
		
		levelDots.add(4);
		levelDots.add(5);
		for (int i = 6; i <= 9; i++) {
			for (int j = 0; j < 4; j++) {
				levelDots.add(i);
			}
		}
		isCreatedResources=true;
	}
	
	private void connectToAppWarp(){
		if (MulMetdsNsn==null) MulMetdsNsn = new MultiplayerMetods(this);
		WarpController.getInstance().setActivity(this);
		Util.UserName = Util.getRandomHexString(10);
		WarpController.getInstance().startApp(Util.UserName, this);
	}
	
	public void createLevelTATR() {
		levelTA.clear();
		levelTR.clear();
		for (int i = 0; i < levelCount; i++) {
			levelPngArray[i]=""+db.getLevelPng(i+1);
			
			levelTA.add(new BitmapTextureAtlas(
					this.activity.getTextureManager(), 1024, 1024,TextureOptions.BILINEAR));
			levelTR.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelTA.get(i), this.activity,
					levelPngArray[i], 0,0));
			levelTA.get(i).load();
		}
	}

	public Scene createGamePauseScene() {
		oyunBitti=true;
		gamePauseScene = new CameraScene(this.camera);
		pausedText.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()+70, pausedText.oTextureRegion, engine.getVertexBufferObjectManager());
		grayBack.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2, grayBack.oTextureRegion, engine.getVertexBufferObjectManager());
		
		//grayBack.oSprite.setScale(3);
		
		MoveModifier ptxMvMod= new MoveModifier(0.5f, camera.getWidth()/2, camera.getHeight() +70, camera.getWidth()/2, camera.getHeight()-90);
		pausedText.oSprite.registerEntityModifier(ptxMvMod);
		gamePauseScene.attachChild(grayBack.oSprite);
		gamePauseScene.attachChild(pausedText.oSprite);
		
		pauseMainMenuButton.oSprite = new Sprite(0, 0, pauseMainMenuButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					isCreateLevelTATR=true;
					if (isMultiPlayGame) {
						isGameStarted = false;
						isUserTurn = false;
						WarpController.getInstance().stopApp();
					}
					setCurrentScene(AllScenes.MENU);
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		gamePauseScene.setBackgroundEnabled(false);
		pauseMainMenuButton.oSprite.setScale(0.45f);
		pauseMainMenuButton.oSprite.setPosition(-65, camera.getHeight()/2);
		MoveModifier pmbMvMod= new MoveModifier(0.5f, -65, camera.getHeight()/2, camera.getWidth()/2-65, camera.getHeight()/2);
		pauseMainMenuButton.oSprite.registerEntityModifier(pmbMvMod);
		gamePauseScene.attachChild(pauseMainMenuButton.oSprite);
		gamePauseScene.registerTouchArea(pauseMainMenuButton.oSprite);
		
		pauseResumeButton.oSprite = new Sprite(0, 0, pauseResumeButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					oyunBitti=false;
					if (gameScene.getChildScene()!=null)
					gameScene.clearChildScene();
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pauseResumeButton.oSprite.setScale(0.45f);
		pauseResumeButton.oSprite.setPosition(camera.getWidth()+65, camera.getHeight()/2);
		MoveModifier prbMvMod= new MoveModifier(0.5f, camera.getWidth()+65, camera.getHeight()/2, camera.getWidth()/2+65, camera.getHeight()/2);
		pauseResumeButton.oSprite.registerEntityModifier(prbMvMod);
		gamePauseScene.attachChild(pauseResumeButton.oSprite);
		gamePauseScene.registerTouchArea(pauseResumeButton.oSprite);
		if (isMultiPlayGame) {
			multiPlayerGameScene.setChildScene(gamePauseScene);
		}else gameScene.setChildScene(gamePauseScene);
		return gamePauseScene;
	
	}
	public Scene createGameOverScene() {
		oyunBitti=true;
		if (isMultiPlayGame){
			//WarpController.getInstance().stopApp();
			yourTurn.oSprite.setVisible(false);
			rivalsTurn.oSprite.setVisible(false);
			}
		gameOverScene = new CameraScene(this.camera);
		youLoseText.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2+150, youLoseText.oTextureRegion, engine.getVertexBufferObjectManager());
		youLoseText.oSprite.setScale(camera.getWidth()/(camera.getHeight()-40));
		gameOverScene.attachChild(youLoseText.oSprite);
		
		OverMainButton.oSprite = new Sprite(0, 0, OverMainButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					isCreateLevelTATR=true;
					if (isMultiPlayGame) {
						isGameStarted = false;
						isUserTurn = false;
						WarpController.getInstance().stopApp();
					}
					setCurrentScene(AllScenes.MENU);
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		gameOverScene.setBackgroundEnabled(false);
		OverMainButton.oSprite.setScale(camera.getWidth()/(camera.getHeight()));
		OverMainButton.oSprite.setPosition(camera.getWidth()/2, camera.getHeight()/2);
		gameOverScene.attachChild(OverMainButton.oSprite);
		gameOverScene.registerTouchArea(OverMainButton.oSprite);
		
		OverReplayButton.oSprite = new Sprite(0, 0, OverReplayButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					oyunBitti=false;
					loadGame(level);
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		OverReplayButton.oSprite.setScale(camera.getWidth()/(camera.getHeight()));
		OverReplayButton.oSprite.setPosition(camera.getWidth()/2, camera.getHeight()/2+80);
		gameOverScene.attachChild(OverReplayButton.oSprite);
		gameOverScene.registerTouchArea(OverReplayButton.oSprite);
		if (isMultiPlayGame) {
			multiPlayerGameScene.setChildScene(gameOverScene);
		}else gameScene.setChildScene(gameOverScene);
		return gameOverScene;
	
	}
	
	public Scene createGameWinScene() {
		oyunBitti=true;
		if (isMultiPlayGame){
		//WarpController.getInstance().stopApp();
		yourTurn.oSprite.setVisible(false);
		rivalsTurn.oSprite.setVisible(false);
		}
		gameWinScene = new CameraScene(this.camera);
		youWinText.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2+150, youWinText.oTextureRegion, engine.getVertexBufferObjectManager());
		youWinText.oSprite.setScale(camera.getWidth()/(camera.getHeight()-40));
		gameWinScene.attachChild(youWinText.oSprite);
		winNextButton.oSprite = new Sprite(0, 0, winNextButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					oyunBitti=false;
					level++;
					loadGame(level);
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		gameWinScene.setBackgroundEnabled(false);
		winNextButton.oSprite.setPosition(camera.getWidth()/2, camera.getHeight()/2+80);
		winNextButton.oSprite.setScale(camera.getWidth()/(camera.getHeight()));
		gameWinScene.attachChild(winNextButton.oSprite);
		gameWinScene.registerTouchArea(winNextButton.oSprite);
		
		winMainButton.oSprite = new Sprite(0, 0, winMainButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					isCreateLevelTATR=true;
					if (isMultiPlayGame) {
						isGameStarted = false;
						isUserTurn = false;
						WarpController.getInstance().stopApp();
					}
					
					setCurrentScene(AllScenes.MENU);
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		gameWinScene.setBackgroundEnabled(false);
		winMainButton.oSprite.setPosition(camera.getWidth()/2, camera.getHeight()/2);
		winMainButton.oSprite.setScale(camera.getWidth()/(camera.getHeight()));
		gameWinScene.attachChild(winMainButton.oSprite);
		gameWinScene.registerTouchArea(winMainButton.oSprite);
		return gameWinScene;
	
	}
	
	public void drawDots(){
		koorNesne = new PointsKoordinat(koorCount);
		koordinatlar = koorNesne.getKoor();
		
		for (koorIndis = 0; koorIndis < koorCount; koorIndis++) {
			aSprite[koorIndis] = new Sprite(koordinatlar[koorIndis][0],
					koordinatlar[koorIndis][1], point.oTextureRegion,
					engine.getVertexBufferObjectManager());
			aSprite[koorIndis].setScale(camera.getHeight()/(camera.getWidth()+150));
		}
	}
	
	public Scene createLevelScene() {
		Log.i("start","girdi");
		int artma=1,karsilastir = 0,a=0,deger=(-1);
		fark=(-1);
		spriteLevel.clear();
		levelScene= new Scene();
		levelScene.setBackground(new Background(1,1,0.7f));
		for (int i = 0; i < levelCount; i++) {
			final int levell=i;
		spriteLevel.add(new Sprite(0, 0, levelTR.get(i), engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp() && pSceneTouchEvent.getX() < (spriteLevel.get(levell).getX()+20.0f) &&
						pSceneTouchEvent.getX() > (spriteLevel.get(levell).getX()-20.0f)) {
					Log.i("ads",""+pSceneTouchEvent.getX());
					oyunBitti=false;
					level=levell+1;
					if (db.isLevelUnLocked(levell+1).equals("true")){
						modifNsn.modifLevel(pSceneTouchEvent.getX(),pSceneTouchEvent.getY(),levell);
						new Timer().schedule(new TimerTask() {   
						    @Override
						    public void run() {
						    	loadGame(level);
						    }
						}, 900);
					}
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		});	
			if (i >= 0 && i == karsilastir) {
				karsilastir = i + artma;
				a = 0;
				fark++;
				if (i < 4)
					artma += 2;
				if (i > 4)
					artma -= 2;
				if (i < 9)
					deger++;
				if (i > 9)
					deger--;
			}
		spriteLevel.get(i).setScale(0.7f);
			spriteLevel
					.get(i)
					.setPosition(
							((camera.getWidth() / 2) - ((camera.getWidth() / 7) * (deger)))
									+ ((camera.getWidth() / 7) * a),
							(camera.getHeight() - 110 * fark) - 130);

		levelScene.attachChild(spriteLevel.get(i));
		levelScene.registerTouchArea(spriteLevel.get(i));
		
		a=a+1;
		}
		return levelScene;
		}
	public void refreshStates(){
		cizgiCount=0;
		for (int i = 0; i < cizilenCizgiArray.length; i++) {
			for (int j = 0; j < cizilenCizgiArray[i].length; j++) {
				cizilenCizgiArray[i][j]=false;
			}
		}
	}
	
	public void loadGame(int level){
		koorCount=levelDots.get(level-1);
		drawDots();
		refreshStates();
		hamleliOyunMu=false;
		
		if (isCreatedResources==false)
		loadGameResources();
		
		createGameScene();
		
		for (int i = 1; i < 4; i++) {
			if (level==i+2 || level==i+6 || level == i+10 || level == i+14) {
				plyLvlNesn.createPlayedLevel(i,koorCount);
				hamleSayisi=i;
			}
		}
		setCurrentScene(AllScenes.GAME);
	}
	
	public Scene createSplashScene() {
		splashScene = new Scene();
		splashBack.oSprite = new Sprite(camera.getWidth()/2,camera.getHeight()/2,splashBack.oTextureRegion,engine.getVertexBufferObjectManager());
		//create a SpriteBackground object.
		splashBack.oSprite.setScale(camera.getHeight()/camera.getWidth());
		SpriteBackground background = new SpriteBackground(1,1,1,splashBack.oSprite);
		//set the background to scene
		splashScene.setBackground(background);
		splashText.oSprite = new Sprite(0, 0, splashText.oTextureRegion, engine.getVertexBufferObjectManager());
		splashText.oSprite.setPosition(camera.getWidth()/2, camera.getHeight()/2+70);
		splashText.oSprite.setScale(camera.getWidth()/camera.getHeight());
		
		splashScene.attachChild(splashText.oSprite);
		
		splashLoadingText.oSprite = new Sprite(0, 0, splashLoadingText.oTextureRegion, engine.getVertexBufferObjectManager());
		splashLoadingText.oSprite.setPosition(camera.getWidth()/2+10, camera.getHeight()/2+10);
		splashLoadingText.oSprite.setScale(camera.getWidth()/camera.getHeight());
		
		splashScene.attachChild(splashLoadingText.oSprite);
		
		return splashScene;

	}
	
	public Scene createMenuScene() {
		menuScene = new Scene();
		menuBack.oSprite = new Sprite(camera.getWidth()/2,camera.getHeight()/2,menuBack.oTextureRegion,engine.getVertexBufferObjectManager());
		//create a SpriteBackground object.
		menuBack.oSprite.setScale(1);
		SpriteBackground background = new SpriteBackground(1,1,1,menuBack.oSprite);
		//set the background to scene
		menuScene.setBackground(background);
		menuPlayHoverButton.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2-20, menuPlayHoverButton.oTextureRegion, engine.getVertexBufferObjectManager());
		menuPlayButton.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2-20, menuPlayButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					menuPlayButton.oSprite.setVisible(false);
					menuPlayHoverButton.oSprite.setVisible(true);
					
				}
				if (pSceneTouchEvent.isActionUp()) {
					if (isCreateLevelTATR){
						createLevelTATR();
						isCreateLevelTATR=false;
					}
					isMultiPlayGame=false;
						createLevelScene();
					
					setCurrentScene(AllScenes.LEVELS);
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		menuPlayButton.oSprite.setScale(camera.getWidth()/camera.getHeight());
		menuPlayHoverButton.oSprite.setScale(camera.getWidth()/camera.getHeight());
		menuMultiPlayButton.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2-100, menuMultiPlayButton.oTextureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
					if (pSceneTouchEvent.isActionUp()) {
						oyunBitti=false;
						isMultiPlayGame=true;
						refreshStates();
						connectToAppWarp();
						createMultiLoadingScene();
						setCurrentScene(AllScenes.MULTILOADING);
					}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		menuMultiPlayButton.oSprite.setScale(camera.getWidth()/camera.getHeight());
		menuScene.registerTouchArea(menuPlayButton.oSprite);
		menuScene.attachChild(menuPlayHoverButton.oSprite);
		menuScene.attachChild(menuPlayButton.oSprite);
		menuScene.attachChild(menuMultiPlayButton.oSprite);
		menuScene.registerTouchArea(menuMultiPlayButton.oSprite);
		
		
		Sprite icon = new Sprite(0, 0, splashText.oTextureRegion, engine.getVertexBufferObjectManager());
		icon.setPosition(camera.getWidth()/2, camera.getHeight()/2+70);
		icon.setScale(camera.getWidth()/camera.getHeight());
		
		menuScene.attachChild(icon);
		
		menuScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				switch (pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					menuPlayButton.oSprite.setVisible(true);
					menuPlayHoverButton.oSprite.setVisible(false);
					break;

				default:
					break;
				}
				return false;
			}
		});
		return menuScene;
	}
	public Scene createMultiLoadingScene() {
		multiLoadingScene = new Scene();
		multiLoadingScene.setBackground(new Background(1.0f, 1.0f, 1.0f));
		text = new Text(camera.getWidth()/2, camera.getHeight()/2-150, this.mFont, "Hello Android", this.activity.getVertexBufferObjectManager());
		multiLoadingScene.attachChild(text);
		text.setPosition(camera.getWidth()/2, camera.getHeight()/2-150);
		loadingGif.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()/2, loadingGif.oTextureRegion, engine.getVertexBufferObjectManager());
		loadingGif.oSprite.setScale(0.8f);
		RotationModifier rtMod= new RotationModifier(2, 0, 360);
				LoopEntityModifier loopMod = new LoopEntityModifier(rtMod);
		loadingGif.oSprite.registerEntityModifier(loopMod);
		multiLoadingScene.attachChild(loadingGif.oSprite);
		waitUserText.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()-90, waitUserText.oTextureRegion, engine.getVertexBufferObjectManager());
		waitUserText.oSprite.setVisible(false);
		multiLoadingScene.attachChild(waitUserText.oSprite);
		gameStartText.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()-90, gameStartText.oTextureRegion, engine.getVertexBufferObjectManager());
		gameStartText.oSprite.setVisible(false);
		multiLoadingScene.attachChild(gameStartText.oSprite);
		yourTurn.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()-100, yourTurn.oTextureRegion, engine.getVertexBufferObjectManager());
		 yourTurn.oSprite.setVisible(false);
		 rivalsTurn.oSprite = new Sprite(camera.getWidth()/2, camera.getHeight()-100, rivalsTurn.oTextureRegion, engine.getVertexBufferObjectManager());
		 rivalsTurn.oSprite.setVisible(false);
		
		return multiLoadingScene;
	}
	
	public Scene createMultiPlayerGameScene() {
		multiPlayerGameScene = new Scene();
		multiPlayerGameScene.setBackground(new Background(1.0f, 1.0f, 1.0f));
		koorCount=5;
		drawDots();
		
		multiPlayerGameScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				switch (pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if (oyunBitti==false) {
					for (int i = 0; i < koorCount; i++) {
						if (pSceneTouchEvent.getX() > aSprite[i].getX()-((aSprite[i].getWidthScaled()/2)+5)
								&& pSceneTouchEvent.getX() < aSprite[i].getX()+((aSprite[i].getWidthScaled()/2)+5)
								&& pSceneTouchEvent.getY() > aSprite[i].getY()-((aSprite[i].getHeightScaled()/2)+5)
								&& pSceneTouchEvent.getY() < aSprite[i].getY()+((aSprite[i].getHeightScaled()/2)+5)) {
							
							TiklananSekilX = aSprite[i].getX();
							TiklananSekilY = aSprite[i].getY();
							TiklananSekilIndis=i;
							modifNsn.modifPoints(TiklananSekilX,TiklananSekilY);
							sekil = aSprite[i];
							a=i;
						}
						}
					}
					break;
				case TouchEvent.ACTION_MOVE:
					if(sekil!=null){
						if (beforeLine!=null)
							multiPlayerGameScene.detachChild(beforeLine);
						
						Line line=new Line(TiklananSekilX, TiklananSekilY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 10,
								engine.getVertexBufferObjectManager());
						line.setColor(Color.parseColor("#85B3FF"));
						multiPlayerGameScene.attachChild(line);
						beforeLine=line;						
					}
					break;
				case TouchEvent.ACTION_UP:
					float X=pSceneTouchEvent.getX();
					float Y=pSceneTouchEvent.getY();
					if (oyunBitti==false && sekil!=null) {
					for (int i = 0; i < koorCount; i++) {		
						if (beforeLine != null && cizilenCizgiArray[TiklananSekilIndis][i]==false) {	
							multiPlayerGameScene.detachChild(beforeLine);
							
							if (X>aSprite[i].getX()-((aSprite[i].getWidthScaled()/2)+5) 
							&& X<aSprite[i].getX()+((aSprite[i].getWidthScaled()/2)+5) 
							&& Y>aSprite[i].getY()-((aSprite[i].getHeightScaled()/2)+5) 
							&& Y<aSprite[i].getY()+((aSprite[i].getHeightScaled()/2)+5)) {	
						
								b=i;
								
								if(isGameStarted && isUserTurn){
									
									MulMetdsNsn.getIndex(a, b);
								}
								break;
							}
						}
					}
					sekil=null;
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		 for (int i = 0; i < koorCount; i++) {
			 multiPlayerGameScene.attachChild(aSprite[i]);
			}
		 
		 
		 multiPlayerGameScene.attachChild(yourTurn.oSprite);
		 multiPlayerGameScene.attachChild(rivalsTurn.oSprite);
		return multiPlayerGameScene;
	}
	
	public Scene createGameScene() {
		gameScene = new Scene();
		gameScene.setBackground(new Background(1.0f, 1.0f, 1.0f));
		
		
		gameScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				switch (pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if (oyunBitti==false) {
					for (int i = 0; i < koorCount; i++) {
						if (pSceneTouchEvent.getX() > aSprite[i].getX()-((aSprite[i].getWidthScaled()/2)+5)
								&& pSceneTouchEvent.getX() < aSprite[i].getX()+((aSprite[i].getWidthScaled()/2)+5)
								&& pSceneTouchEvent.getY() > aSprite[i].getY()-((aSprite[i].getHeightScaled()/2)+5)
								&& pSceneTouchEvent.getY() < aSprite[i].getY()+((aSprite[i].getHeightScaled()/2)+5)) {
							
							TiklananSekilX = aSprite[i].getX();
							TiklananSekilY = aSprite[i].getY();
							TiklananSekilIndis=i;
							modifNsn.modifPoints(TiklananSekilX,TiklananSekilY);
							sekil = aSprite[i];
						}
						}
					}
					break;
				case TouchEvent.ACTION_MOVE:
					if(sekil!=null){
						if (beforeLine!=null)
						gameScene.detachChild(beforeLine);
						
						Line line=new Line(TiklananSekilX, TiklananSekilY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 10,
								engine.getVertexBufferObjectManager());
						line.setColor(Color.parseColor("#85B3FF"));
						gameScene.attachChild(line);
						beforeLine=line;						
					}
					break;
				case TouchEvent.ACTION_UP:
					float X=pSceneTouchEvent.getX();
					float Y=pSceneTouchEvent.getY();
					if (oyunBitti==false && sekil!=null) {
					for (int i = 0; i < koorCount; i++) {		
						if (beforeLine != null && cizilenCizgiArray[TiklananSekilIndis][i]==false) {	
							gameScene.detachChild(beforeLine);
							
							if (X>aSprite[i].getX()-((aSprite[i].getWidthScaled()/2)+5) 
							&& X<aSprite[i].getX()+((aSprite[i].getWidthScaled()/2)+5) 
							&& Y>aSprite[i].getY()-((aSprite[i].getHeightScaled()/2)+5) 
							&& Y<aSprite[i].getY()+((aSprite[i].getHeightScaled()/2)+5)) {
								
								if (sekil!=aSprite[i]){
									
								drawLine(TiklananSekilX, TiklananSekilY,aSprite[i].getX(), aSprite[i].getY(), 10);
								cizilenCizgiArray[TiklananSekilIndis][i]=true;
								cizilenCizgiArray[i][TiklananSekilIndis]=true;
								modifNsn.modifPoints(TiklananSekilX,TiklananSekilY);
								modifNsn.modifPoints(aSprite[i].getX(),aSprite[i].getY());
								ucgen=triControl.fonkTriangleControl();
								
								if (hamleliOyunMu==false) {	
								
								if(gameOverTest() && ucgen==null){
									
											createGameWinScene();
											oyunBitti=true;
											gameScene.setChildScene(gameWinScene);
											
											db.changeLevelPng(level+1, "level"+(level+1)+".png");
											passLevel();
								}else{
									new Timer().schedule(new TimerTask() {   
									    @Override
									    public void run() {
									    	//if (level==1 || level==2 || level==6 || level==10 || level==14 || level==18) {
									    		sonuc= playComputer(); 
									    	//	}      
									    		if (gameOverTest()) {
									    			createGameOverScene();
									    		}
									    	
									    }
									}, 1000);
								}
								}
								else{
									hamleSayisi--;
									if (hamleSayisi==0) {
										ucgen=triControl.fonkTriangleControl();
										if(gameOverTest() && ucgen==null){
											createGameWinScene();
											oyunBitti=true;
											gameScene.setChildScene(gameWinScene);
											
											db.changeLevelPng(level+1, "level"+(level+1)+".png");
											passLevel();
										}
										else {
											createGameOverScene();
											Log.i("oyun","hamle sýfýr ancakoyun bitmedi");
										}
									}
									else{
										if(gameOverTest()){
											createGameOverScene();
											Log.i("oyun","hamle sýfýr degil ancak oyun bitti");
										}
									}
								}
								break;
								}
							}
						}
					}
					ucgen=triControl.fonkTriangleControl();
					if(ucgen!=null){
						drawTriangle();
						oyunBitti=true;
					}
					sekil=null;
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		 
	        for (int i = 0; i < koorCount; i++) {
			gameScene.attachChild(aSprite[i]);
		}

		gameScene.setTouchAreaBindingOnActionDownEnabled(true);
		
		return gameScene;

	}
	public void passLevel(){
		db.unLockLevel(level+1, "true");
	}

	public void drawLine(float x1,float y1,float x2,float y2,int lineWidth){
		cizgi[cizgiCount] = new Line(x1, y1,x2, y2, lineWidth,
				engine.getVertexBufferObjectManager());
		
		cizgi[cizgiCount].setColor(Color.parseColor("#85B3FF"));
		if (isMultiPlayGame) {
			multiPlayerGameScene.attachChild(cizgi[cizgiCount]);
		}else gameScene.attachChild(cizgi[cizgiCount]);
		
		cizgiCount++;
	}
	
	public boolean gameOverTest(){
		for (int i = 0; i < koorCount; i++) {
			for (int j = 0; j < koorCount; j++) {
				if (cizilenCizgiArray[i][j]==false && i!=j) {
					cizilenCizgiArray[i][j]=true;
					cizilenCizgiArray[j][i]=true;
					
					if (triControl.fonkTriangleControl() == null) {
						cizilenCizgiArray[i][j]=false;
						cizilenCizgiArray[j][i]=false;
						return false;
					}
					else{
						cizilenCizgiArray[i][j]=false;
						cizilenCizgiArray[j][i]=false;
					}
				}
			}
		}
		return true;
		
	}
	
	public boolean playComputer(){
		List<String> cizilmeyenCizgiList = new ArrayList<String>();
		cizilmeyenCizgiList.clear();
		String veri="";
		for (int i = 0; i < koorCount; i++) {
			for (int j = 0; j < koorCount; j++) {
				
				if (cizilenCizgiArray[i][j]==false && j>i) {
					veri=""+i+"-"+j;
					cizilmeyenCizgiList.add(veri);
				}
			}
		}	
		
		while(cizilmeyenCizgiList.size()>0){
			
			randomSayi=new Random();
			int sayi=1+randomSayi.nextInt(cizilmeyenCizgiList.size());
			String cizilecek=cizilmeyenCizgiList.get(sayi-1);
	
			String[] parts = cizilecek.split("-");
			int part1 = Integer.valueOf(parts[0]);
			int part2 = Integer.valueOf(parts[1]);
			cizilenCizgiArray[part1][part2]=true;
			cizilenCizgiArray[part2][part1]=true;
			if (triControl.fonkTriangleControl() == null) {
				drawLine(koordinatlar[part1][0], koordinatlar[part1][1], koordinatlar[part2][0], koordinatlar[part2][1], 10);
				modifNsn.modifLines(koordinatlar[part1][0], koordinatlar[part1][1], koordinatlar[part2][0], koordinatlar[part2][1], 10);
				return true;
			}
			else{
				cizilenCizgiArray[part1][part2]=false;
				cizilenCizgiArray[part2][part1]=false;
				cizilmeyenCizgiList.remove(sayi-1);
				
			}
		}
		return false;
		
	}
	
	public void drawTriangle(){
		final Line cizgi0 = new Line(
				koordinatlar[ucgen[0]][0], koordinatlar[ucgen[0]][1],
				koordinatlar[ucgen[1]][0], koordinatlar[ucgen[1]][1], 10,
				engine.getVertexBufferObjectManager());
		cizgi0.setColor(1.0f, 0.0f, 0.0f);
		final Line cizgi1 = new Line(
				koordinatlar[ucgen[1]][0], koordinatlar[ucgen[1]][1],
				koordinatlar[ucgen[2]][0], koordinatlar[ucgen[2]][1], 10,
				engine.getVertexBufferObjectManager());
		cizgi1.setColor(1.0f, 0.0f, 0.0f);
		final Line cizgi2 = new Line(
				koordinatlar[ucgen[2]][0], koordinatlar[ucgen[2]][1],
				koordinatlar[ucgen[0]][0], koordinatlar[ucgen[0]][1], 10,
				engine.getVertexBufferObjectManager());
		cizgi2.setColor(1.0f, 0.0f, 0.0f);
		if (isMultiPlayGame) {
			multiPlayerGameScene.attachChild(cizgi0);
			multiPlayerGameScene.attachChild(cizgi1);
			multiPlayerGameScene.attachChild(cizgi2);
		}else{
		gameScene.attachChild(cizgi0);
		gameScene.attachChild(cizgi1);
		gameScene.attachChild(cizgi2);
		}
	}
	public AllScenes getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.currentScene = currentScene;
		
		switch (currentScene) {
		case SPLASH:
			break;
		case MENU:
			engine.setScene(menuScene);
			break;
		case GAME:
			engine.setScene(gameScene);
			break;
		case LEVELS:
			engine.setScene(levelScene);
			break;
		case MULTIGAME:
			engine.setScene(multiPlayerGameScene);
			break;
		case MULTILOADING:
			engine.setScene(multiLoadingScene);
			break;
		default:
			break;
		}
	}

	
}
