package com.hopto.dtanjp.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.gui.DifficultyButton;
import com.hopto.dtanjp.gui.GameButton;
import com.hopto.dtanjp.gui.SelectMarkButton;
import com.hopto.dtanjp.tictactoe.GameController;
import com.hopto.dtanjp.tictactoe.OpponentAI;
import com.hopto.dtanjp.tictactoe.SaveManager;
/**
 * SelectTypeScene.java
 * 
 * @author David Tan
 **/
public class SelectTypeScene extends Scene {

	/** Serial version UID **/
	private static final long serialVersionUID = -7600323208332730169L;

	/** Main menu button **/
	private GameButton main_menu_btn, start_game_btn;
	
	/** Difficulty selector **/
	private DifficultyButton easy_btn, normal_btn, hard_btn, random_btn;
	
	/** Value buttons **/
	private SelectMarkButton o_btn, x_btn;
	
	private final String difficulty_mode[] = {"Random", "Easy", "Normal", "Hard"};
	
	private final String mode_description[] = {
		"Your opponent picks random spots",
		"Your opponent only tries to connects 3 in a row",
		"Your opponent will try to stalemate to prevent you from winning",
		"Your opponent analyzes every possible move"
	};
	
	private int hovering_mode = -1;
	
	/** Constructor **/
	public SelectTypeScene() {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		main_menu_btn = new GameButton("Main menu");
		start_game_btn = new GameButton("Start game");
		o_btn = new SelectMarkButton(SelectMarkButton.Type.O);
		x_btn = new SelectMarkButton(SelectMarkButton.Type.X);
		easy_btn = new DifficultyButton(DifficultyButton.DIFFICULTY_EASY);
		normal_btn = new DifficultyButton(DifficultyButton.DIFFICULTY_NORMAL);
		hard_btn = new DifficultyButton(DifficultyButton.DIFFICULTY_HARD);
		random_btn = new DifficultyButton(DifficultyButton.DIFFICULTY_RANDOM);
		
		start_game_btn.setBackground(new Color(179, 255, 102));
		start_game_btn.setForeground(new Color(48, 158, 32));
		start_game_btn.setEnabled(false);
		start_game_btn.setVisible(false);
		
		add(main_menu_btn);
		add(start_game_btn);
		add(x_btn);
		add(o_btn);
		add(easy_btn);
		add(normal_btn);
		add(hard_btn);
		add(random_btn);
	}
	
	@Override
	public void OnEnter() {
		GameController.PLAYER_VALUE = "";
		x_btn.toggled = false;
		o_btn.toggled = false;
		
		main_menu_btn.setBounds(10, 20, 100, 25);
		x_btn.setBounds((getWidth()/2) - 210, 250, 200, 200);
		o_btn.setBounds((getWidth()/2) + 10, 250, 200, 200);
		start_game_btn.setBounds(getWidth()/2 - 50, 500, 100, 30);
		
		easy_btn.setBounds(getWidth()/2 - 210, 100, 100, 30);
		normal_btn.setBounds(getWidth()/2 - 100, 100, 100, 30);
		hard_btn.setBounds(getWidth()/2 + 10, 100, 100, 30);
		random_btn.setBounds(getWidth()/2 + 120, 100, 100, 30);
		GameController.opponent = new OpponentAI();
	}
	
	@Override
	public void Update(Input input, double delta) {
		hovering_mode = -1;
		if(o_btn.isClicked()) {
			GameController.PLAYER_VALUE = "O";
			o_btn.toggled = true;
			x_btn.toggled = false;
		} else if(x_btn.isClicked()) {
			GameController.PLAYER_VALUE = "X";
			x_btn.toggled = true;
			o_btn.toggled = false;
		} else if(main_menu_btn.isClicked())
			GameController.MAIN_MENU.EnterScene();
		else if(GameController.PLAYER_VALUE != "" && GameController.opponent.difficulty != -1 && start_game_btn.isClicked()) {
			if(GameController.GRID != null)
				GameController.GRID.resetGrid();
			SaveManager.getInstance().getGrid().resetGrid();
			GameController.OPPONENT_MOVES_COUNT = 0;
			GameController.PLAYER_MOVES_COUNT = 0;
			GameController.AI_SCORE = 0;
			GameController.PLAYER_SCORE = 0;
			GameController.GAME_SCENE.EnterScene();
		} else if(easy_btn.isHovering())
			hovering_mode = easy_btn.getValue();
		else if(normal_btn.isHovering())
			hovering_mode = normal_btn.getValue();
		else if(hard_btn.isHovering())
			hovering_mode = hard_btn.getValue();
		else if(random_btn.isHovering())
			hovering_mode = random_btn.getValue();
		
		start_game_btn.setEnabled(!GameController.PLAYER_VALUE.equals("") && GameController.opponent.difficulty != -1);
		start_game_btn.setVisible(!GameController.PLAYER_VALUE.equals("") && GameController.opponent.difficulty != -1);
	}

	@Override
	public void Render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 32));
		g.drawString("Select difficulty", (getWidth()/2) - (g.getFontMetrics().stringWidth("Select difficulty")/2), 50);
		
		if(hovering_mode != -1) {
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			String difficulty = difficulty_mode[hovering_mode]+": "+mode_description[hovering_mode];
			g.drawString(difficulty, getWidth()/2 - g.getFontMetrics().stringWidth(difficulty)/2, 150);
			g.setFont(new Font("Arial", Font.BOLD, 32));
		} else {
			//Difficulty is set: Display the description
			if(GameController.opponent != null) {
				if(GameController.opponent.difficulty != -1) {
					g.setFont(new Font("Arial", Font.PLAIN, 20));
					String difficulty = difficulty_mode[GameController.opponent.difficulty]+": "+mode_description[GameController.opponent.difficulty];
					g.drawString(difficulty, getWidth()/2 - g.getFontMetrics().stringWidth(difficulty)/2, 150);
					g.setFont(new Font("Arial", Font.BOLD, 32));
				}
			}
		}
		g.drawString("Select your mark", (getWidth()/2) - (g.getFontMetrics().stringWidth("Select your mark")/2), 230);
	}

}
