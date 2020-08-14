package com.hopto.dtanjp.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
/**
 * SelectMarkButton.java
 * 
 * @author David Tan
 **/
public class SelectMarkButton extends JButton {

	/** Serial version UID **/
	private static final long serialVersionUID = -4811142796212074815L;

	/** Clicked: Has this button been clicked? **/
	private boolean clicked = false;
	
	/** Hovering: Is the user hovering over this button? **/
	private boolean hovering = false;
	
	private Type value = Type.NONE;
	
	/** Is this button toggled on **/
	public boolean toggled = false;
	
	public enum Type {
		X, O, NONE
	}
	
	/** Constructor **/
	public SelectMarkButton(Type type) {
		setText("");
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		value = type;
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				toggled = !toggled;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				hovering = true;
				if(isEnabled())
					setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hovering = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
		});
		addActionListener(e -> {
			clicked = true;
			toggled = true;
		});
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paintComponent(g);
		setBackground(getParent().getBackground());
		if(value == Type.O) {
			if(isHovering() || toggled) {
				setBackground(new Color(237, 255, 250));
				g.setColor(new Color(181, 255, 138));
			} else
				g.setColor(new Color(181, 255, 138, 100));
			g.setStroke(new BasicStroke(5));
			g.drawOval(25, 25, getWidth()-50, getHeight()-50);
		} else if(value == Type.X) {
			if(isHovering() || toggled) {
				setBackground(new Color(255, 227, 227));
				g.setColor(Color.RED);
			} else
				g.setColor(new Color(255, 0, 0, 100));
			g.setStroke(new BasicStroke(5));
			g.drawLine(10, 10, getWidth()-10, getHeight()-10);
			g.drawLine(10, getHeight()-10, getWidth()-10, 10);
		}
	}
	
	public boolean isClicked() {
		boolean result = clicked;
		clicked = false;
		return result;
	}
	
	public boolean isHovering() {
		return hovering;
	}
}
