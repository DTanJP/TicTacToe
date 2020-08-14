package com.hopto.dtanjp.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hopto.dtanjp.tictactoe.GameController;
/**
 * DisplayPanel.java
 * Contains settings to change display settings
 * 
 * @author David Tan
 **/
public class DisplayPanel extends JPanel {

	/** Serial version UID **/
	private static final long serialVersionUID = 5814609337487108110L;

	/** Buttons **/
	public GameButton apply_btn, close_btn;
	
	/** Labels **/
	private JLabel resolutionLabel;
	
	/** The labels for the drop down box **/
	private static String[] resolutionLabelSizes = {
		"800 x 600",
		"1024 x 768",
		"1152 x 864",
		"1176 x 664",
		"1280 x 768",
		"1280 x 800",
		"1280 x 960",
		"1280 x 1024",
		"1360 x 768",
		"1366 x 768",
		"1600 x 900",
		"1600 x 1024",
		"1680 x 1050",
		"1920 x 1080"
	};
	
	private static Dimension[] resolutionDimensions = {
		new Dimension(800, 600),
		new Dimension(1024, 768),
		new Dimension(1152, 864),
		new Dimension(1176, 664),
		new Dimension(1280, 768),
		new Dimension(1280, 800),
		new Dimension(1280, 960),
		new Dimension(1280, 1024),
		new Dimension(1360, 768),
		new Dimension(1366, 768),
		new Dimension(1600, 900),
		new Dimension(1600, 1024),
		new Dimension(1680, 1050),
		new Dimension(1920, 1080),
	};
	
	/** JCombo box: Drop down select menu **/
	private JComboBox<String> resolutions;
	
	/** Resolution index **/
	private int selected_index = 0;
	
	/** Constructor **/
	public DisplayPanel() {
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(new Color(191, 235, 255), 5, true));
		setLayout(null);
		setVisible(false);
		resolutions = new JComboBox<>(resolutionLabelSizes);
		resolutionLabel = new JLabel("Display resolution:");
		apply_btn = new GameButton("Apply");
		close_btn = new GameButton("Cancel");
		
		resolutions.setBackground(Color.DARK_GRAY);
		resolutions.setForeground(Color.WHITE);
		resolutionLabel.setForeground(Color.WHITE);
		apply_btn.setBackground(new Color(0, 128, 49));
		close_btn.setBackground(new Color(120, 0, 0));
		apply_btn.hoverColor = new Color(189, 255, 214);
		close_btn.hoverColor = new Color(255, 189, 189);
		
		resolutions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selected_index = resolutions.getSelectedIndex();
			}
			
		});
		
		add(resolutions);
		add(resolutionLabel);
		add(apply_btn);
		add(close_btn);
		resolutions.setBounds(15, 90, 120, 20);
		resolutionLabel.setBounds(15, 70, 120, 20);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 48));
		g.drawString("Display settings", getWidth()/2 - g.getFontMetrics().stringWidth("Display settings")/2, 50);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(10, 65, getWidth()-10, 65);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
	    g.drawImage(GameController.assets.monitor_icon, (getWidth()/2) - (getWidth()/4), (getHeight()/2) - (getHeight()/4), getWidth()/2, getHeight()/2, null);
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	    
	    g.setColor(Color.LIGHT_GRAY);
	    g.drawLine(0, apply_btn.getY()-10, getWidth(), apply_btn.getY()-10);
	}
	
	public Dimension getSelectedSize() {
		return resolutionDimensions[selected_index];
	}
}
