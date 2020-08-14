package com.hopto.dtanjp.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import com.hopto.dtanjp.tictactoe.GameController;
/**
 * DifficultyButton.java
 * 
 * @author David Tan
 **/
public class DifficultyButton extends JButton {

	/** Serial version UID **/
	private static final long serialVersionUID = -2887557595203609393L;

	public final static int DIFFICULTY_RANDOM = 0;
	public final static int DIFFICULTY_EASY = 1;
	public final static int DIFFICULTY_NORMAL = 2;
	public final static int DIFFICULTY_HARD = 3;
	
	/** Has this button been clicked **/
	private boolean clicked = false;
	
	/** Is the user hovering the cursor above this button **/
	private boolean hovering = false;
	
	/** The toggle status of this button **/
	public boolean toggled = false;
	
	private Color defaultBG = Color.DARK_GRAY, hoverBG = Color.GRAY;
	
	/** The difficulty value of this button **/
	private int value = -1;
	
	private Color random_colors[] = {Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.PINK};
	
	/** Constructor **/
	public DifficultyButton(int difficulty) {
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		value = difficulty;
		switch(value) {
			case DIFFICULTY_EASY:
				defaultBG = new Color(82, 130, 0);
				hoverBG = new Color(216, 255, 150);
				setText("Easy");
				break;
			case DIFFICULTY_NORMAL:
				defaultBG = new Color(163, 163, 0);
				hoverBG = new Color(255, 255, 56);
				setText("Normal");
				break;
			case DIFFICULTY_HARD:
				defaultBG = new Color(92, 0, 0);
				hoverBG = Color.RED;
				setText("Hard");
				break;
			case DIFFICULTY_RANDOM:
				setText("Random");
			default:
				setBackground(Color.DARK_GRAY);
				break;
		}
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(hoverBG);
				hovering = true;
				switch(value) {
					case DIFFICULTY_EASY:
						setForeground(Color.GREEN);
						break;
					case DIFFICULTY_NORMAL:
						setForeground(new Color(171, 168, 0));
						break;
					case DIFFICULTY_HARD:
						setForeground(new Color(168, 0, 20));
						break;
					default:
						setForeground(Color.WHITE);
						break;
				}
				if(value == DIFFICULTY_RANDOM)
					setBackground(random_colors[(int)(Math.random()*random_colors.length)]);
				if(isEnabled())
					setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hovering = false;
				if(value == DIFFICULTY_RANDOM)
					setBackground(random_colors[(int)(Math.random()*random_colors.length)]);
				else
					setBackground(defaultBG);
				setForeground(Color.WHITE);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
		});
		addActionListener(e -> {
			GameController.opponent.difficulty = value;
			clicked = true;
			toggled = true;
		});
		setBackground(defaultBG);
		setForeground(Color.WHITE);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paintComponent(g);
	}
	
	public boolean isClicked() {
		boolean result = clicked;
		clicked = false;
		return result;
	}
	
	
	public boolean isHovering() { return hovering; }
	
	/** Return the value of this button **/
	public int getValue() { return value; }
}
