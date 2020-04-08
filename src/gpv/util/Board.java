/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.util;

import java.util.*;
import java.util.stream.Stream;
import gpv.Piece;

/**
 * Class for a rectangular board made up of squares
 * @version Feb 23, 2020
 */
public class Board
{
	Map<Coordinate, Piece> board;
	public int nRows;
	public int nColumns;
	
	/**
	 * Constructor for an uninitialized board with given dimensions.
	 * @param maxRows
	 * @param maxColumns
	 */
	public Board(int maxRows, int maxColumns)
	{
		nRows = maxRows;
		nColumns = maxColumns;
		board = new HashMap<Coordinate, Piece>();
	}
	
	/**
	 * Constructor that also initializes the board.
	 * @param maxRows
	 * @param maxColumns
	 * @param initializers
	 */
	public Board(int maxRows, int maxColumns, 
			List<SquareInitializer> initializers)
	{
		this(maxRows, maxColumns);
		reset(initializers);
	}
	
	/**
	 * @return the piece at the given coordinate or null if none.
	 */
	public Piece getPieceAt(Coordinate c)
	{
		return board.get(c);
	}
	
	/**
	 * Clear the board and re-initialize it with the specified configuration
	 * @param initializers a configuration consisting of a list of 
	 * 	SquareInitializers for those squares containing pieces.
	 */
	public void reset(List<SquareInitializer> initializers)
	{
		board.clear();
		for (SquareInitializer si : initializers) {
			board.put(si.getSquare(), si.getPiece());
		}
	}
	
	/**
	 * Place a piece p at the given location
	 * @param p the piece to place
	 * @param c the coordinate of the square
	 * @return the piece that was placed
	 */
	public Piece putPieceAt(Piece p, Coordinate c)
	{
		return board.put(c, p);
	}

	/**
	 * @return the nColumns
	 */
	public int getnColumns()
	{
		return nColumns;
	}

	/**
	 * @param nRows the nRows to set
	 */
	public void setnRows(int nRows)
	{
		this.nRows = nRows;
	}
}
