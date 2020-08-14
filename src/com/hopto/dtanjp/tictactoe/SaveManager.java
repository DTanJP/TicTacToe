package com.hopto.dtanjp.tictactoe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

import com.hopto.dtanjp.gui.TTTGrid;
/**
 * SaveManager.java
 * 
 * @author David Tan
 **/
public class SaveManager implements Serializable {

	/** Serial version UID **/
	private static final long serialVersionUID = -1707311421574500179L;

	/** Singleton instance **/
	private static SaveManager instance = null;
	
	/** Constructor **/
	private SaveManager() {}
	
	/** Singleton **/
	public static SaveManager getInstance() {
		if(instance == null)
			instance = new SaveManager();
		return instance;
	}
	
	public boolean saveFileExists() {
		return new File("saveFile.dat").exists();
	}
	
	public void save() {
		PLAYER_SCORE = GameController.PLAYER_SCORE;
		AI_SCORE = GameController.AI_SCORE;
		PLAYER_TURN = GameController.PLAYER_TURN;
		PLAYER_MOVES_COUNT = GameController.PLAYER_MOVES_COUNT;
		OPPONENT_MOVES_COUNT = GameController.OPPONENT_MOVES_COUNT;
		DIFFICULTY = GameController.opponent.difficulty;
		PLAYER_VALUE = GameController.PLAYER_VALUE;
		board = GameController.getBoardValues();
		try {    
            FileOutputStream file = new FileOutputStream("saveFile.dat"); 
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(instance);
            out.close(); 
            file.close(); 
        } catch(IOException e) {
        	e.printStackTrace();
        } 
	}
	
	public void load() {
		try {
			loadingSaveFile = true;
            FileInputStream file = new FileInputStream("saveFile.dat"); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            instance = (SaveManager)in.readObject(); 
            
            in.close(); 
            file.close();
            GameController.PLAYER_VALUE = instance.PLAYER_VALUE;
            GameController.PLAYER_SCORE = instance.PLAYER_SCORE;
            GameController.AI_SCORE = instance.AI_SCORE;
            GameController.PLAYER_TURN = instance.PLAYER_TURN;
            GameController.PLAYER_MOVES_COUNT = instance.PLAYER_MOVES_COUNT;
            GameController.OPPONENT_MOVES_COUNT = instance.OPPONENT_MOVES_COUNT;
            GameController.opponent = new OpponentAI();
            GameController.opponent.difficulty = instance.DIFFICULTY;
            instance.grid = new TTTGrid(100);
            for(int i=0; i<instance.grid.cells.length; i++)
            	instance.grid.getCellByID(i).setValue(instance.board[i]);
        } catch(IOException | ClassNotFoundException e) {
        	e.printStackTrace();
        }
	}
	
	public boolean isLoadingSaveFile() {
		boolean result = loadingSaveFile;
		loadingSaveFile = false;
		return result;
	}
	
	public TTTGrid getGrid() {
		return Optional.ofNullable(instance.grid).orElse(new TTTGrid(100));
	}
	
	/** Save variables **/
	private String PLAYER_VALUE = "";
	private int PLAYER_SCORE = 0;
	private int AI_SCORE = 0;
	private boolean PLAYER_TURN = false;
	private int PLAYER_MOVES_COUNT = 0;
	private int OPPONENT_MOVES_COUNT = 0;
	private int DIFFICULTY = 0;
	private String[] board = new String[9];
	private boolean loadingSaveFile = false;
	private TTTGrid grid = null;
}
