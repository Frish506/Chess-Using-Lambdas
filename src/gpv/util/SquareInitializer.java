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

import gpv.Piece;

/**
 * This is a class that is used to initialize one square on a 2D board. 
 * The appropriate Board instance can then initialize the itself when given
 * a collection of these. It is up to the board or some other class to verify
 * the validity of the contents of a SquareInitializer instance. This is strictly
 * a data object.
 * @version Feb 23, 2020
 */
public class SquareInitializer
{
	private final Piece thePiece;
	private final Coordinate theSquare;
	
	/**
	 * Only constructor
	 * @param p The piece
	 * @param c The coordinate
	 */
	private SquareInitializer(Piece p, Coordinate c)
	{
		this.thePiece = p;
		this.theSquare = c;
	}
	
	/**
	 * Factory method for the initializer. This allows for having an object pool
	 * that is shared if necessary.
	 * @param p The piece
	 * @param c The coordinate
	 * @return the SquareInitializer instance
	 */
	public static SquareInitializer makeSquareInitializer(Piece p, Coordinate c)
	{
		return new SquareInitializer(p, c);
	}

	/**
	 * @return the thePiece
	 */
	public Piece getPiece()
	{
		return thePiece;
	}

	/**
	 * @return the theSquare
	 */
	public Coordinate getSquare()
	{
		return theSquare;
	}
}
