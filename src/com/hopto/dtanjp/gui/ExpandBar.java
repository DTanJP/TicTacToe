package com.hopto.dtanjp.gui;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * ExpandBar.java
 * Description: Used for the settings button.
 * Clicking on this component expands it and shows off inner components inside
 * Toggleable to open/close.
 * 
 * @author David Tan
 **/
public class ExpandBar extends JPanel {

	/** Serial version UID **/
	private static final long serialVersionUID = -1502857407770307749L;

	/** Is this component expanded **/
	private boolean expanded = false;
	
	/** The margin space between each component **/
	public final int margin_padding = 10;
	
	/** The vector for the expandbar width growth/shrink **/
	private int dx = 0;//1 to expand, 0 to do nothing, -1 to shrink
	private int vx = 3;//The velocity of dx
	
	/** Constructor **/
	public ExpandBar() {
		setLayout(null);
		setVisible(false);
	}
	
	public void process() {
		if(dx != 0) {
			int newX = getX()-(dx*vx);
			int newWidth = getWidth()+(dx*vx);
			setBounds(newX, getY(), newWidth, getHeight());
			if(dx == 1 && getWidth() >= getExpandedWidth()) {
				dx = 0;
				setSize(getExpandedWidth(), getHeight());//Just to be safe. I calibrate it for accuracy.
				expanded = true;
			}
			if(dx == -1 && getWidth() <= 1) {
				dx = 0;
				setSize(1, getHeight());
				expanded = false;
				setBackground(getParent().getBackground());
				setBorder(BorderFactory.createEmptyBorder());
			}
		}
	}
	
	public void addButton(GameButton btn) {
		if(btn == null) return;
		btn.setBounds((getComponents().length*(50+margin_padding))+margin_padding, margin_padding, 50, 50);
		add(btn);
	}
	
	/** Does the expanding animation **/
	public void expand() {
		if(expanded || getComponents().length == 0 || dx != 0) return;//Already expanded
		setSize(1, getHeight());
		setBackground(new Color(191, 235, 255, 80));
		setBorder(BorderFactory.createLineBorder(new Color(191, 235, 255), 3));
		setVisible(true);
		dx = 1;
	}
	
	/** Does the collapsing animation **/
	public void collapse() {
		if(!expanded || dx != 0) return;//Already collapsed
		dx = -1;
	}
	
	public int getExpandedWidth() {
		return (int)(Arrays.stream(getComponents()).filter(c -> c.isVisible()).count()*(50+margin_padding))+margin_padding;
	}
	
	public void resetComponent() {
		expanded = false;
		dx = 0;
		setSize(1, getHeight());
	}
	
	/** Returns the status condition of this component **/
	public boolean isExpanded() { return expanded; }
	public boolean isExpanding() { return dx != 0; }
}
