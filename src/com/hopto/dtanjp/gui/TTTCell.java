package com.hopto.dtanjp.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.hopto.dtanjp.tictactoe.GameController;

/**
 * TTTCell.java
 * Tic Tac Toe Cell - Contains either an X or an O
 * @author David Tan
 **/
public class TTTCell extends JButton {

	/** Serial version UID **/
	private static final long serialVersionUID = -1442236928074773640L;

	/** Cell value **/
	protected String value = "";
	
	/** The value of the cell when being hovered upon **/
	public String hoverValue = "";
	
	/** Clicked: Is this cell clicked on? **/
	private boolean clicked = false;
	
	/** Hovering: Is this cell being hovered on? **/
	private boolean hovering = false;
	
	/** Cell ID: **/
	public int cellID = -1;
	
	/** Constructor **/
	public TTTCell() {
		setOpaque(true);
        setDoubleBuffered(true);
		setBackground(Color.WHITE);
		/** Listen for hovering **/
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				hovering = true;
				hoverValue = GameController.PLAYER_VALUE;
				if(isEnabled())
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				if(value.equals("")) {
					if(hoverValue.equalsIgnoreCase("X"))
						setIcon(new ImageIcon(GameController.assets.x_imgs[0]));
					else if(hoverValue.equalsIgnoreCase("O"))
						setIcon(new ImageIcon(GameController.assets.o_imgs[0]));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hovering = false;
				hoverValue = "";
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if(value.equalsIgnoreCase(""))
					setIcon(null);
			}
			
		});
		/** Listen for clicks **/
		addActionListener(action -> {
			clicked = true;
			if(GameController.PLAYER_TURN && GameController.determineWin() == null) {
				if(value == "") {
					value = GameController.PLAYER_VALUE;
					GameController.PLAYER_MOVES_COUNT++;
					GameController.processRound();
					setValue(value);
					hoverValue = "";
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paintComponent(g);
		
		ImageIcon mark = null;
		if(!value.equals("")) {
			mark = value.equalsIgnoreCase("X") 
					? new ImageIcon(GameController.assets.x_imgs[0].getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH)) 
					: new ImageIcon(GameController.assets.o_imgs[0].getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH));
		} else {
			if(!hoverValue.equals("")) {
				mark = hoverValue.equalsIgnoreCase("X") 
						? new ImageIcon(GameController.assets.x_imgs[0].getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH)) 
						: new ImageIcon(GameController.assets.o_imgs[0].getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH));
			}
		}
		setIcon(mark);
	}
	
	public void setValue(String value) {
		if(!this.value.equals("")) return;//Cannot replace the value here. The TTTGrid must reset it.
		if(!this.value.equalsIgnoreCase("")) return; //The grid has to reset. Cannot edit an individual cell.
		if(value.equalsIgnoreCase("X") || value.equalsIgnoreCase("O")) {
			this.value = value;
			if(value.equalsIgnoreCase("X"))
				setIcon(new ImageIcon(GameController.assets.x_imgs[0].getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH)));
			else
				setIcon(new ImageIcon(GameController.assets.o_imgs[0].getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH)));
		}
	}
	
	public String getValue() { return value; }
	
	public boolean isClicked() {
		boolean result = clicked;
		clicked = false;
		return result;
	}
	
	public boolean isHovering(String value) {
		value = (value.equalsIgnoreCase("X") || value.equalsIgnoreCase("O")) ? value : ""; 
		hoverValue = (hovering) ? value : "";
		return hovering;
	}
}