package com.hopto.dtanjp.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
/**
 * OptionPanel.java
 * 
 * @author David Tan
 **/
public class OptionPanel extends JLayeredPane {

	/** Serial Version UID **/
	private static final long serialVersionUID = 2162870985660432488L;

	/** Buttons **/
	public GameButton menu_btn,
						exit_btn,
						save_btn,
						quit_save_btn,
						resume_btn;
	
	/** Constructor **/
	public OptionPanel() {
		setOpaque(true);
		setLayout(null);
        setDoubleBuffered(true);
		setSize(220, 210);
		setBorder(BorderFactory.createLineBorder(new Color(204, 253, 255), 5, true));
		
		menu_btn = new GameButton("Main menu");
		exit_btn = new GameButton("Exit game");
		save_btn = new GameButton("Save game");
		quit_save_btn = new GameButton("Save & Quit");
		resume_btn = new GameButton("Resume game");
		
		menu_btn.setBounds(10, 10, 200, 30);
		exit_btn.setBounds(10, 50, 200, 30);
		save_btn.setBounds(10, 90, 200, 30);
		quit_save_btn.setBounds(10, 130, 200, 30);
		resume_btn.setBounds(10, 170, 200, 30);
		
		add(menu_btn);
		add(exit_btn);
		add(save_btn);
		add(quit_save_btn);
		add(resume_btn);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paintComponent(g);
	}
}
