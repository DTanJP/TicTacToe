package com.hopto.dtanjp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hopto.dtanjp.scene.Scene;
/**
 * Display.java
 * 
 * @author David Tan
 **/
public class Display extends JPanel {

	/** Serial version UID **/
	private static final long serialVersionUID = 5176805089796296975L;
	
	/** Current scene **/
	public Scene currentScene = null;
	
	/** Singleton instance **/
	private static Display instance = null;
	
	/** Constructor **/
	private Display() {
		setLayout(null);
		setBounds(0, 0, 800, 600);
		setBackground(Color.BLACK);
		setFocusable(true);
		requestFocusInWindow(true);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g);
	}
	
	/** Singleton **/
	public static Display getInstance() {
		if(instance == null)
			instance = new Display();
		return instance;
	}
}