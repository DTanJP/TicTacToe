package com.hopto.dtanjp.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;

import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.gui.GameButton;
import com.hopto.dtanjp.tictactoe.GameController;
/**
 * CreditScene.java
 * 
 * @author David Tan
 **/
public class CreditScene extends Scene {

	/** Serial version UID **/
	private static final long serialVersionUID = 7633837961378995103L;
	
	/** Main menu button **/
	private GameButton main_menu_btn;
	
	/** Constructor **/
	public CreditScene() {
		setBackground(Color.BLACK);
		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));
		main_menu_btn = new GameButton("<< Main Menu");
		main_menu_btn.setBounds(10, 20, 100, 30);
		add(main_menu_btn);
	}
	
	@Override
	public void Update(Input input, double delta) {
		if(main_menu_btn.isClicked())
			GameController.MAIN_MENU.EnterScene();
	}

	@Override
	public void Render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 32));
		centerString(g, "- Credits -", 50);
		g.drawLine(10, 60, getWidth()-10, 60);
		
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		centerString(g, "Developer - David Tan - https://github.com/DTanJP", 100);
		centerString(g, "StackOverFlow", 140);
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		centerString(g, "Audio", 170);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		centerString(g, "Main Menu Music - Chadderbox - https://chadderbox.itch.io", 190);
	}

	private void centerString(Graphics2D g, String line, int y) {
		g.drawString(line, (getWidth()/2) - (g.getFontMetrics().stringWidth(line)/2), y);
	}
}
