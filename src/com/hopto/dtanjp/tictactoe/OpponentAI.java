package com.hopto.dtanjp.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hopto.dtanjp.gui.TTTCell;

/**
 * OpponentAI.java
 * 
 * @author David Tan
 **/
public class OpponentAI {

	/** 
	 * Difficulty:
	 * 0 - Chooses random spots
	 * 1 - Easy - Picks the most optimal win flag -> chooses random spots on the win flag. If theres no available win flags then just do it randomly
	 * 2 - Medium - Picks the most optimal win flag. Will try to block the player if they are 1 move away from winning.
	 * 3 - Hard - Uses the minimax algorithm to find the most optimal way
	 **/
	public int difficulty = -1;
	
	/** OPPONENT_VALUE: Is the AI playing as an X or O **/
	private String OPPONENT_VALUE = "";
	
	/** Win flag: Choose which win flag to try to go for **/
	private int[] win_flag = null;
	
	/** Determine the player's win flag **/
	private int[] player_win_flag = null;
	
	/** Did the AI completed their turn? **/
	public boolean completedTurn = false;
	
	/** Constructor **/
	public OpponentAI() {}
	
	public void process() {
		if(GameController.determineWin() != null) return;
		if(OPPONENT_VALUE == "" || OPPONENT_VALUE.equalsIgnoreCase(GameController.PLAYER_VALUE)) OPPONENT_VALUE = GameController.getOpponentValue();
		
		//Make the opponent "think"
		try {
			Thread.sleep((Math.min(3, (3-Math.max(0, difficulty)))*300));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(difficulty <= 0)//Randomly picking
			difficulty_process_0();
		else if(difficulty == 1)//Easy mode: just pick a win flag and go for it
			difficulty_process_1();
		else if(difficulty == 2)//Medium mode: Stalemating if possible else win
			difficulty_process_2();
		else if(difficulty >= 3)//Hard mode: Minimax algorithm
			difficulty_process_3();
		GameController.OPPONENT_MOVES_COUNT++;
		GameController.processRound();
	}
	
	public void resetStrategy() {
		//Difficulty 0 has no strategy.
		
		//Difficulty 1. Find a new win flag.
		win_flag = null;
		//Difficulty 2. 
		player_win_flag = null;
	}
	
	/** Randomly choose a random cell **/
	private void difficulty_process_0() {
		List<TTTCell> cellList = Arrays.asList(GameController.GRID.cells);
		Collections.shuffle(cellList);
		for(TTTCell cell : cellList) {
			if(cell.getValue() == "" && GameController.determineWin() == null && !GameController.PLAYER_TURN) {
				cell.setValue(OPPONENT_VALUE);
				completedTurn = true;
				break;
			}
		}
	}
	
	/** Easy mode: Just go through the win flags and pick an available win flag **/
	private void difficulty_process_1() {
		updateWinFlag();//Find the most optimal win flag
		
		//if the win flag is not valid, don't bother trying to complete it
		if(!validWinFlag(win_flag)) win_flag = null;
		
		//Rule 1: Win if possible
		if(win_flag != null) {
			for(int move : win_flag) {
				if(GameController.GRID.getCellByID(move).getValue().equals("")) {
					GameController.GRID.getCellByID(move).setValue(OPPONENT_VALUE);
					completedTurn = true;
					break;
				}
			}
			if(completedTurn) return;
		}
		//Unable to find a move to complete in win flag
		if(!completedTurn)
			difficulty_process_0();
	}
	
	/** Medium mode: Stalemating and finding the most optimal win flag **/
	/**
	 * Heuristics:
	 * 1. Try to win if the AI can
	 * 2. Stalemate if possible
	 * 3. Continue the win flag/Find the most optimal win flag
	 **/
	private void difficulty_process_2() {
		player_win_flag = findPlayerWinFlag();//Find the player win flag
		int movesRemaining = movesLeftForWinFlag(win_flag);//How many moves left to win
		
		updateWinFlag();//Find the most optimal win flag
		//Rule 1: Win if possible
		if(win_flag != null) {
			if(movesRemaining == 1 && GameController.OPPONENT_MOVES_COUNT >= 2) {
				for(int move : win_flag) {
					if(GameController.GRID.getCellByID(move).getValue().equals("")) {
						GameController.GRID.getCellByID(move).setValue(OPPONENT_VALUE);
						completedTurn = true;
						break;
					}
				}
				if(completedTurn) return;
			}
		}
		
		//Rule 2: Stalemate if possible
		if(player_win_flag != null) {
			if(!breakPlayerWinFlag())
				difficulty_process_1();
		} else {
			if(win_flag == null)
				difficulty_process_1();
			else {
				for(int move : win_flag) {
					if(GameController.GRID.getCellByID(move).getValue().equals("")) {
						GameController.GRID.getCellByID(move).setValue(OPPONENT_VALUE);
						completedTurn = true;
						break;
					}
				}
			}
		}
	}
	
	/** Hard: Use the minimax algorithm **/
	private void difficulty_process_3() {
		int bestScore = Integer.MIN_VALUE;
		int bestMove = -1;
		String[] board = GameController.getBoardValues();
		List<Integer> moves = getAvailableMoves(GameController.getBoardValues());
		
		//Loop through all the available moves
		for(int i : moves) {
			int score = Integer.MIN_VALUE;
			if(board[i].equals("")) {
				board[i] = OPPONENT_VALUE;
				score = minimax(board, false);
				board[i] = "";
				
				//Found the best score
				if(score > bestScore) {
					bestScore = score;
					bestMove = i;
				}
			}
		}
		//Found a move
		if(bestMove != -1 && !GameController.PLAYER_TURN && !completedTurn) {
			GameController.GRID.getCellByID(bestMove).setValue(OPPONENT_VALUE);
			completedTurn = true;
		} else
			difficulty_process_2();//Fall back to medium mode
	}
	
	/** Determine the best move by calculating all the possibilities **/
	private int minimax(String[] board, boolean aiTurn) {
		//Find and determine a winner
		String winner = getWinner(board);
		if(winner != null)
			return winner.equalsIgnoreCase(OPPONENT_VALUE) ? 1 : -1;
		
		//Get the list of available moves
		List<Integer> moves = getAvailableMoves(board);
		
		//No more moves are available. Return draw.
		if(moves.isEmpty()) return 0;
		
		int bestScore = (aiTurn) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for(int i : moves) {//GO through each available move and find an empty spot and evaluate it
			if(board[i].equals("")) {//Empty spot found.
				board[i] = (aiTurn) ? OPPONENT_VALUE : GameController.PLAYER_VALUE;//Set the value of this choice to the board according if it's the AI/Player's turn
				int score = minimax(board, !aiTurn);//Evaluate the rest of the boards with this choice,
				board[i] = "";//Reset this choice.
				bestScore = (aiTurn) ? Math.max(score, bestScore) : Math.min(score, bestScore);//Evaluate the score
			}
		}
		return bestScore;
	}
	
	/** getWinner: Used for the minimax algorithm to determine if the board at that stage has a winner **/
	private String getWinner(String[] board) {
		for(int[] flag : GameController.win_flags) {
			if((board[flag[0]].equalsIgnoreCase(GameController.PLAYER_VALUE) 
					&& board[flag[1]].equalsIgnoreCase(GameController.PLAYER_VALUE) 
					&& board[flag[2]].equalsIgnoreCase(GameController.PLAYER_VALUE)))
				return GameController.PLAYER_VALUE;
			else if((board[flag[0]].equalsIgnoreCase(OPPONENT_VALUE) 
					&& board[flag[1]].equalsIgnoreCase(OPPONENT_VALUE) 
					&& board[flag[2]].equalsIgnoreCase(OPPONENT_VALUE)))
				return OPPONENT_VALUE;
		}
		return null;//Draw or still on-going
	}
	
	/** getAvailableMoves: Find any free spots on the board. Used for the minimax algorithm **/
	private List<Integer> getAvailableMoves(String[] board) {
		List<Integer> result = new ArrayList<>(9);
		for(int i=0; i<board.length; i++) {
			if(board[i].equals(""))
				result.add(i);
		}
		return result;
	}
	
	/** Try and break the player's win flag **/
	private boolean breakPlayerWinFlag() {
		if(player_win_flag == null) return false;
		boolean result = false;
		for(int i : player_win_flag) {
			String val = GameController.GRID.getCellByID(i).getValue();
			if(val.equals("")) {
				GameController.GRID.getCellByID(i).setValue(OPPONENT_VALUE);
				completedTurn = true;
				result = true;
				break;
			}
		}
		return result;
	}
	
	/** How many moves left for win flag **/
	private int movesLeftForWinFlag(int[] flag) {
		if(flag == null || !validWinFlag(flag)) return -1;
		int result = 0;
		for(int move : flag) {
			if(GameController.GRID.getCellByID(move).getValue().equals(""))
				result++;
		}
		return result;
	}
	
	/** Find the best win flag that the AI can use **/
	private int[] findOptimalWinFlag() {
		int[] result = null;
		int bestCount = 3;
		for(int[] flag : GameController.win_flags) {
			int count = 3;//How many moves in this win flag belongs to the AI
			for(int i=0; i<flag.length; i++) {
				if(!validWinFlag(flag)) continue;
				String val = GameController.GRID.getCellByID(flag[i]).getValue();
				if(val.equalsIgnoreCase(OPPONENT_VALUE)) count--;
			}
			if(count < bestCount) {
				result = flag;
				bestCount = count;
			}
			//Theres only 1 move left. Don't continue looking
			if(bestCount == 1)
				break;
		}
		return result;
	}
	
	/** Decide if the current win flag is still good else switch to the new one **/
	private void updateWinFlag() {
		int movesRemaining = movesLeftForWinFlag(win_flag);//How many moves left to win
		
		if(!validWinFlag(win_flag) || win_flag == null || movesRemaining == -1)
			win_flag = findOptimalWinFlag();
		else {//Win flag exists, but check to see if theres any better win flags
			int[] temp_win_flag = findOptimalWinFlag();
			if(temp_win_flag != win_flag) {
				if(movesLeftForWinFlag(temp_win_flag) < movesRemaining)
					win_flag = temp_win_flag;
			}
		}
	}
	
	/** Determine if the AI can use this flag to win **/
	private boolean validWinFlag(int[] cellIDs) {
		if(cellIDs == null) return false;
		if(cellIDs.length != 3) return false;
		int valid = 0;
		for(int i=0; i<cellIDs.length; i++) {
			String value = GameController.GRID.getCellByID(cellIDs[i]).getValue();
			//This win flag contains a player value and is blocked off by the player. -> This win flag is automatically invalid
			if(value.equalsIgnoreCase(GameController.PLAYER_VALUE)) {
				valid = 0;
				break;
			}
			if(value.equals("") || value.equalsIgnoreCase(OPPONENT_VALUE))
				valid++;
		}
		return valid==3;
	}
	
	/** findPlayerWinFlag(): The AI tries to determine which win flag the player is using and tries to block them. **/
	private int[] findPlayerWinFlag() {
		int[] result = null;
		int bestWinFlag = 0;
		
		//Loop through each win flag
		for(int[] flag : GameController.win_flags) {
			int point = 0;
			for(int i : flag) {
				String value = GameController.GRID.getCellByID(i).getValue();
				if(value.equalsIgnoreCase(OPPONENT_VALUE)) break;//AI already blocked this win flag
				if(value.equalsIgnoreCase(GameController.PLAYER_VALUE)) point++;
			}
			if(point > bestWinFlag) {
				result = flag;
				bestWinFlag = point;
			}
			if(bestWinFlag == 2)
				break;
		}
		//The win flag must be occupied with at least 2 spots to be considered a win flag
		return (bestWinFlag >= 2) ? result : null;
	}
}