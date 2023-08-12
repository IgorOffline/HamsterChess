package practice.igoroffline.hamsterchessbackend.piece.movement;

import practice.igoroffline.hamsterchessbackend.board.Board;
import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.PieceColor;
import practice.igoroffline.hamsterchessbackend.board.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FindSquare {

    public static MovementContact findSquare(Piece piece, PieceMovement pieceMovement, Square pieceSquare, Board board) {

        final var moves = new ArrayList<Square>();

        final var pieceColor = pieceSquare.getPieceColor() == PieceColor.WHITE ? PieceColor.WHITE : PieceColor.BLACK;
        final var oppositePieceColor = pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        final var doWhilePieces = List.of(Piece.ROOK, Piece.BISHOP);

        Optional<Square> square = Optional.of(new Square(pieceSquare.getLetter(), pieceSquare.getNumber(), Piece.NONE, PieceColor.NONE));
        var contact = Contact.NONE;

        do {
            if (piece == Piece.ROOK) {
                square = switch(pieceMovement) {
                    case NEXT_NUMBER -> board.findNextNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case PREVIOUS_NUMBER -> board.findPreviousNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case NEXT_LETTER -> board.findNextLetterSquare(square.get().getLetter(), square.get().getNumber());
                    case PREVIOUS_LETTER -> board.findPreviousLetterSquare(square.get().getLetter(), square.get().getNumber());
                    default -> throw new IllegalArgumentException("Illegal ROOK movement");
                };
            } else if (piece == Piece.BISHOP) {
                square = switch(pieceMovement) {
                    case PREVIOUS_LETTER_NEXT_NUMBER -> board.findPreviousLetterNextNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case NEXT_LETTER_NEXT_NUMBER -> board.findNextLetterNextNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case PREVIOUS_LETTER_PREVIOUS_NUMBER -> board.findPreviousLetterPreviousNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case NEXT_LETTER_PREVIOUS_NUMBER -> board.findNextLetterPreviousNumberSquare(square.get().getLetter(), square.get().getNumber());
                    default -> throw new IllegalArgumentException("Illegal BISHOP movement");
                };
            } else if (piece == Piece.KNIGHT) {
                square = switch (pieceMovement) {
                    case PP_LETTER_NEXT_NUMBER -> board.findPpLetterNextNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case NN_NUMBER_PREVIOUS_LETTER -> board.findNnNumberPreviousLetterSquare(square.get().getLetter(), square.get().getNumber());
                    case NN_NUMBER_NEXT_LETTER -> board.findNnNumberNextLetterSquare(square.get().getLetter(), square.get().getNumber());
                    case NN_LETTER_NEXT_NUMBER -> board.findNnLetterNextNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case PP_LETTER_PREVIOUS_NUMBER -> board.findPpLetterPreviousNumberSquare(square.get().getLetter(), square.get().getNumber());
                    case PP_NUMBER_PREVIOUS_LETTER -> board.findPpNumberPreviousLetterSquare(square.get().getLetter(), square.get().getNumber());
                    case PP_NUMBER_NEXT_LETTER -> board.findPpNumberNextLetterSquare(square.get().getLetter(), square.get().getNumber());
                    case NN_LETTER_PREVIOUS_NUMBER -> board.findNnLetterPreviousNumberSquare(square.get().getLetter(), square.get().getNumber());
                    default -> throw new IllegalArgumentException("Illegal KNIGHT movement");
                };
            }

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
        } while (square.isPresent() && contact == Contact.NONE && doWhilePieces.contains(piece));

        return new MovementContact(moves, contact);
    }
}
