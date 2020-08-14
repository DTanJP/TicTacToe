package com.hopto.dtanjp.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.stream.Stream;

import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.gui.AudioPanel;
import com.hopto.dtanjp.gui.DisplayPanel;
import com.hopto.dtanjp.gui.ExpandBar;
import com.hopto.dtanjp.gui.GameButton;
import com.hopto.dtanjp.particles.FallingParticle;
import com.hopto.dtanjp.particles.Particle;
import com.hopto.dtanjp.tictactoe.GameController;
/**
 * MainMenu.java
 * 
 * @author David Tan
 **/
public class MainMenu extends Scene {

	/** Serial Version UID **/
	private static final long serialVersionUID = -4164769735827931743L;
	
	/** Particles **/
	private Particle[] particles = new Particle[100];
	
	/** Menu buttons **/
	private GameButton start_btn, exit_btn, instruction_btn, credits_btn, settings_btn, resume_btn;
	
	/** Settings bar: buttons **/
	private GameButton display_btn, audio_btn;
	
	/** DisplayPanel: Settings bar -> Display button**/
	private DisplayPanel displayPanel;
	
	/** AudioPanel: Settings bar -> Audio button **/
	private AudioPanel audioPanel;
	
	/** Settings button: Expand bar instance **/
	private ExpandBar settings_bar;
	
	/** isLoaded **/
	private boolean loaded = false;
	
	private float currentFade = 1.0f;
	
	/** Constructor **/
	public MainMenu() {
		setLayout(null);
		setBackground(Color.BLACK);
		
		start_btn = new GameButton("Start");
		resume_btn = new GameButton("Resume game");
		instruction_btn = new GameButton("Instructions");
		credits_btn = new GameButton("Credits");
		exit_btn = new GameButton("Exit");
		settings_btn = new GameButton("");
		display_btn = new GameButton("");
		audio_btn = new GameButton("");
		settings_bar = new ExpandBar();
		
		displayPanel = new DisplayPanel();
		audioPanel = new AudioPanel();
		
		start_btn.hoverColor = Color.CYAN;
		exit_btn.hoverColor = Color.RED;

		settings_bar.setBackground(getBackground());
		settings_bar.addButton(display_btn);
		settings_bar.addButton(audio_btn);
		
		add(displayPanel);
		add(audioPanel);
		add(start_btn);
		add(instruction_btn);
		add(credits_btn);
		add(settings_bar);
		add(settings_btn);
		add(exit_btn);
		add(resume_btn);
	}
	
	@Override
	public void OnEnter() {
		settings_bar.resetComponent();
		
		settings_btn.setBounds(getWidth()-60, getHeight()-60, 50, 50);
		settings_bar.setBounds(getWidth()-(60+settings_bar.getWidth()), getHeight()-(60+settings_bar.margin_padding+6), 1, 50+(settings_bar.margin_padding*2));
		displayPanel.setBounds(getWidth()/2 - 250, getHeight()/2 - 200, 500, 400);
		audioPanel.setBounds(getWidth()/2 - 250, getHeight()/2 - 200, 500, 400);
		
		resume_btn.setVisible(GameController.getInstance().saveController.saveFileExists());
			
		GameButton[] btns = Stream.of(start_btn, resume_btn, instruction_btn, credits_btn, exit_btn).filter(btn -> btn.isVisible()).toArray(GameButton[]::new);
		for(int i=0; i<btns.length; i++)
			btns[i].setBounds(getWidth()/2 - 60, 300 + (i*30), 120, 25);
		
		displayPanel.apply_btn.setBounds((displayPanel.getWidth()/2) - 55, displayPanel.getHeight()-40, 50, 30);
		displayPanel.close_btn.setBounds((displayPanel.getWidth()/2) + 5, displayPanel.getHeight()-40, 50, 30);
		
		audioPanel.close_btn.setBounds((audioPanel.getWidth()/2) - 25, audioPanel.getHeight()-40, 50, 30);
		
		settings_bar.setVisible(false);
		displayPanel.setVisible(false);
		start_btn.setVisible(true);
		instruction_btn.setVisible(true);
		credits_btn.setVisible(true);
		exit_btn.setVisible(true);
		
		if(!loaded) {
			Collections.shuffle(GameController.assets.particles);
			//Initiate particles
			for(int i=0; i<particles.length; i++)
				particles[i] = new FallingParticle(GameController.assets.particles.get((int)(Math.random() * GameController.assets.particles.size())));
			
			settings_btn.setIconImage(GameController.assets.settings_icon);
			display_btn.setIconImage(GameController.assets.monitor_icon);
			audio_btn.setIconImage(GameController.assets.speaker_icon);
		}
		
		//Setup particles positions
		for(int i=0; i<particles.length; i++) {
			particles[i].x = (int) (particles[i].width + (Math.random() * (getWidth()-particles[i].width)));
			particles[i].y = (float) (Math.random() * getHeight()/2) - particles[i].height;
		}
		
		//Background music for the main menu
		if(GameController.background_music != null)
			GameController.background_music.stop();
		GameController.background_music = GameController.assets.main_menu_bgm;
		GameController.background_music.loop = true;
		GameController.background_music.play();
		loaded = true;
	}
	
