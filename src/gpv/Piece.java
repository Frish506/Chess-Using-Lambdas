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

package gpv;

import gpv.util.*;

/**
 * This is an interface that describes a generic interface for pieces in a
 * board game such as chess. The generic parameter is a descriptor that is
 * used to describe the specific piece. This is used by the concrete implementations
 * to determine properties of the piece, such as movement rules, etc.
 * @version Feb 21, 2020
 */
public interface Piece<D extends PieceDescriptor>
{
	/**
	 * @return the piece descriptor
	 */
	D getDescriptor();
	
	boolean canMove(Coordinate from, Coordinate to, Board b);
}
