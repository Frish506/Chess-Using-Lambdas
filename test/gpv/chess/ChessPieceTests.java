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
import static org.junit.Assert.*;

import java.util.stream.Stream;

import static gpv.util.Coordinate.makeCoordinate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import gpv.util.Board;

/**
 * Tests to ensure that pieces are created correctly and that all pieces
 * get created.
 * @version Feb 23, 2020
 */
class ChessPieceTests
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
	
	@Test
	void thisShouldFailOnDelivery()
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1,5));
		assertTrue(wk.canMove(makeCoordinate(1,5), makeCoordinate(2, 5), board));
	}
	
	//This test is to check whether choosing a destination outside the board will fail or not
	@Test
	void canPieceMoveOutsideTheBoard()
	{
		ChessPiece qu = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(qu, makeCoordinate(5,5));
		boolean can = qu.canMove(makeCoordinate(5,5), makeCoordinate(9,9), board);
		assertFalse(can);
		
		can = qu.canMove(makeCoordinate(5,5), makeCoordinate(4,10), board);
		assertFalse(can);
	}
	
	//This test will check whether a piece can move to a spot where another piece of the same color is
	@Test
	void canPieceMoveOnSameTeam()
	{
		ChessPiece qu = factory.makePiece(WHITEQUEEN);
		ChessPiece uwu = factory.makePiece(WHITEPAWN);
		board.putPieceAt(qu, makeCoordinate(5,5));
		board.putPieceAt(uwu, makeCoordinate(5,6));
		boolean can = qu.canMove(makeCoordinate(5,5), makeCoordinate(5,7), board);
		assertFalse(can);
		

		can = qu.canMove(makeCoordinate(5,5), makeCoordinate(5,4), board);
		assertTrue(can);

	}
	
	//Beginning of queen move tests
	
	@ParameterizedTest
	@MethodSource("queenBasicMoves")
	void queenBasicMoveTests(int fromX, int fromY, int toX, int toY) {
		ChessPiece qu = factory.makePiece(WHITEQUEEN);
		board.putPieceAt(qu, makeCoordinate(fromX, fromY));
		assertTrue(qu.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> queenBasicMoves() {
		return Stream.of(
				Arguments.of(5, 5, 8, 8), //Up right
				Arguments.of(5, 5, 1, 1), //Down left
				Arguments.of(5, 5, 8, 2), //Up left
				Arguments.of(5, 5, 2, 8), //Down right
				Arguments.of(5, 5, 5, 8), //Right
				Arguments.of(5, 5, 5, 1), //Left
				Arguments.of(5, 5, 1, 5), //Down
				Arguments.of(5, 5, 8, 5)  //Up
				);
	}
	
	@ParameterizedTest
	@MethodSource("queenFailedMoves")
	void queenFailedMoveTests(int fromX, int fromY, int toX, int toY) {
		ChessPiece qu = factory.makePiece(WHITEQUEEN);
		ChessPiece baQu = factory.makePiece(BLACKQUEEN);
		ChessPiece baPa = factory.makePiece(BLACKPAWN);
		ChessPiece pa = factory.makePiece(WHITEPAWN);
		board.putPieceAt(qu, makeCoordinate(fromX, fromY));
		board.putPieceAt(baQu, makeCoordinate(5, 7)); //Bad queen is two spaces up
		board.putPieceAt(baPa, makeCoordinate(3, 3)); //Bad pawn is two diagonals down to the left
		board.putPieceAt(pa, makeCoordinate(7, 5)); //Good pawn is two spaces to the right
		assertFalse(qu.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> queenFailedMoves() {
		return Stream.of(
				Arguments.of(5, 5, 9, 5), //Off the board to the right
				Arguments.of(5, 5, 5, 8), //Try to go through bad queen
				Arguments.of(5, 5, 2, 2), //Try to go through bad pawn
				Arguments.of(5, 5, 8, 5), //Try to go through good pawn
				Arguments.of(5, 5, 7, 5), //Try to land on good pawn
				Arguments.of(5, 5, 1, 3), //Teleport!
				Arguments.of(5, 5, 4, 7) //Teleport!
				);
	}
	
	//Beginning of Bishop move tests
	
	@ParameterizedTest
	@MethodSource("bishopBasicMoves")
	void bishopBasicMoveTests(int fromX, int fromY, int toX, int toY) {
		ChessPiece bi = factory.makePiece(WHITEBISHOP);
		ChessPiece baBi = factory.makePiece(BLACKBISHOP);
		board.putPieceAt(bi, makeCoordinate(fromX, fromY));
		board.putPieceAt(baBi, makeCoordinate(2, 8));
		assertTrue(bi.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> bishopBasicMoves() {
		return Stream.of(
				Arguments.of(5, 5, 8, 8), //Up right
				Arguments.of(5, 5, 1, 1), //Down left
				Arguments.of(5, 5, 8, 2), //Down Right
				Arguments.of(5, 5, 3, 7), //Up Left
				Arguments.of(5, 5, 2, 8) //Capture!

			);
	}
	
	@ParameterizedTest
	@MethodSource("bishopFailedMoves")
	void bishopFailedMoveTests(int fromX, int fromY, int toX, int toY) {
		ChessPiece bi = factory.makePiece(WHITEBISHOP);
		ChessPiece pa = factory.makePiece(WHITEPAWN);
		board.putPieceAt(bi, makeCoordinate(fromX, fromY));
		board.putPieceAt(pa, makeCoordinate(6, 6));
		assertFalse(bi.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> bishopFailedMoves() {
		return Stream.of(
				Arguments.of(5, 5, 9, 9), //Off the board
				Arguments.of(5, 5, 5, 1), //Moving vertically
				Arguments.of(5, 5, 7, 7), //Through a piece
				Arguments.of(5, 5, 6, 6) //On a friendly piece
			);
	}
	
	//Beginning of Rook move tests
	
	@ParameterizedTest
	@MethodSource("rookBasicMoves")
	void rookBasicMoveTests(int fromX, int fromY, int toX, int toY) {
		ChessPiece ro = factory.makePiece(WHITEROOK);
		ChessPiece baPa = factory.makePiece(BLACKPAWN);
		board.putPieceAt(ro, makeCoordinate(fromX, fromY));
		board.putPieceAt(baPa, makeCoordinate(8, 5));
		assertTrue(ro.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> rookBasicMoves() {
		return Stream.of(
				Arguments.of(5, 5, 5, 8), //Up
				Arguments.of(5, 5, 5, 1), //Down
				Arguments.of(5, 5, 1, 5), //Left
				Arguments.of(5, 5, 7, 5), //Right
				Arguments.of(5, 5, 8, 5)  //Capture!

			);
	}
	
	@ParameterizedTest
	@MethodSource("rookFailedMoves")
	void rookFailedMoveTests(int fromX, int fromY, int toX, int toY) {
		ChessPiece ro = factory.makePiece(WHITEROOK);
		ChessPiece pa = factory.makePiece(WHITEPAWN);
		board.putPieceAt(ro, makeCoordinate(fromX, fromY));
		board.putPieceAt(pa, makeCoordinate(7, 5));
		assertFalse(ro.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> rookFailedMoves() {
		return Stream.of(
				Arguments.of(5, 5, 5, 9), //Off the board
				Arguments.of(5, 5, 6, 6), //Diagonal
				Arguments.of(5, 5, 4, 7), //Teleport!
				Arguments.of(5, 5, 8, 5)  //Go through friendly

			);
	}

	//Beginning of King move tests
	
	@ParameterizedTest
	@MethodSource("kingBasicMoves")
	void kingBasicMovesTest(int fromX, int fromY, int toX, int toY) {
		ChessPiece ki = factory.makePiece(WHITEKING);
		board.putPieceAt(ki, makeCoordinate(fromX, fromY));
		assertTrue(ki.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> kingBasicMoves() {
		return Stream.of(
				Arguments.of(5, 5, 5, 6), //Up
				Arguments.of(5, 5, 5, 4), //Down
				Arguments.of(5, 5, 6, 5), //Right
				Arguments.of(5, 5, 4, 5),  //Left
				Arguments.of(5, 5, 6, 6), //Up Right
				Arguments.of(5, 5, 4, 4), //Down Left
				Arguments.of(5, 5, 6, 4), //Down Right
				Arguments.of(5, 5, 4, 6) //Down Right
				
			);
	}
	
	@ParameterizedTest
	@MethodSource("kingFailedMoves")
	void kingFailedMovesTest(int fromX, int fromY, int toX, int toY) {
		ChessPiece ki = factory.makePiece(WHITEKING);
		board.putPieceAt(ki, makeCoordinate(fromX, fromY));
		assertFalse(ki.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> kingFailedMoves() {
		return Stream.of(
				Arguments.of(8, 8, 9, 8), //Off the board to the right
				Arguments.of(8, 8, 8, 9), //Off the board up
				Arguments.of(8, 8, 9, 9), //Off the board diagonally
				Arguments.of(5, 5, 7, 5), //Two to the right
				Arguments.of(5, 5, 6, 7), //Just no
				Arguments.of(5, 5, 2, 3) //Teleportation!
				
			);
	}
	
	@Test
	void testCastling() { //Castling should work
		ChessPiece ki = factory.makePiece(WHITEKING);
		ChessPiece ro = factory.makePiece(WHITEROOK);
		board.putPieceAt(ki, makeCoordinate(5, 1));
		board.putPieceAt(ro, makeCoordinate(8, 1));
		assertTrue(ki.canMove(makeCoordinate(5, 1), makeCoordinate(7, 1), board));
	}
	
	@Test
	void testFailedCastling1() { //Test that you can't castle if the king has moves
		ChessPiece ki = factory.makePiece(WHITEKING);
		ki.setHasMoved();
		ChessPiece ro = factory.makePiece(WHITEROOK);
		board.putPieceAt(ki, makeCoordinate(5, 1));
		board.putPieceAt(ro, makeCoordinate(8, 1));
		assertFalse(ki.canMove(makeCoordinate(5, 1), makeCoordinate(7, 1), board));
	}
	
	@Test 
	void testFailedCastling2() { //Test that you can't castle if the rook has moved
		ChessPiece ki = factory.makePiece(WHITEKING);
		ChessPiece ro = factory.makePiece(WHITEROOK);
		ro.setHasMoved();
		board.putPieceAt(ki, makeCoordinate(5, 1));
		board.putPieceAt(ro, makeCoordinate(8, 1));
		assertFalse(ki.canMove(makeCoordinate(5, 1), makeCoordinate(7, 1), board));
	}
	
	//Beginning of Knights move tests
	
	@ParameterizedTest
	@MethodSource("knightBasicMoves")
	void knightBasicMovesTest(int fromX, int fromY, int toX, int toY) {
		ChessPiece kn = factory.makePiece(WHITEKNIGHT);
		ChessPiece pn = factory.makePiece(WHITEPAWN);
		ChessPiece baPn = factory.makePiece(BLACKPAWN);
		board.putPieceAt(kn, makeCoordinate(fromX, fromY));
		board.putPieceAt(pn, makeCoordinate(6,6)); //Test for if Knight can jump
		board.putPieceAt(pn, makeCoordinate(4,4));
		board.putPieceAt(pn, makeCoordinate(5,4));
		board.putPieceAt(baPn, makeCoordinate(4,7)); //Test for Knight capturing
		assertTrue(kn.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> knightBasicMoves() {
		return Stream.of(
				Arguments.of(5, 5, 7, 6),
				Arguments.of(5, 5, 7, 4),
				Arguments.of(5, 5, 6, 3),
				Arguments.of(5, 5, 6, 7),
				Arguments.of(5, 5, 3, 4),
				Arguments.of(5, 5, 3, 6),
				Arguments.of(5, 5, 4, 3),
				Arguments.of(5, 5, 4, 7) //In Knight range and a capture
			);
	}
	
	@ParameterizedTest
	@MethodSource("knightFailedMoves")
	void knightFailedMoves(int fromX, int fromY, int toX, int toY) {
		ChessPiece kn = factory.makePiece(WHITEKNIGHT);
		ChessPiece pn = factory.makePiece(WHITEPAWN);
		board.putPieceAt(kn, makeCoordinate(fromX, fromY));
		board.putPieceAt(pn, makeCoordinate(7, 6)); //Test for if Knight can jump
		assertFalse(kn.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	static Stream<Arguments> knightFailedMoves() {
		return Stream.of(
				Arguments.of(5, 5, 7, 6), //Onto a piece of same color
				Arguments.of(5, 5, 7, 5), //Outside knight movement range
				Arguments.of(5, 5, 6, 2), //Outside knight movement range
				Arguments.of(5, 5, 6, 9) //Outside board range
			);
	}
	
	//Beginning of Pawn move tests
	
	@ParameterizedTest
	@MethodSource("pawnBasicMoves")
	void pawnBasicMovesTest(int fromX, int fromY, int toX, int toY) {
		ChessPiece pa = factory.makePiece(WHITEPAWN);
		ChessPiece badPa = factory.makePiece(BLACKPAWN);
		board.putPieceAt(pa, makeCoordinate(fromX, fromY));
		board.putPieceAt(badPa, makeCoordinate(4,6));
		assertTrue(pa.canMove(makeCoordinate(fromX, fromY), makeCoordinate(toX, toY), board));
	}
	
	static Stream<Arguments> pawnBasicMoves() {
		return Stream.of(
				Arguments.of(5, 5, 5, 7), //Up 2
				Arguments.of(5, 5, 5, 6), //Up 1
				Arguments.of(5, 5, 4, 6) //Capture
			);
	}
	
	@ParameterizedTest
	@MethodSource("failedPawnMovements1")
	void testFailedPawnMovements1(int fromX, int fromY, int toX, int toY) {
		ChessPiece pa = factory.makePiece(WHITEPAWN);
		pa.setHasMoved();
		board.putPieceAt(pa, makeCoordinate(5, 4));
	}
	
	static Stream<Arguments> failedPawnMovements1() {
		return Stream.of(
				Arguments.of(5, 4, 5, 3), //Cannot move down if white
				Arguments.of(5, 4, 4, 6), //Cannot capture if there's no piece
				Arguments.of(5, 4, 5, 6), //Cannot move two if already moved
				Arguments.of(5, 4, 6, 4), //Cannot move anywhere right
				Arguments.of(5, 4, 4, 4) //Cannot move left
			);
	}
	
	@Test
	void testFailedPawnMovements2() {
		ChessPiece pa = factory.makePiece(WHITEPAWN);
		ChessPiece badPa = factory.makePiece(BLACKPAWN);
		board.putPieceAt(pa, makeCoordinate(5, 6));
		board.putPieceAt(badPa, makeCoordinate(5,7));
		assertFalse(badPa.canMove(makeCoordinate(5,7), makeCoordinate(5,8), board)); //Cannot move up if black
		assertFalse(badPa.canMove(makeCoordinate(5,7), makeCoordinate(5,6), board)); //Cannot run into another piece
	}
}
