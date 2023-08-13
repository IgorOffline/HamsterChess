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

public class Pawn {

    public static MovementAttackOpponentCheck pawnMoves(Square pawnSquare, Board board) {

        final var list = new ArrayList<Square>();

        final var movement1 = FindSquare.findSquare(Piece.PAWN, PieceMovement.PAWN, pawnSquare, board);

        final var movements = List.of(movement1);
        movements.forEach(movement -> list.addAll(movement.movementContact().squares()));
        final var opponentsKingInCheck = movements.stream().anyMatch(movementContact ->
                movementContact.movementContact().contact() == Contact.OPPONENT_KING);

        return new MovementAttackOpponentCheck(list, Optional.empty(), opponentsKingInCheck);
    }
}
