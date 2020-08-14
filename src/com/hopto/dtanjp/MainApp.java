package com.hopto.dtanjp;

import java.awt.MouseInfo;
import java.awt.Toolkit;

import com.hopto.dtanjp.tictactoe.GameController;

/**
 * MainApp.java
 * 
 * @author David Tan
 **/
public class MainApp {

	/** MainApp instance **/
	private static MainApp instance = null;
	
	/** Setup Success: Is the game able to start up successfully **/
	private boolean setupSuccess = false;
	
	/** GameController instance **/
	private GameController gcontroller = null;
	
	/** Last FPS Time **/
	private long lastFpsTime = 0;
	
	/** The FPS achieved **/
	public static int FPS = 0;
	
	/** Milliseconds that occured since the last update **/
	private double delta = 0;
	
	/** Toolkit instance **/
	private Toolkit kit = Toolkit.getDefaultToolkit();
	
	/** Constructor **/
	private MainApp() {}
	
	private void Setup() {
		System.out.println("Setting up...");
		gcontroller = GameController.getInstance();
		setupSuccess = true;
	}
	
	private void Run() {
		System.out.println("Starting up!");
		System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("sun.java2d.translaccel", "true");
		System.setProperty("sun.java2d.ddforcevram", "true");
		GameController.running = true;
		GameController.MAIN_MENU.EnterScene();
	    gcontroller.window.setVisible(true);
	    kit.beep();
		long lastLoopTime = System.nanoTime();
		final long OPTIMAL_TIME = 1_000_000_000 / 60;
		try {
			while(GameController.running) {
				long now = System.nanoTime();
			    long updateLength = now - lastLoopTime;
			    lastLoopTime = now;
			    delta = updateLength / ((double)OPTIMAL_TIME);
			    
			    // update the frame counter
			    lastFpsTime += updateLength;
			    FPS++;
			      
			    // update our FPS counter if a second has passed since we last recorded
			    if (lastFpsTime >= 1_000_000_000) {
			         lastFpsTime = 0;
			         FPS = 0;
			    }

				kit.sync();
				gcontroller.display.revalidate();
				gcontroller.display.repaint();
				gcontroller.window.revalidate();
				gcontroller.window.repaint();
				
				Input.mouseX = MouseInfo.getPointerInfo().getLocation().x;
				Input.mouseY = MouseInfo.getPointerInfo().getLocation().y;
				
				if(gcontroller.display.currentScene != null)
					gcontroller.display.currentScene.Update(Input.getInstance(), delta);
				long sleepTime = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000;
				if(sleepTime > 0)
					Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** Main method **/
	public static void main(String[] args) {
		instance = new MainApp();
		instance.Setup();
		if(instance.setupSuccess)
			instance.Run();
		else {
			System.out.println("Error: Unable to setup game.");
			System.exit(0);
		}
	}
}