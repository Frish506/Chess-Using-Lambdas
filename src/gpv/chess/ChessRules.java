package gpv.chess;

import static gpv.util.Coordinate.makeCoordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import gpv.util.Board;
import gpv.util.Coordinate;

public class ChessRules {
	
	//This lambda method checks if a piece can move vertically and not hit a piece
	public static ChessMoveValidator<ChessPiece, Coordinate, Board> verticleTest=
		(piece, from, to, board) -> {
			if(from.x != to.x) {return false;}
			
			OfInt inBetweens;
			if(to.y<from.y) {
				inBetweens = IntStream.range(to.y, from.y).sorted().iterator();
			}
			else { 
				inBetweens = IntStream.range(from.y, to.y).iterator(); //create an iterator to go over every in between x coordinate
			}
			inBetweens.nextInt();
			while(inBetweens.hasNext()) { //Go over iterator- if there is a piece in any spot between, return false
			if(board.getPieceAt(makeCoordinate(from.x, inBetweens.nextInt())) != null) return false;
			}
			
			return true;
		};
			
	//This lambda method checks if a piece can move horizontally and not hit a piece
	public static ChessMoveValidator<ChessPiece, Coordinate, Board> horizontalTest=
		(piece, from, to, board) -> {
			if(from.y != to.y) {return false;}
			OfInt inBetweens;
			if(to.x<from.x) {inBetweens = IntStream.range(to.x, from.x).iterator();} //
			else { inBetweens = IntStream.range(from.x, to.x).iterator(); } //create an iterator to go over every in between x coordinate
			inBetweens.nextInt();
			while(inBetweens.hasNext()) { //Go over iterator- if there is a piece in any spot between, return false
			if(board.getPieceAt(makeCoordinate(inBetweens.nextInt(), from.y)) != null) return false;
			}
			
			return true;
		};
		
	public static ChessMoveValidator<ChessPiece, Coordinate, Board> diagonalTest=
		(piece, from, to, board) -> {	
			if(Math.abs(to.x - from.x)!=Math.abs(to.y - from.y)) return false; //If not a diagonal, return false
			ChessPiece potentialEnemy = (ChessPiece) board.getPieceAt(to);
			boolean capturing = false;
			if(potentialEnemy != null && potentialEnemy.getColor() != piece.getColor()) capturing = true;
			
			
			boolean secondXSmaller = (to.x<from.x); //Testing if we need to go from the front to the end
			boolean secondYSmaller = (to.y<from.y); //or vice versa- streams can't do range from a high to a low number,
													//so have to go from low to high then reverse
			List<Integer> inBetweensX, inBetweensY;
			if(secondXSmaller) {
				inBetweensX = IntStream.rangeClosed(to.x, from.x).boxed().collect(Collectors.toList()); //create an iterator to go over every in between x coordinate
				Collections.reverse(inBetweensX);
			}
			else {
				inBetweensX = IntStream.rangeClosed(from.x, to.x).boxed().collect(Collectors.toList()); //create an iterator to go over every in between x coordinate
			}
			
			if(secondYSmaller) {
				inBetweensY = IntStream.rangeClosed(to.y, from.y).boxed().collect(Collectors.toList()); //create an iterator to go over every in between x coordinate
				Collections.reverse(inBetweensY);
			}
			else {
				inBetweensY = IntStream.rangeClosed(from.y, to.y).boxed().collect(Collectors.toList()); //create an iterator to go over every in between x coordinate
			}
			
			for(int i = 1; i<inBetweensX.size(); i++) { //Test if there's any pieces in the way
				if(board.getPieceAt(makeCoordinate(inBetweensX.get(i), inBetweensY.get(i))) != null)
				{
					if(i!=inBetweensX.size() && !capturing) return false;
				}
			}

			
			return true;
		};
		
