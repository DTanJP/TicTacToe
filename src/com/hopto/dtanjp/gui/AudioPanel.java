package com.hopto.dtanjp.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hopto.dtanjp.tictactoe.GameController;
/**
 * AudioPanel.java
 * 
 * @author David Tan
 **/
public class AudioPanel extends JPanel {

	/** Serial version UID **/
	private static final long serialVersionUID = -1334712504158247163L;

	/** Volume slider **/
	private JSlider volume = null;
	
	/** Close button: closes this panel **/
	public GameButton close_btn = null;
	
	private JLabel bgm_label;
	
	/** Constructor **/
	public AudioPanel() {
		setLayout(null);
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(new Color(157, 255, 150), 5));
		setLayout(null);
		setVisible(false);
		
		close_btn = new GameButton("Close");
		volume = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		bgm_label = new JLabel("BGM Volume");
		
		add(volume);
		add(close_btn);
		add(bgm_label);
		
		volume.setBackground(Color.DARK_GRAY);
		volume.setBounds(10, 100, 100, 20);
		volume.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				GameController.background_music.setVolume(volume.getValue()/100f);
			}
			
		});
		close_btn.hoverColor = Color.RED;
		bgm_label.setForeground(Color.WHITE);
		bgm_label.setBounds(18, 75, 100, 20);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("Audio settings", getWidth()/2 - g.getFontMetrics().stringWidth("Audio settings")/2, 50);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(10, 65, getWidth()-10, 65);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
	    g.drawImage(GameController.assets.speaker_icon, (getWidth()/2) - (getWidth()/4), (getHeight()/2) - (getHeight()/4), getWidth()/2, getHeight()/2, null);
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	    
	    g.setColor(Color.LIGHT_GRAY);
	    g.drawLine(0, close_btn.getY()-10, getWidth(), close_btn.getY()-10);
	}
	
}