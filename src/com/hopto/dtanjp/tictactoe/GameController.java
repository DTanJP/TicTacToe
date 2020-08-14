package com.hopto.dtanjp.tictactoe;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import com.hopto.dtanjp.Display;
import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.Window;
import com.hopto.dtanjp.audio.Audio;
import com.hopto.dtanjp.gui.TTTCell;
import com.hopto.dtanjp.gui.TTTGrid;
import com.hopto.dtanjp.scene.*;

/**
 * GameController.java
 * 
 * @author David Tan
 **/
public class GameController {

	/** Singleton instance **/
	private static GameController instance = null;
	
	/** Window instance **/
	public final Window window = Window.getInstance();
	
	/** Display instance **/
	public final Display display = Display.getInstance();
	
	/** Input instance **/
	public final Input input = Input.getInstance();
	
	/** SaveManager instance: Handles saving/loading **/
	public final SaveManager saveController = SaveManager.getInstance();
	
	/** The game application status **/
	public static boolean running = false;
	
	public static String PLAYER_VALUE = "";//X or O
	
	/** AssetManager instance **/
	public static AssetManager assets = AssetManager.getInstance();
	
	/** Scenes **/
	public static final Scene MAIN_MENU = new MainMenu();
	public static final Scene GAME_SCENE = new GameScene();
	public static final Scene INSTRUCTION_SCENE = new InstructionScene();
	public static final Scene CREDIT_SCENE = new CreditScene();
	public static final Scene SELECT_TYPE_SCENE = new SelectTypeScene();
	
	/** Tic Tac Toe Grid **/
	public static TTTGrid GRID = null;
	
	/** Scores **/
	public static int PLAYER_SCORE = 0;
	public static int AI_SCORE = 0;
	
	public static boolean PLAYER_TURN = false;
	
	/** Opponent AI instance **/
	public static OpponentAI opponent;
	
	/** Move counter: Determine wins only if there at least 3 moves done **/
	public static int PLAYER_MOVES_COUNT = 0;
	public static int OPPONENT_MOVES_COUNT = 0;
	
	public static Audio background_music = null;
	
	/** Constructor **/
	private GameController() {
		window.setContentPane(display);
		window.setPreferredSize(display.getSize());
		display.addMouseWheelListener(input);
		display.addKeyListener(input);
		display.addMouseListener(input);
		display.requestFocusInWindow();
		window.pack();
		assets.Load();
		if(assets.getMissingAssetsCount() > 0)
			System.out.println("Missing assets: "+assets.getMissingAssetsCount());
		window.setIconImage(assets.game_icon);
	}
	
	/** Singleton **/
	public static GameController getInstance() {
		if(instance == null)
			instance = new GameController();
		return instance;
	}
	
	/** Get the current scene **/
	public Scene getCurrentScene() { return display.currentScene; }
	
	public void EnterScene(Scene scene) {
		if(display.currentScene != null) {
			display.currentScene.OnExit();
			display.remove(display.currentScene);
		}
		
		display.currentScene = scene;
		if(display.currentScene != null) {
			display.add(display.currentScene);
			display.currentScene.setOpaque(true);
			display.currentScene.setBounds(0, 0, Math.max(display.getSize().width, 0), Math.max(display.getSize().height, 0));
			display.currentScene.setVisible(true);
			display.currentScene.OnEnter();
		}
	}
	
	public static void processRound() {
		if(GRID == null) return;
		String winner = determineWin();
		if(winner == null) {
			if(!PLAYER_TURN) {//Opponent's turn
				if(opponent.completedTurn) {
					opponent.completedTurn = false;
					PLAYER_TURN = true;
				} else//Something happened and the AI didn't make a move.
					opponent.process();
			} else//Player's turn
				PLAYER_TURN = false;
		} else {//Winner is determined
			opponent.resetStrategy();
			PLAYER_TURN = winner.equalsIgnoreCase(PLAYER_VALUE);
			if(winner.equalsIgnoreCase(PLAYER_VALUE))
				PLAYER_SCORE++;
			else {
				AI_SCORE++;
				for(TTTCell cell : GRID.cells)
					cell.setEnabled(false);
			}
			instance.saveController.save();
		}
	}
	
	public static int getAvailableMovesLeft() {
		if(GRID != null)
			return (int) Arrays.stream(GRID.cells).filter(c -> c.getValue().equals("")).count();
		return -1;
	}
	
	public static String[] getBoardValues() {
		if(GRID != null) {
			String[] result = new String[9];
			for(int i=0; i<GRID.cells.length; i++)
				result[i] = GRID.getCellByID(i).getValue().toUpperCase();
			return result;
		}
		return null;
	}
	
	public static String getOpponentValue() {
		if(PLAYER_VALUE.equalsIgnoreCase("")) return "";
		return (PLAYER_VALUE.equalsIgnoreCase("X")) ? "O" : "X";
	}
	
	public static int[][] win_flags = {
			{0, 3, 6}, {1, 4, 7}, {2, 5, 8},//Vertical lines
			{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //Horizontal lines
			{0, 4, 8}, {2, 4, 6}//Diagonals
	};
	
	public void changeResolution(Dimension size) {
		if(size == null) return;
		window.setSize(size);
		window.setPreferredSize(size);
		window.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width/2) - (size.width/2), (Toolkit.getDefaultToolkit().getScreenSize().height/2) - (size.height/2));
		
		display.setSize(size);
		display.setPreferredSize(size);
		display.setBounds(0, 0, (int)size.getWidth(), (int)size.getHeight());
		
		if(display.currentScene != null) {
			display.currentScene.setBounds(0, 0, Math.max(display.getSize().width, 0), Math.max(display.getSize().height, 0));
			display.currentScene.OnEnter();
		}
	}
	
	public static String determineWin() {
		if(PLAYER_TURN && PLAYER_MOVES_COUNT < 3) return null;
		if(!PLAYER_TURN && OPPONENT_MOVES_COUNT < 3) return null;
		String result = null;
		String check = ((PLAYER_TURN) ? PLAYER_VALUE : getOpponentValue()).toUpperCase();
		for(int[] flag : win_flags) {
			String val1 = GRID.getCellByID(flag[0]).getValue().toUpperCase();
			String val2 = GRID.getCellByID(flag[1]).getValue().toUpperCase();
			String val3 = GRID.getCellByID(flag[2]).getValue().toUpperCase();
			if(val1.equalsIgnoreCase(check) && val2.equalsIgnoreCase(check) && val3.equalsIgnoreCase(check)) {
				result = check;
				break;
			}
		}
		return result;
	}
	
	public static float lerp(float point1, float point2, float alpha) {
	    return point1 + alpha * (point2 - point1);
	}
}