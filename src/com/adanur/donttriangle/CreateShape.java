package com.adanur.donttriangle;


import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;

public class CreateShape {
	public int oTextureWidth,oTextureHeight;
	public BitmapTextureAtlas oTexture;
	public ITextureRegion oTextureRegion;
	public Sprite oSprite;
	
	
	public CreateShape(TextureManager oTextureManager,int oTextureWidth,int oTextureHeight, TextureOptions oTextureOptions,
			Context oContext, String oAssetPath, int oTexturePositionX,int oTexturePositionY){
		this.oTextureWidth=oTextureWidth;
		this.oTextureHeight=oTextureHeight;
		oTexture= new BitmapTextureAtlas(oTextureManager,oTextureWidth, oTextureHeight,oTextureOptions);
		oTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(oTexture, oContext, oAssetPath,
				oTexturePositionX,oTexturePositionY);
		oTexture.load();
	}
	public CreateShape(){}
	
}
