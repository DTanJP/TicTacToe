package com.hopto.dtanjp.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.hopto.dtanjp.Input;
import com.hopto.dtanjp.gui.GameButton;
import com.hopto.dtanjp.gui.TTTGrid;
import com.hopto.dtanjp.tictactoe.GameController;
/**
 * InstructionScene.java
 * 
 * @author David Tan
 **/
public class InstructionScene extends Scene {

	/** Serial version UID **/
	private static final long serialVersionUID = 2670255855229655247L;

	/** Main menu button **/
	private GameButton main_menu_btn;
	
	/** TTTGrid **/
	private TTTGrid grid;
	
	/** The timer to display each win flag **/
	private Timer timer;
	
	/** The current win flag index to display **/
	private int win_index = 0;
	
	private String[] markTypes = {"X", "O"};
	
	/** Constructor **/
	public InstructionScene() {
		setLayout(null);
		setBackground(Color.BLACK);
		
		main_menu_btn = new GameButton("<< Main menu");
		grid = new TTTGrid(100);
		
		grid.setEnabled(false);
		
		add(main_menu_btn);
		add(grid);
		
		main_menu_btn.setBounds(10, 10, 100, 30);
		timer = new Timer(2500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				win_index++;
				if(win_index >= GameController.win_flags.length || win_index < 0)
					win_index = 0;
				grid.resetGrid();
				String markType = markTypes[(int)(Math.random()*markTypes.length)];
				for(int i : GameController.win_flags[win_index])
					grid.getCellByID(i).setValue(markType);
			}
			
		});
		timer.setRepeats(true);
	}
	
	@Override
	public void OnEnter() {
		grid.setLocation((getWidth()/2) - (grid.getWidth()/2), 150);
		String markType = markTypes[(int)(Math.random()*markTypes.length)];
		for(int i : GameController.win_flags[win_index])
			grid.getCellByID(i).setValue(markType);
		timer.start();
	}
	
	@Override
	public void OnExit() {
		timer.stop();
	}
	
	@Override
	public void Update(Input input, double delta) {
		if(main_menu_btn.isClicked())
			GameController.MAIN_MENU.EnterScene();
	}

	@Override
	public void Render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 32));
		centerString(g, "Instructions", 40);
		g.drawLine(0, 50, getWidth(), 50);
		g.setFont(new Font("Helvetica", Font.PLAIN, 16));
		centerString(g, "The purpose of tic-tac-toe is for whoever to place 3 marks in a straight line first, wins the game.", 70);
		centerString(g, "If all squares are filled up, then the game is tied.", 85);
		centerString(g, "You can pick either X or O.", 100);
		g.setColor(Color.GREEN);
		centerString(g, "Ways to win", grid.getY()+grid.getHeight()+20);
	}

	private void centerString(Graphics2D g, String line, int y) {
		g.drawString(line, (getWidth()/2) - (g.getFontMetrics().stringWidth(line)/2), y);
	}
}
