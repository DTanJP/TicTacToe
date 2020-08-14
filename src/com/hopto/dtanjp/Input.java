package com.hopto.dtanjp;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
/**
 * Input.java
 * Description: Handles the Mouse + Keyboard listener
 * 
 * @author David Tan
 **/
public class Input extends KeyAdapter implements MouseListener, KeyListener, MouseWheelListener {

	/** Variables **/
	private static Input instance = null;
	
	/** Input Events **/
	private KeyEvent keyEvent = null;
	private MouseEvent mouseEvent = null;
	private MouseWheelEvent wheelEvent = null;
	
	public static int mouseX = 0, mouseY = 0;
	private boolean mousePressed = false, mouseDown = false, mouseReleased = false;
	private boolean[] keyPushed = new boolean[1024];
	private boolean[] keyPressed = new boolean[1024];
	private int releaseChar = -1;
	
	/** Constructor **/
	private Input() {}

	/** Singleton **/
	public static Input getInstance() {
		if(instance == null)
			instance = new Input();
		return instance;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyEvent = e;
		if(!keyPushed[e.getKeyCode()])
			keyPressed[e.getKeyCode()] = true;
		keyPushed[e.getKeyCode()] = true;
		releaseChar = -1;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyEvent = e;
		releaseChar = e.getKeyCode();
		keyPushed[e.getKeyCode()] = false;
	}
	
	@Override public void mouseWheelMoved(MouseWheelEvent e) { wheelEvent = e; }

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseEvent = e;
		mousePressed = true;
		mouseDown = true;
		mouseReleased = true;
	}

	@Override public void mouseEntered(MouseEvent e) { mouseEvent = e; }
	@Override public void mouseExited(MouseEvent e) { mouseEvent = e; }

	@Override
	public void mousePressed(MouseEvent e) {
		mouseEvent = e;
		mousePressed = true;
		mouseDown = true;
		mouseReleased = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseEvent = e;
		mousePressed = false;
		mouseDown = false;
		mouseReleased = true;
	}
	
	/** Mouse functions **/
	public boolean mouseClicked() {
		boolean event = (mouseEvent != null) ? ((mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) && mousePressed) : false;
		mousePressed = false;
		return event;
	}
	
	public boolean mouseClicked(int mX, int mY) { return (mouseClicked() && (mouseX == mX && mouseY == mY)); }
	
	public boolean mouseClickedArea(int minX, int minY, int maxX, int maxY) {
		return mouseClicked() && (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	
	public boolean mouseEntered() { return (mouseEvent != null) ? mouseEvent.getID() == MouseEvent.MOUSE_ENTERED : false; }
	public boolean mouseExited() { return (mouseEvent != null) ? mouseEvent.getID() == MouseEvent.MOUSE_EXITED : false; }
	public boolean mousePressed() { return (mouseEvent != null) ? (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED && mouseDown) : false; }

	public boolean mouseReleased() {
		boolean event = (mouseEvent != null) ? mouseReleased : false;
		mouseReleased = false;
		return event;
	}
	
	public boolean mouseReleased(int mX, int mY) { return mouseReleased() && (mouseX == mX && mouseY == mY); }
	
	public boolean mouseReleasedArea(int minX, int minY, int maxX, int maxY) { 
		return mouseReleased() && (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY);
	}
	/** End of Mouse functions **/
	/** Keyboard functions **/
	/** Checks if the key was press/release/down -> remove it **/
	public boolean isKeyDown(int key) { return (key >= 0 && key < 1024) ? (keyPushed[key]) : false; }

	public boolean isKeyReleased(int key) {
		boolean event = (key == releaseChar);
		if(event) releaseChar = -1;
		return event;
	}

	public boolean isKeyPressed(int key) {
		boolean event = false;
		if(key >= 0 && key < 1024) {
			event = keyPressed[key];
			keyPressed[key] = false;
		}
		return event;
	}
	/** End of Keyboard functions **/
	/** Getters **/
	public KeyEvent getKeyEvent() { return keyEvent; }
	public MouseEvent getMouseEvent() { return mouseEvent; }
	public MouseWheelEvent getWheelEvent() { return wheelEvent; }
}
