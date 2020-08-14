package com.hopto.dtanjp.tictactoe;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.hopto.dtanjp.Cache;
import com.hopto.dtanjp.audio.Audio;

/**
 * AssetManager.java
 * 
 * @author David Tan
 **/
public class AssetManager {

	/** Singleton instance **/
	private static AssetManager instance = null;
	
	/** Check if the cache is loaded **/
	private boolean isLoaded = false;
	
	/** Cache instance **/
	private Cache assetCache;
	
	/** Image assets **/
	public final BufferedImage[] grid_imgs = new BufferedImage[7];
	public final BufferedImage[] x_imgs = new BufferedImage[5];
	public final BufferedImage[] o_imgs = new BufferedImage[5];
	public BufferedImage settings_icon = null,
							title_icon = null, 
							monitor_icon = null,
							speaker_icon = null,
							options_icon = null,
							game_icon = null;
	
	public BufferedImage glassPanel = null;
	
	/** Main menu background music **/
	public Audio main_menu_bgm = null;
	
	/** Particles: Used for the main menu **/
	public final List<BufferedImage> particles = new ArrayList<>(17);
	
	/** Missing assets count **/
	private int missingAssets = 0;
	
	/** Constructor **/
	private AssetManager() {
		Load();
	}
	
	public void Load() {
		isLoaded = false;
		assetCache = new Cache("res/assets.cache");
		if(!assetCache.exist()) return;
		assetCache.load();
		missingAssets = 0;
		
		/** Load in game logo **/
		game_icon = getBufferedImage("icon.jpg");
		
		/** Load in title icon for main menu **/
		title_icon = getBufferedImage("title.png");
		
		/** Load in settings icon **/
		settings_icon = getBufferedImage("setting-icon.png");
		
		options_icon = getBufferedImage("options-icon.png");
		
		/** Load in the grid images **/
		for(int i=0; i<grid_imgs.length; i++) {
			grid_imgs[i] = getBufferedImage("grid"+(i+1)+".png");
			if(grid_imgs[i] != null) 
				particles.add(grid_imgs[i]);
		}
		
		/** Load in X images **/
		for(int i=0; i<x_imgs.length; i++) {
			x_imgs[i] = getBufferedImage("X"+(i+1)+".png");
			if(x_imgs[i] != null) 
				particles.add(x_imgs[i]);
		}
		
		/** Load in O images **/
		for(int i=0; i<o_imgs.length; i++) {
			o_imgs[i] = getBufferedImage("O"+(i+1)+".png");
			if(o_imgs[i] != null) 
				particles.add(o_imgs[i]);
		}
		
		glassPanel = getBufferedImage("glassPanel.png");
		monitor_icon = getBufferedImage("monitor-icon.png");
		speaker_icon = getBufferedImage("speaker-icon.png");
		
		main_menu_bgm = new Audio(assetCache.get("chadderbox-chillBubomp.wav"));
		assetCache = null;//Unload the cache
		System.gc();
		isLoaded = true;
	}
	
	/** Grab the BufferedImage from the cache **/
	private BufferedImage getBufferedImage(String key) {
		if(assetCache.get(key) == null) {
			missingAssets++;
			return null;
		}
		try {
			return ImageIO.read(new ByteArrayInputStream(assetCache.get(key)));
		} catch (IOException e) {
			missingAssets++;
			return null;
		}
	}
	
	/** Check if the cache is loaded **/
	public boolean isLoaded() { return isLoaded; }
	
	/** Return the amount of resources that failed to load **/
	public int getMissingAssetsCount() { return missingAssets; }
	
	/** Singleton **/
	public static AssetManager getInstance() {
		if(instance == null)
			instance = new AssetManager();
		return instance;
	}
}
