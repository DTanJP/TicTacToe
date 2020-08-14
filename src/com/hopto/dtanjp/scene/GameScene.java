package com.hopto.dtanjp.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.gui.GameButton;
import com.hopto.dtanjp.gui.OptionPanel;
import com.hopto.dtanjp.gui.TTTCell;
import com.hopto.dtanjp.gui.TTTGrid;
import com.hopto.dtanjp.tictactoe.GameController;

public class GameScene extends Scene {

	/** Serial Version UID **/
	private static final long serialVersionUID = -7759498221557922648L;
	
	/** Tic Tac Toe Grid instance **/
	private TTTGrid grid = null;
	
	/** Winner **/
	private String winner = null;
	
	/** New round button **/
	private GameButton restart_btn = null;
	
	/** Return to the main menu **/
	private GameButton menu_btn = null;
	
	/** Options button **/
	private GameButton option_btn = null;
	
	/** Option panel **/
	private OptionPanel option_panel = null;
	
	/** The fade in opacity on entering this scene **/
	private float currentFade = 1.0f;
	
	/** Constructor **/
	public GameScene() {
		setLayout(null);
        setIgnoreRepaint(true);
        setDoubleBuffered(true);
		setBackground(Color.LIGHT_GRAY);
		restart_btn = new GameButton("New round");
		restart_btn.setVisible(false);
		restart_btn.setEnabled(false);
		
		menu_btn = new GameButton("Main menu");
		menu_btn.setVisible(false);
		menu_btn.setEnabled(false);
		
		option_btn = new GameButton("");
		option_btn.setIconImage(GameController.assets.options_icon);
		
		option_panel = new OptionPanel();
		option_panel.setVisible(false);
		option_panel.setDoubleBuffered(true);
		
		add(restart_btn);
		add(menu_btn);
		add(option_btn);
		add(option_panel, 50);
	}
	
	@Override
	public void OnEnter() {
		currentFade = 1.0f;
		option_panel.setVisible(false);
		if(grid != null) remove(grid);//Remove the old grid if it exists
		
		GameController.GRID = GameController.getInstance().saveController.getGrid();
		grid = GameController.GRID;
		grid.setVisible(false);
		add(grid);
		
		/** Center the GRID **/
		GameController.GRID.setLocation((getWidth()/2)-(grid.getWidth()/2), (getHeight()/2)-(grid.getHeight()/2));

		restart_btn.setBounds(getWidth()/2 - 105, grid.getY()+grid.getHeight()+10, 100, 30);
		menu_btn.setBounds(getWidth()/2 + 5, grid.getY()+grid.getHeight()+10, 100, 30);
		option_btn.setBounds(10, 10, 50, 50);
		option_panel.setLocation(getWidth()/2 - option_panel.getWidth()/2, getHeight()/2 - option_panel.getHeight()/2);
	}
	
	@Override
	public void Update(Input input, double delta) {
		if(currentFade > 0f) return;
		
		winner = GameController.determineWin();
		if(winner == null && GameController.getAvailableMovesLeft() > 0) {
			if(getBackground() != Color.LIGHT_GRAY)
				setBackground(Color.LIGHT_GRAY);
			if(!GameController.PLAYER_TURN) {
				//Round is bugged
				if(GameController.opponent == null) {
					GameController.MAIN_MENU.EnterScene();
					return;
				}
				GameController.opponent.process();
				return;
			}
			for(TTTCell cell : grid.cells) {
				if(cell.getValue().equals("")) {
					cell.setEnabled(true);
					cell.isHovering(GameController.PLAYER_VALUE);
				}
			}
		} else {
			restart_btn.setVisible(true);
			restart_btn.setEnabled(true);
			menu_btn.setVisible(true);
			menu_btn.setEnabled(true);
		}
		
		if(restart_btn.isClicked()) {
			grid.resetGrid();
			GameController.PLAYER_MOVES_COUNT = 0;
			GameController.OPPONENT_MOVES_COUNT = 0;
			restart_btn.setVisible(false);
			restart_btn.setEnabled(false);
			menu_btn.setVisible(false);
			menu_btn.setEnabled(false);
			restart_btn.setBounds(getWidth()/2 - 105, grid.getY()+grid.getHeight()+10, 100, 30);
			setBackground(Color.LIGHT_GRAY);
		} else if(option_btn.isClicked()) {
			option_panel.setVisible(!option_panel.isVisible());
			grid.setEnabled(!option_panel.isVisible());
		} else if(option_panel.exit_btn.isClicked())
			System.exit(0);
		else if(option_panel.resume_btn.isClicked()) {
			option_panel.setVisible(false);
			grid.setEnabled(true);
		} else if(option_panel.save_btn.isClicked()) {
			GameController.getInstance().saveController.save();
			option_panel.setVisible(false);
			grid.setEnabled(true);
		} else if(option_panel.quit_save_btn.isClicked()) {
			GameController.getInstance().saveController.save();
			option_panel.setVisible(false);
			grid.setEnabled(true);
			GameController.MAIN_MENU.EnterScene();
		}
		
		if(input.isKeyReleased(KeyEvent.VK_ESCAPE) || menu_btn.isClicked() || option_panel.menu_btn.isClicked())
			GameController.MAIN_MENU.EnterScene();
	}
	
	@Override
	public void Render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 32));
		g.drawString("Player wins: "+GameController.PLAYER_SCORE, 10, getHeight()-10);
		String ai_score = "AI wins: "+GameController.AI_SCORE;
		g.drawString(ai_score, getWidth() - (g.getFontMetrics().stringWidth(ai_score)+10), getHeight()-10);
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 32));
		if(winner != null) {
			if(winner.equalsIgnoreCase(GameController.PLAYER_VALUE)) {
				setBackground(new Color(107, 240, 255));
				g.setColor(new Color(255, 234, 0, 100));//Transparent gold
				g.fillRect(0, 30, getWidth(), 60);
				g.setColor(new Color(255, 234, 0));//Gold
				centerString(g, "Winner", 70);
			} else if(winner.equalsIgnoreCase(GameController.getOpponentValue())) {
				setBackground(Color.BLACK);
				g.setColor(new Color(255, 0, 0, 100));//Transparent red
				g.fillRect(0, 30, getWidth(), 60);
				g.setColor(new Color(255, 0, 0));//Red
				centerString(g, "You lost", 70);
			}
		} else {
			if(GameController.getAvailableMovesLeft() == 0) {
				setBackground(Color.LIGHT_GRAY);
				g.setColor(Color.BLACK);
				g.fillRect(0, 30, getWidth(), 60);
				g.setColor(Color.WHITE);
				centerString(g, "It's a tie", 70);
			}
		}
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		super.paint(g);
		
		if(currentFade > 0.005f) {
			currentFade = currentFade - 0.005f;
			g.setColor(new Color(0, 0, 0, currentFade));
			g.fillRect(0, 0, getWidth(), getHeight());
		} else {
			grid.setVisible(true);
			currentFade = 0f;
		}
	}
	
	private void centerString(Graphics2D g, String line, int y) {
		g.drawString(line, (getWidth()/2) - (g.getFontMetrics().stringWidth(line)/2), y);
	}
	
}