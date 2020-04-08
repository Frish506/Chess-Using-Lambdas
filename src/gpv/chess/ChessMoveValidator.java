package gpv.chess;

@FunctionalInterface
public interface ChessMoveValidator<ChessPiece, Coord, Board> {
	abstract boolean isValidMove(ChessPiece p, Coord from, Coord to, Board b);
}	
