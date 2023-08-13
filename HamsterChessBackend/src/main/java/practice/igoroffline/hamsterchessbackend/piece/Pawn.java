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

public class Pawn {

    public static MovementAttackOpponentCheck pawnMoves(Square pawnSquare, Board board) {

        final var movementSquares = new ArrayList<Square>();
        final var attackSquares = new ArrayList<Square>();

        final var movement1 = FindSquare.findSquare(Piece.PAWN, PieceMovement.PAWN_MOVE, pawnSquare, board);
        final var attack1 = FindSquare.findSquare(Piece.PAWN, PieceMovement.PAWN_ATTACK_PREVIOUS_LETTER, pawnSquare, board);
        final var attack2 = FindSquare.findSquare(Piece.PAWN, PieceMovement.PAWN_ATTACK_NEXT_LETTER, pawnSquare, board);

        final var movements = List.of(movement1);
        movements.stream().filter(movement -> movement.contact() == Contact.NONE)
                .forEach(movement -> movementSquares.addAll(movement.squares()));

        final var attacks = List.of(attack1, attack2);
        attacks.forEach(attack -> attackSquares.addAll(attack.squares()));
        attacks.stream().filter(attack -> attack.contact() == Contact.OPPONENT_NON_KING)
                .forEach(attack -> movementSquares.addAll(attack.squares()));

        final var opponentsKingInCheck = attacks.stream().anyMatch(movementContact ->
                movementContact.contact() == Contact.OPPONENT_KING);

        return new MovementAttackOpponentCheck(movementSquares, attackSquares, opponentsKingInCheck);
    }
}
