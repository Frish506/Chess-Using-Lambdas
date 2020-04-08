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

import static gpv.chess.ChessPieceDescriptor.*;
import static gpv.util.Coordinate.makeCoordinate;
import static gpv.util.SquareInitializer.makeSquareInitializer;
import static org.junit.Assert.assertNotNull;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import gpv.chess.*;

/**
 * Description
 * @version Feb 23, 2020
 */
class BoardTests
{
	private static ChessPieceFactory factory = null;
	private Board theBoard;
	
	@BeforeAll
	public static void setupBeforeTests()
	{
		factory = new ChessPieceFactory();
	}
	
	@BeforeEach
	public void setup()
	{
		theBoard = new Board(8, 8);
	}
	
	@Test
	void boardWithOnePawn()
	{
		List<SquareInitializer> initializers = makeInitializers(WHITEPAWN, 1, 0);
		theBoard.reset(initializers);
		assertNotNull(theBoard.getPieceAt(makeCoordinate(1, 0)));
	}
	
	@Test
	void boardWithTwoPieces()
	{
		List<SquareInitializer> initializers = makeInitializers(
				WHITEPAWN, 1, 0,
				BLACKPAWN, 6, 0);
		theBoard.reset(initializers);
		assertNotNull(theBoard.getPieceAt(makeCoordinate(1, 0)));
		assertNotNull(theBoard.getPieceAt(makeCoordinate(6, 0)));
	}

	// Helper methods
	private List<SquareInitializer> makeInitializers(Object... params)
	{
		List<SquareInitializer> initializers = new ArrayList<SquareInitializer>();
		int ix = 0;
		while (ix < params.length) {
			ChessPiece p = factory.makePiece((ChessPieceDescriptor) params[ix++]);
			Coordinate c = makeCoordinate((int)params[ix++], (int)params[ix++]);
			initializers.add((SquareInitializer)makeSquareInitializer(p, c));
		}
		
		return initializers;
	}
}
