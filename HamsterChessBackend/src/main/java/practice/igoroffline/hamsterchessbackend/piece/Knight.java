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

public class Knight {

    public static MovementAttackOpponentCheck knightMoves(Square knightSquare, Board board) {

        final var list = new ArrayList<Square>();

        final var movement1 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.PP_LETTER_NEXT_NUMBER, knightSquare, board);
        final var movement2 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.NN_NUMBER_PREVIOUS_LETTER, knightSquare, board);
        final var movement3 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.NN_NUMBER_NEXT_LETTER, knightSquare, board);
        final var movement4 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.NN_LETTER_NEXT_NUMBER, knightSquare, board);
        final var movement5 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.PP_LETTER_PREVIOUS_NUMBER, knightSquare, board);
        final var movement6 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.PP_NUMBER_PREVIOUS_LETTER, knightSquare, board);
        final var movement7 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.PP_NUMBER_NEXT_LETTER, knightSquare, board);
        final var movement8 = FindSquare.findSquare(Piece.KNIGHT, PieceMovement.NN_LETTER_PREVIOUS_NUMBER, knightSquare, board);

        final var movements = List.of(movement1, movement2, movement3, movement4,
                movement5, movement6, movement7, movement8);
        movements.forEach(movement -> list.addAll(movement.movementContact().squares()));
        final var opponentsKingInCheck = movements.stream().anyMatch(movementContact ->
                movementContact.movementContact().contact() == Contact.OPPONENT_KING);

        return new MovementAttackOpponentCheck(list, Collections.emptyList(), opponentsKingInCheck);
    }
}
