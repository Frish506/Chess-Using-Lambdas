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

/**
 * <p>
 * The PieceFactory produces pieces of various types. To get a piece the client
 * must get the instance and invoke the makePiece method.
 * <br/>
 * For example the client code might look like this:
 * <code>
 * 	// Assume that theFactory is injected during instantiation of the client
 * 	private PieceFactory<ChessPiece, ChessPieceDescriptor> theFactory;
 * 
 * 		...
 * 
 * 		ChessPiece pawn = factory.makePiece(WHITEPAWN);
 * </code>
 * 
 * @version Feb 21, 2020
 * @param <P> the type of piece
 * @param <D> a descriptor of the piece subtype
 */
public interface PieceFactory<P extends Piece, D extends PieceDescriptor>
{
	P makePiece(D descriptor);
}
