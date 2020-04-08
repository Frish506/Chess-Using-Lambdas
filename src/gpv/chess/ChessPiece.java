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

package gpv.chess;

import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static gpv.util.Coordinate.makeCoordinate;


import gpv.Piece;
import gpv.util.*;

/**
 * The chess piece is a piece with some special properties that are used for
 * determining whether a piece can move. It implements the Piece interface
 * and adds properties and methods that are necessary for the chess-specific
 * behavior.
 * @version Feb 21, 2020
 */
public class ChessPiece implements Piece<ChessPieceDescriptor>
{
	private final ChessPieceDescriptor descriptor;
	private boolean hasMoved;	// true if this piece has moved
	
	/**
	 * The only constructor for a ChessPiece instance. Requires a descriptor.
	 * @param descriptor
	 */
	public ChessPiece(ChessPieceDescriptor descriptor)
	{
		this.descriptor = descriptor;
		hasMoved = false;
	}

	/*
	 * @see gpv.Piece#getDescriptor()
	 */
	@Override
	public ChessPieceDescriptor getDescriptor()
	{
		return descriptor;
	}
	
	/**
	 * @return the color
	 */
	public PlayerColor getColor()
	{
		return descriptor.getColor();
	}

	/**
	 * @return the name
	 */
	public PieceName getName()
	{
		return descriptor.getName();
	}
	
	
	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{
		ChessPiece movingPiece = (ChessPiece) b.getPieceAt(from);
		
		//First check if coordinate "to" is outside the board's range
		if(!(0 < to.x && to.x <= b.nColumns) || !(0 < to.y && to.y <= b.nRows)) { return false; }
		
		//Next check if coordinate "to" already has a piece of the same color on it
		ChessPiece dest = (ChessPiece) b.getPieceAt(to);
		if(dest != null && dest.getColor() == movingPiece.getColor()) { return false; }
		
		//Now check if the piece can actually make the move
		return ChessRules.checkValidRule(movingPiece, from, to, b);
		
	}
	

	/**
	 * @return the hasMoved
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}

	/**
	 * Once it moves, you can't change it.
	 * @param hasMoved the hasMoved to set
	 */
	public void setHasMoved()
	{
		hasMoved = true;
	}
}
