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

import gpv.PieceDescriptor;
import static gpv.chess.PlayerColor.*;
import static gpv.chess.PieceName.*;

/**
 * An enumeration that describes all of the chess piece types with methods
 * to get the color and name.
 * 
 * @version Feb 21, 2020
 */
public enum ChessPieceDescriptor implements PieceDescriptor
{
	WHITEPAWN(WHITE, PAWN), 
	WHITEROOK(WHITE, ROOK),
	WHITEKNIGHT(WHITE, KNIGHT), 
	WHITEBISHOP(WHITE, BISHOP), 
	WHITEQUEEN(WHITE, QUEEN), 
	WHITEKING(WHITE, KING),
	BLACKPAWN(BLACK, PAWN), 
	BLACKROOK(BLACK, ROOK),
	BLACKKNIGHT(BLACK, KNIGHT), 
	BLACKBISHOP(BLACK, BISHOP), 
	BLACKQUEEN(BLACK, QUEEN), 
	BLACKKING(BLACK, KING);
	
	private PlayerColor color;
	private PieceName name;
	
	/**
	 * Private constructor to set the color and name in the instance.
	 * @param color
	 * @param name
	 */
	private ChessPieceDescriptor(PlayerColor color, PieceName name)
	{
		this.color = color;
		this.name = name;
	}

	/**
	 * @return the color
	 */
	public PlayerColor getColor()
	{
		return color;
	}

	/**
	 * @return the name
	 */
	public PieceName getName()
	{
		return name;
	}
}