	@Override
	public void Render(Graphics2D g) {
		for(Particle p : particles)
			p.render(g);
		BufferedImage title = GameController.assets.title_icon;
		if(title != null)
			g.drawImage(title, ((getWidth()/2) - (title.getWidth()/2)), (title.getHeight()+ 30), null);
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paint(g);
		if(currentFade > 0.005f) {
			currentFade = currentFade - 0.005f;
			g.setColor(new Color(0, 0, 0, currentFade));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	@Override
	public void OnExit() {
		displayPanel.setVisible(false);
		settings_bar.collapse();
		GameController.assets.main_menu_bgm.stop();
		GameController.background_music = null;
	}
	
	@Override
	public void Update(Input input, double delta) {
		for(Particle p : particles) {
			p.process();
			if(p.y >= getHeight())
				p.x = (int) (p.width + (Math.random() * (getWidth()-p.width)));
		}
		
		if(settings_btn.isClicked() && !settings_bar.isExpanding()) {
			if(settings_bar.isExpanded()) {
				settings_bar.setBounds(getWidth()-(60+settings_bar.getWidth()), getHeight()-(60+settings_bar.margin_padding+6), settings_bar.getExpandedWidth(), 50+(settings_bar.margin_padding*2));
				settings_bar.collapse();
			} else {
				settings_bar.setBounds(getWidth()-60, getHeight()-(60+settings_bar.margin_padding+6), 1, 50+(settings_bar.margin_padding*2));
				settings_bar.expand();
			}
		}
		settings_bar.process();
		if(settings_bar.isExpanded() && !settings_bar.isExpanding())
			settings_bar.setLocation(getWidth() - (settings_bar.getExpandedWidth()+60), settings_bar.getY());
		if(start_btn.isClicked())
			EnterScene(GameController.SELECT_TYPE_SCENE);
		else if(credits_btn.isClicked())
			GameController.CREDIT_SCENE.EnterScene();
		else if(instruction_btn.isClicked())
			GameController.INSTRUCTION_SCENE.EnterScene();
		else if(audio_btn.isClicked() || audioPanel.close_btn.isClicked()) {
			audioPanel.setVisible(!audioPanel.isVisible());
			start_btn.setVisible(!audioPanel.isVisible());
			instruction_btn.setVisible(!audioPanel.isVisible());
			credits_btn.setVisible(!audioPanel.isVisible());
			exit_btn.setVisible(!audioPanel.isVisible());
		} else if(displayPanel.apply_btn.isClicked()) {
			GameController.getInstance().changeResolution(displayPanel.getSelectedSize());
			if(settings_bar.isExpanded() && !settings_bar.isExpanding())//Expanded
				settings_bar.setBounds(getWidth()-(60+settings_bar.getWidth()), getHeight()-(60+settings_bar.margin_padding+6), settings_bar.getExpandedWidth(), 50+(settings_bar.margin_padding*2));
			else if(!settings_bar.isExpanded() && !settings_bar.isExpanding())//Collapsed
				settings_bar.setBounds(getWidth()-60, getHeight()-(60+settings_bar.margin_padding+6), 1, 50+(settings_bar.margin_padding*2));
		} else if(display_btn.isClicked() || displayPanel.close_btn.isClicked()) {
			displayPanel.setVisible(!displayPanel.isVisible());
			start_btn.setVisible(!displayPanel.isVisible());
			instruction_btn.setVisible(!displayPanel.isVisible());
			credits_btn.setVisible(!displayPanel.isVisible());
			exit_btn.setVisible(!displayPanel.isVisible());
		} else if(exit_btn.isClicked())
			System.exit(0);
		else if(resume_btn.isClicked()) {
			GameController.getInstance().saveController.load();
			GameController.GAME_SCENE.EnterScene();
		}
	}
}