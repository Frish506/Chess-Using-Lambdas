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

import java.awt.Point;

/**
 * This class represents a two-dimensional coordinate that would be
 * used in a rectangular board. No assumptions are made about the actual
 * mapping of coordinate to the squares on the board.
 * <br/>
 * There are equals() and hashCode() methods so the Coordinate can be
 * used as keys in collections that use hashing (e.g. HashMap) and a
 * toString() to print the coordinate in some readable form. This is
 * useful for debugging.
 * 
 * @version Feb 21, 2020
 */
public class Coordinate extends Point
{
	
	/**
	 * The only constructor. It is private to avoid any client from
	 * creating one for a purpose not intended.
	 * 
	 * @param row
	 * @param column
	 */
	private Coordinate(int row, int column)
	{
		this.x = row;
		this.y = column;
	}
	
	/**
	 * Factory method. This only creates a Coordinate if it hasn't been created
	 * already.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public static Coordinate makeCoordinate(int row, int column)
	{
		return new Coordinate(row, column);
	}
	
	/**
	 * @return the row
	 */
	public int getRow()
	{
		return this.x;
	}
	
	/**
	 * @return the column
	 */
	public int getColumn()
	{
		return this.y;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Coordinate)) {
			return false;
		}
		return true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
