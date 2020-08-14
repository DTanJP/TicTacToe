package com.hopto.dtanjp.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
/**
 * TTTGrid.java
 * 
 * @author David Tan
 **/
public class TTTGrid extends JPanel {

	/** Serial version UID **/
	private static final long serialVersionUID = -41177570192981259L;

	/** TTT Cells **/
	public final TTTCell[] cells = new TTTCell[9];
	
	/** Cell padding **/
	private int padding = 5;
	
	/** Cell length: How long one side of a cell is **/
	private int cellLength = 100;
	
	/** Constructor **/
	public TTTGrid(int cellLength) {
		this.cellLength = cellLength;
		setBackground(Color.BLACK);
		setSize(new Dimension((cellLength*3 + (padding * 4)), (cellLength*3) + (padding*4)));
		setLayout(null);
		for(int i = 0; i < cells.length; i++) {
			cells[i] = new TTTCell();
			cells[i].setSize(cellLength, cellLength);
			cells[i].cellID = i;
			add(cells[i]);
		}
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				cells[(y*3)+x].setLocation(((x+1)*padding)+(x*cellLength), ((y+1)*padding)+(y*cellLength));
			}
		}
	}
	
	public void resetGrid() {
		for(TTTCell cell : cells) {
			cell.value = "";
			cell.setIcon(null);
			cell.setEnabled(true);
			cell.hoverValue = "";
		}
	}
	
	public void setCellValue(TTTCell cell, String value) {
		String cvalue = (value.equalsIgnoreCase("X") || value.equalsIgnoreCase("O")) ? value : "";
		cell.value = cvalue;
	}
	
	public int getCellLength() { return this.cellLength; }
	
	public TTTCell getCellByID(int id) {
		TTTCell result = null;
		for(TTTCell cell : cells) {
			if(cell.cellID == id) {
				result = cell;
				break;
			}
		}
		return result;
	}
}
