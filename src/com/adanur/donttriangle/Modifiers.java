package com.adanur.donttriangle;

import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;

import android.graphics.Color;

public class Modifiers {

	SceneManager scnNesne;
	
	public Modifiers(SceneManager scnNesn) {
		this.scnNesne = scnNesn;
	}
	public void modifLevel(float x,float y,int i){
		Sprite levelsprt = new Sprite(x,y, scnNesne.levelTR.get(i),
				scnNesne.engine.getVertexBufferObjectManager());
		levelsprt.setScale(0.6f);
		
		FadeOutModifier fadeOut = new FadeOutModifier(2);
		ScaleModifier scaleUp = new ScaleModifier(2, 1, 3);
		levelsprt.registerEntityModifier(fadeOut);
		levelsprt.registerEntityModifier(scaleUp);
		scnNesne.levelScene.attachChild(levelsprt);
	}
	
	public void modifPoints(float x,float y){
		scnNesne.pointModif.oSprite = new Sprite(x,y, scnNesne.pointModif.oTextureRegion,
				scnNesne.engine.getVertexBufferObjectManager());
		scnNesne.pointModif.oSprite.setScale(scnNesne.camera.getHeight()/(scnNesne.camera.getWidth()+150));
		
		FadeOutModifier fadeOut = new FadeOutModifier(2);
		ScaleModifier scaleUp = new ScaleModifier(2, scnNesne.camera.getHeight()/(scnNesne.camera.getWidth()+150), 2);
		scnNesne.pointModif.oSprite.registerEntityModifier(fadeOut);
		scnNesne.pointModif.oSprite.registerEntityModifier(scaleUp);
		if (scnNesne.isMultiPlayGame) {
			scnNesne.multiPlayerGameScene.attachChild(scnNesne.pointModif.oSprite);
		}else scnNesne.gameScene.attachChild(scnNesne.pointModif.oSprite);
	}
	public void modifLines(float x1,float y1,float x2,float y2,int lineWidth){
		Line line=new Line(x1, y1,x2, y2, lineWidth,
				scnNesne.engine.getVertexBufferObjectManager());
		line.setColor(Color.parseColor("#FF0000"));
		
		FadeOutModifier fadeOut = new FadeOutModifier(2);
		
		line.registerEntityModifier(fadeOut);
		if (scnNesne.isMultiPlayGame) {
			scnNesne.multiPlayerGameScene.attachChild(line);
		}else scnNesne.gameScene.attachChild(line);
	}

}
