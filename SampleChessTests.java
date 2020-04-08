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

import static gpv.chess.ChessPieceDescriptor.*;
import static gpv.util.Coordinate.makeCoordinate;
import static gpv.util.SquareInitializer.makeSquareInitializer;
import static org.junit.Assert.*;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import gpv.util.*;

/**
 * Tests to ensure that pieces are created correctly and that all pieces
 * get created.
 * @version Feb 23, 2020
 */
class SampleChessTests
{
	private static ChessPieceFactory factory = null;
	private Board board;
	
	@BeforeAll
	public static void setupBeforeTests()
	{
		factory = new ChessPieceFactory();
	}
	
	@BeforeEach
	public void setupTest()
	{
		board = new Board(8, 8);
	}
	
	@Test
	void makePiece()
	{
		ChessPiece pawn = factory.makePiece(WHITEPAWN);
		assertNotNull(pawn);
	}
	
	/**
	 * This type of test loops through each value in the Enum and
	 * one by one feeds it as an argument to the test method.
	 * It's worth looking at the different types of parameterized
	 * tests in JUnit: 
	 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
	 * @param d the Enum value
	 */
	@ParameterizedTest
	@EnumSource(ChessPieceDescriptor.class)
	void makeOneOfEach(ChessPieceDescriptor d)
	{
		ChessPiece p = factory.makePiece(d);
		assertNotNull(p);
		assertEquals(d.getColor(), p.getColor());
		assertEquals(d.getName(), p.getName());
	}

	@Test
	void placeOnePiece()
	{
		ChessPiece p = factory.makePiece(BLACKPAWN);
		board.putPieceAt(p, makeCoordinate(2, 2));
		assertEquals(p, board.getPieceAt(makeCoordinate(2, 2)));
	}

	@Test
	void placeTwoPieces()
	{
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		assertEquals(bn, board.getPieceAt(makeCoordinate(3, 5)));
		assertEquals(wb, board.getPieceAt(makeCoordinate(2, 6)));
	}
	
	@Test
	void checkForPieceHasMoved()
	{
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		assertFalse(bq.hasMoved());
		bq.setHasMoved();
		assertTrue(bq.hasMoved());
	}
	
	/**
	 * This type of test gets its arguments via dependency injection from moveTestProvider();
	 * @param initializers set up the board
	 * @param from move from this coordinate
	 * @param to move to this coordinate
	 * @param expected the expected result calling canMove() on the piece at "from"
	 */
    @ParameterizedTest
    @MethodSource("moveTestProvider")
    void moveTest(List<SquareInitializer> initializers, Coordinate from,
            Coordinate to, boolean expected)
    {
        board.reset(initializers);
        ChessPiece p = (ChessPiece)board.getPieceAt(from);
        assertNotNull(p);
        assertEquals(expected, p.canMove(from, to, board));
    }
    
    /**
     * When castling, the initializers are the king first, rook second,
     * and others follow, if they are in play.
     */
    @ParameterizedTest
    @MethodSource("castlingTestProvider")
    void castlingTest(List<SquareInitializer> initializers, boolean kingMoved,
        boolean rookMoved, Coordinate to, boolean expected)
    {
        board.reset(initializers);
        Coordinate kingCoord = initializers.get(0).getSquare();
        Coordinate rookCoord = initializers.get(1).getSquare();
        ChessPiece king = (ChessPiece)board.getPieceAt(kingCoord);
        if (kingMoved) {
            ((ChessPiece)board.getPieceAt(kingCoord)).setHasMoved();
        }
        if (rookMoved) {
            ((ChessPiece)board.getPieceAt(rookCoord)).setHasMoved();
        }
        assertEquals(expected, king.canMove(kingCoord, to, board));
    }

	// Helper methods
	   static Stream<Arguments> moveTestProvider()
	   {
	       return Stream.of(
	           Arguments.of(
	               makeInitializers(WHITEKING, 1, 5, WHITEPAWN, 2, 5),
	               makeCoordinate(1, 5), makeCoordinate(2, 5), false),
	           Arguments.of(
	               makeInitializers(WHITEKING, 1, 5),
	               makeCoordinate(1, 5), makeCoordinate(2, 5), true)
            );
        }
	    
	    static Stream<Arguments> castlingTestProvider()
	    {
	        return Stream.of(
	            Arguments.of(
	                makeInitializers(BLACKKING, 8, 5, BLACKROOK, 8, 8),
	                false, false, makeCoordinate(8, 7), true)
	        );
	    }
	    
	   private static List<SquareInitializer> makeInitializers(Object... params)
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