	public static ChessMoveValidator<ChessPiece, Coordinate, Board> pawnTest=
			(piece, from, to, board) -> {	
				int vertical = to.y - from.y; //First get direction going in (pawns can only move in one direction)
				if(Math.abs(vertical) > 2 || vertical == 0) return false; //If you try to go more than two spaces or don't move vertically at all get out of here
				if(piece.getColor() == PlayerColor.WHITE && vertical < 0) return false; //If they try to go down as white, that's illegal
				if(piece.getColor() == PlayerColor.BLACK && vertical > 0) return false; //If they try to go up as black, that's illegal				
				//At this point we've established the pawn is moving in the correct direction and not too many spaces
				int horizontal = to.x - from.x; //Get how much it's moving horizontally
				if(!piece.hasMoved() && horizontal == 0 && Math.abs(vertical) == 2) return true; //Special case of moving vertically two on first move- that's okay
				if(Math.abs(vertical) == 2) return false; //Now that we've tested the special case, there's no other time the pawn can move forward two spaces
				if(Math.abs(horizontal) > 1)  return false;  // If they moved horizontally at all, it should be only one space
				ChessPiece dest = (ChessPiece) board.getPieceAt(to);
				if(Math.abs(horizontal) == 1 && dest == null) return false; //If they're trying to go diagonal and there's not a piece there, that's not allowed
				if(Math.abs(horizontal) == 0 && dest != null) return false; //If trying to capture a piece by just moving vertically, that's not allowed
				return true;
			};
			
	public static ChessMoveValidator<ChessPiece, Coordinate, Board> kingTest=
			(piece, from, to, board) -> {	
				int vertical = to.y - from.y; //First get the horizontal direction
				int horizontal = to.x - from.x; //Get how much it's moving horizontally
				//Now check if it's moving to the right by 2 and hasn't moved yet- this is the only situation where a castle is possible for the king
				if(!piece.hasMoved() && Math.abs(horizontal) == 2 && vertical == 0) {//First check if castling, because that's complicated and I don't wanna think about it
					ChessPiece potentialRook = (ChessPiece) board.getPieceAt(makeCoordinate(to.x+1, to.y));
					ChessPiece potentialKnight = (ChessPiece) board.getPieceAt(makeCoordinate(to.x, to.y));
					ChessPiece potentialBishop = (ChessPiece) board.getPieceAt(makeCoordinate(to.x-1, to.y));
					if(potentialRook != null && potentialRook.getName() == PieceName.ROOK && potentialRook.getColor() == piece.getColor() && !potentialRook.hasMoved()) { //If there's a piece at the location, and it's a rook that hasn't moved, I shall consider it...
						if(potentialKnight == null && potentialBishop == null) { return true; }
					}
				}
				if(Math.abs(vertical) > 1 || Math.abs(horizontal) > 1) return false; //If the king is trying to move more than one space in any direction
				return true;
			};
			
	public static ChessMoveValidator<ChessPiece, Coordinate, Board> knightTest=
			(piece, from, to, board) -> {	
				int vertical = to.y - from.y; //First get vertical amount
				int horizontal = to.x - from.x; //Get how much it's moving horizontally
				if( Math.abs(horizontal) < 1 || Math.abs(horizontal) > 2 || //If they're moving not 2 & 1 either horizontal or vertical, return false
					Math.abs(vertical) < 1 || Math.abs(vertical) > 2 ||
					Math.abs(horizontal) == Math.abs(vertical)) return false;
				return true;
			};
			
	public static boolean checkValidRule(ChessPiece movingPiece, Coordinate from, Coordinate to, Board b) {
		switch(movingPiece.getName()) {
			case BISHOP:
				return diagonalTest.isValidMove(movingPiece, from, to, b);
			case ROOK:
				return (verticleTest.isValidMove(movingPiece, from, to, b) || horizontalTest.isValidMove(movingPiece, from, to, b));
			case QUEEN:
				return (diagonalTest.isValidMove(movingPiece, from, to, b) || 
						verticleTest.isValidMove(movingPiece, from, to, b) || horizontalTest.isValidMove(movingPiece, from, to, b));
			case KING:
				return kingTest.isValidMove(movingPiece, from, to, b);
			case KNIGHT:
				return knightTest.isValidMove(movingPiece, from, to ,b);
			case PAWN:
				return pawnTest.isValidMove(movingPiece, from, to, b);
		}
		return false;
	
	}
}
