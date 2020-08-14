package com.hopto.dtanjp.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
/**
 * GameButton.java
 * 
 * @author David Tan
 **/
public class GameButton extends JButton {

	/** Serial Version UID **/
	private static final long serialVersionUID = 5422009635650715043L;

	/** Clicked: Has this button been clicked? **/
	private boolean clicked = false;
	
	/** Hovering: Is the user hovering over this button? **/
	private boolean hovering = false;
	
	/** The foreground color when hovering on this button **/
	public Color hoverColor = Color.GREEN;
	
	/** Constructor **/
	public GameButton(String title) {
		setText(title);
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		setDoubleBuffered(true);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				hovering = true;
				if(hoverColor != null)
					setForeground(hoverColor);
				setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
				if(isEnabled())
					setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hovering = false;
				setForeground(Color.WHITE);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
		});
		addActionListener(e -> {
			clicked = true;
			setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED));
		});
	}
	
	public void setIconImage(BufferedImage icon) {
		setIcon(new ImageIcon(icon.getScaledInstance(getWidth()-10, getHeight()-10, BufferedImage.SCALE_SMOOTH)));
	}
	
	public boolean isClicked() {
		boolean result = clicked && isVisible() && isEnabled();
		clicked = false;
		return result;
	}
	
	public boolean isHovering() {
		return hovering;
	}
}
