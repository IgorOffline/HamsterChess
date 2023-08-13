package practice.igoroffline.hamsterchessbackend.piece;

import practice.igoroffline.hamsterchessbackend.board.Board;
import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.Square;
import practice.igoroffline.hamsterchessbackend.piece.movement.Contact;
import practice.igoroffline.hamsterchessbackend.piece.movement.FindSquare;
import practice.igoroffline.hamsterchessbackend.piece.movement.MovementAttackOpponentCheck;
import practice.igoroffline.hamsterchessbackend.piece.movement.PieceMovement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rook {

    public static MovementAttackOpponentCheck rookMoves(Square rookSquare, Board board) {

        final var list = new ArrayList<Square>();

        final var movement1 = FindSquare.findSquare(Piece.ROOK, PieceMovement.NEXT_NUMBER, rookSquare, board);
        final var movement2 = FindSquare.findSquare(Piece.ROOK, PieceMovement.PREVIOUS_NUMBER, rookSquare, board);
        final var movement3 = FindSquare.findSquare(Piece.ROOK, PieceMovement.NEXT_LETTER, rookSquare, board);
        final var movement4 = FindSquare.findSquare(Piece.ROOK, PieceMovement.PREVIOUS_LETTER, rookSquare, board);

        final var movements = List.of(movement1, movement2, movement3, movement4);
        movements.forEach(movement -> list.addAll(movement.squares()));
        final var opponentsKingInCheck = movements.stream().anyMatch(movementContact ->
                movementContact.contact() == Contact.OPPONENT_KING);

        return new MovementAttackOpponentCheck(list, Collections.emptyList(), opponentsKingInCheck);
    }
}
