package com.hopto.dtanjp.scene;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLayeredPane;

import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.tictactoe.GameController;
/**
 * Scene.java
 * 
 * @author David Tan
 **/
public abstract class Scene extends JLayeredPane {

	/** Serial version UID **/
	private static final long serialVersionUID = 8500651598978836111L;
	
	/** Abstract methods **/
	public abstract void Update(Input input, double delta);
	public abstract void Render(Graphics2D g);
	
	/** Transition methods **/
	public void OnEnter() {}
	public void OnExit() {}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g);
		Render(g);
	}
	
	/** Enter another scene **/
	public void EnterScene(Scene scene) {
		GameController gc = GameController.getInstance();
		gc.EnterScene(scene);
	}
	
	/** Enter this scene **/
	public void EnterScene() {
		GameController gc = GameController.getInstance();
		gc.EnterScene(this);
	}
}