package practice.igoroffline.hamsterchessbackend.piece;

import practice.igoroffline.hamsterchessbackend.board.Board;
import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.Square;
import practice.igoroffline.hamsterchessbackend.piece.movement.Contact;
import practice.igoroffline.hamsterchessbackend.piece.movement.FindSquare;
import practice.igoroffline.hamsterchessbackend.piece.movement.MovementAttackOpponentCheck;
import practice.igoroffline.hamsterchessbackend.piece.movement.PieceMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bishop {

    public static MovementAttackOpponentCheck bishopMoves(Square bishopSquare, Board board) {

        final var list = new ArrayList<Square>();

        final var movement1 = FindSquare.findSquare(Piece.BISHOP, PieceMovement.PREVIOUS_LETTER_NEXT_NUMBER, bishopSquare, board);
        final var movement2 = FindSquare.findSquare(Piece.BISHOP, PieceMovement.NEXT_LETTER_NEXT_NUMBER, bishopSquare, board);
        final var movement3 = FindSquare.findSquare(Piece.BISHOP, PieceMovement.PREVIOUS_LETTER_PREVIOUS_NUMBER, bishopSquare, board);
        final var movement4 = FindSquare.findSquare(Piece.BISHOP, PieceMovement.NEXT_LETTER_PREVIOUS_NUMBER, bishopSquare, board);

        final var movements = List.of(movement1, movement2, movement3, movement4);
        movements.forEach(movement -> list.addAll(movement.movementContact().squares()));
        final var opponentsKingInCheck = movements.stream().anyMatch(movementContact ->
                movementContact.movementContact().contact() == Contact.OPPONENT_KING);

        return new MovementAttackOpponentCheck(list, Optional.empty(), opponentsKingInCheck);
    }
}
