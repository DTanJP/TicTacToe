package com.hopto.dtanjp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Window.java
 * 
 * @author David Tan
 **/
public class Window extends JFrame {

	/** Serial version UID **/
	private static final long serialVersionUID = -1439175345633051960L;
	
	/** Singleton instance **/
	private static Window instance = null;
	
	/** Toolkit instance **/
	private Toolkit kit = Toolkit.getDefaultToolkit();
	
	/** Constructor **/
	private Window() {
		setTitle("Tic - Tac - Toe");
		setSize(new Dimension(800, 600));
		setPreferredSize(getSize());
		setResizable(false);
		setLayout(null);
		//Center the window
		setLocation((kit.getScreenSize().width/2) - 400, (kit.getScreenSize().height/2) - 300);
		setUndecorated(true);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/** Singleton **/
	public static Window getInstance() {
		if(instance == null)
			instance = new Window();
		return instance;
	}
}