package practice.igoroffline.hamsterchessbackend.piece;

import practice.igoroffline.hamsterchessbackend.board.Board;
import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.PieceColor;
import practice.igoroffline.hamsterchessbackend.board.Square;
import practice.igoroffline.hamsterchessbackend.piece.movement.Contact;
import practice.igoroffline.hamsterchessbackend.piece.movement.MovementContact;
import practice.igoroffline.hamsterchessbackend.piece.movement.MovementOpponentCheck;
import practice.igoroffline.hamsterchessbackend.piece.movement.RookMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Rook {

    public static MovementOpponentCheck rookMoves(Square rookSquare, Board board) {

        final var list = new ArrayList<Square>();

        final var movement1 = findSquare(rookSquare, board, RookMovement.NEXT_NUMBER);
        final var movement2 = findSquare(rookSquare, board, RookMovement.PREVIOUS_NUMBER);
        final var movement3 = findSquare(rookSquare, board, RookMovement.NEXT_LETTER);
        final var movement4 = findSquare(rookSquare, board, RookMovement.PREVIOUS_LETTER);

        final var movements = List.of(movement1, movement2, movement3, movement4);
        movements.forEach(movement -> list.addAll(movement.squares()));
        final var opponentsKingInCheck = movements.stream().anyMatch(movementContact ->
                movementContact.contact() == Contact.OPPONENT_KING);

        return new MovementOpponentCheck(list, opponentsKingInCheck);
    }

    private static MovementContact findSquare(Square rookSquare, Board board, RookMovement rookMovement) {

        final var moves = new ArrayList<Square>();

        final var pieceColor = rookSquare.getPieceColor() == PieceColor.WHITE ? PieceColor.WHITE : PieceColor.BLACK;
        final var oppositePieceColor = pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        Optional<Square> square = Optional.of(new Square(rookSquare.getLetter(), rookSquare.getNumber(), Piece.NONE, PieceColor.NONE));
        var contact = Contact.NONE;

        do {
            square = switch(rookMovement) {
                case NEXT_NUMBER -> board.findNextNumberSquare(square.get().getLetter(), square.get().getNumber());
                case PREVIOUS_NUMBER -> board.findPreviousNumberSquare(square.get().getLetter(), square.get().getNumber());
                case NEXT_LETTER -> board.findNextLetterSquare(square.get().getLetter(), square.get().getNumber());
                case PREVIOUS_LETTER -> board.findPreviousLetterSquare(square.get().getLetter(), square.get().getNumber());
            };
            if (square.isPresent()) {
                if (square.get().getPiece() == Piece.NONE) {
                    moves.add(square.get());
                } else if (square.get().getPieceColor() == pieceColor) {
                    contact = Contact.FRIENDLY;
                } else if (square.get().getPieceColor() == oppositePieceColor) {
                    if (square.get().getPiece() == Piece.KING) {
                        contact = Contact.OPPONENT_KING;
                    } else {
                        moves.add(square.get());
                        contact = Contact.OPPONENT_NON_KING;
                    }
                }
            }
        } while (square.isPresent() && contact == Contact.NONE);

        return new MovementContact(moves, contact);
    }
}
