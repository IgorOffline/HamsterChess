package practice.igoroffline.hamsterchessbackend.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.piece.movement.pawn.PawnAttackDirection;
import practice.igoroffline.hamsterchessbackend.piece.movement.pawn.PawnMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
public class Board {

    private final List<Square> board;

    public Board() {
        this.board = new ArrayList<>();
        createBoard();
    }

    public Board(List<Square> board) {
        this.board = board;
    }

    private void createBoard() {

//        final var filledSquares = List.of(
//                new Square(Letter.C, Number2.N6, Piece.KING, PieceColor.BLACK),
//                new Square(Letter.C, Number2.N4, Piece.ROOK, PieceColor.BLACK),
//                new Square(Letter.E, Number2.N6, Piece.BISHOP, PieceColor.BLACK),
//                new Square(Letter.B, Number2.N6, Piece.KNIGHT, PieceColor.BLACK),
//                new Square(Letter.A, Number2.N7, Piece.PAWN, PieceColor.BLACK),
//                new Square(Letter.E, Number2.N3, Piece.KING, PieceColor.WHITE),
//                new Square(Letter.D, Number2.N2, Piece.ROOK, PieceColor.WHITE),
//                new Square(Letter.G, Number2.N4, Piece.BISHOP, PieceColor.WHITE),
//                new Square(Letter.F, Number2.N2, Piece.KNIGHT, PieceColor.WHITE),
//                new Square(Letter.H, Number2.N2, Piece.PAWN, PieceColor.WHITE));

        final var filledSquares = List.of(
                new Square(Letter.C, Number2.N6, Piece.KING, PieceColor.BLACK),
                new Square(Letter.A, Number2.N7, Piece.PAWN, PieceColor.BLACK),
                new Square(Letter.G, Number2.N5, Piece.PAWN, PieceColor.BLACK),
                new Square(Letter.G, Number2.N6, Piece.PAWN, PieceColor.BLACK),
                new Square(Letter.H, Number2.N6, Piece.PAWN, PieceColor.BLACK),

                new Square(Letter.F, Number2.N7, Piece.PAWN, PieceColor.BLACK),
                new Square(Letter.G, Number2.N7, Piece.PAWN, PieceColor.BLACK),
                new Square(Letter.H, Number2.N7, Piece.PAWN, PieceColor.BLACK),

                new Square(Letter.E, Number2.N3, Piece.KING, PieceColor.WHITE),
                new Square(Letter.H, Number2.N4, Piece.PAWN, PieceColor.WHITE),
                new Square(Letter.H, Number2.N2, Piece.PAWN, PieceColor.WHITE));

        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                final var letter = LetterNumber.getLetterEnum(i);
                final var number = LetterNumber.getNumberEnumReverse(j);
                var letterNumberInFilled = false;
                for (final var filled : filledSquares) {
                    if (filled.getLetter() == letter && filled.getNumber() == number) {
                        board.add(filled);
                        letterNumberInFilled = true;
                        break;
                    }
                }
                if (!letterNumberInFilled) {
                    final var square = new Square(letter, number, Piece.NONE, PieceColor.NONE);
                    board.add(square);
                }
            }
        }
    }

    public boolean squareFound(int i, int j, Square square) {
        return i == square.getLetter().index && j == square.getNumber().index;
    }

    public Optional<Square> findNextNumberSquare(Letter letter, Number2 number) {
        final var nextNumberIndex = number.index + 1;
        if (LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nextNumberIndex))) {
            final var squareIndex = (8 * (7 - nextNumberIndex)) + letter.index;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPreviousNumberSquare(Letter letter, Number2 number) {
        final var previousNumberIndex = number.index - 1;
        if (LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(previousNumberIndex))) {
            final var squareIndex = (8 * (7 - previousNumberIndex)) + letter.index;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNextLetterSquare(Letter letter, Number2 number) {
        final var nextLetterIndex = letter.index + 1;
        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nextLetterIndex))) {
            final var squareIndex = (8 * (7 - number.index)) + nextLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPreviousLetterSquare(Letter letter, Number2 number) {
        final var previousLetterIndex = letter.index - 1;
        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(previousLetterIndex))) {
            final var squareIndex = (8 * (7 - number.index)) + previousLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPreviousLetterNextNumberSquare(Letter letter, Number2 number) {

        final var previousLetterIndex = letter.index - 1;
        final var nextNumberIndex = number.index + 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(previousLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nextNumberIndex))) {
            final var squareIndex = (8 * (7 - nextNumberIndex)) + previousLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNextLetterNextNumberSquare(Letter letter, Number2 number) {

        final var nextLetterIndex = letter.index + 1;
        final var nextNumberIndex = number.index + 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nextLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nextNumberIndex))) {
            final var squareIndex = (8 * (7 - nextNumberIndex)) + nextLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPreviousLetterPreviousNumberSquare(Letter letter, Number2 number) {

        final var previousLetterIndex = letter.index - 1;
        final var previousNumberIndex = number.index - 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(previousLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(previousNumberIndex))) {
            final var squareIndex = (8 * (7 - previousNumberIndex)) + previousLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNextLetterPreviousNumberSquare(Letter letter, Number2 number) {

        final var nextLetterIndex = letter.index + 1;
        final var previousNumberIndex = number.index - 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nextLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(previousNumberIndex))) {
            final var squareIndex = (8 * (7 - previousNumberIndex)) + nextLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPpLetterNextNumberSquare(Letter letter, Number2 number) {

        final var ppLetterIndex = letter.index - 2;
        final var nextNumberIndex = number.index + 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(ppLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nextNumberIndex))) {
            final var squareIndex = (8 * (7 - nextNumberIndex)) + ppLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNnNumberPreviousLetterSquare(Letter letter, Number2 number) {

        final var previousLetterIndex = letter.index - 1;
        final var nnNumberIndex = number.index + 2;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(previousLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nnNumberIndex))) {
            final var squareIndex = (8 * (7 - nnNumberIndex)) + previousLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNnNumberNextLetterSquare(Letter letter, Number2 number) {

        final var nextLetterIndex = letter.index + 1;
        final var nnNumberIndex = number.index + 2;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nextLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nnNumberIndex))) {
            final var squareIndex = (8 * (7 - nnNumberIndex)) + nextLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNnLetterNextNumberSquare(Letter letter, Number2 number) {

        final var nnLetterIndex = letter.index + 2;
        final var nextNumberIndex = number.index + 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nnLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(nextNumberIndex))) {
            final var squareIndex = (8 * (7 - nextNumberIndex)) + nnLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPpLetterPreviousNumberSquare(Letter letter, Number2 number) {

        final var ppLetterIndex = letter.index - 2;
        final var previousNumberIndex = number.index - 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(ppLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(previousNumberIndex))) {
            final var squareIndex = (8 * (7 - previousNumberIndex)) + ppLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPpNumberPreviousLetterSquare(Letter letter, Number2 number) {

        final var previousLetterIndex = letter.index - 1;
        final var ppNumberIndex = number.index - 2;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(previousLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(ppNumberIndex))) {
            final var squareIndex = (8 * (7 - ppNumberIndex)) + previousLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPpNumberNextLetterSquare(Letter letter, Number2 number) {

        final var nextLetterIndex = letter.index + 1;
        final var ppNumberIndex = number.index - 2;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nextLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(ppNumberIndex))) {
            final var squareIndex = (8 * (7 - ppNumberIndex)) + nextLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findNnLetterPreviousNumberSquare(Letter letter, Number2 number) {

        final var nnLetterIndex = letter.index + 2;
        final var previousNumberIndex = number.index - 1;

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(nnLetterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(previousNumberIndex))) {
            final var squareIndex = (8 * (7 - previousNumberIndex)) + nnLetterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPawnMoveSquare(PawnMove pawnMove, PieceColor pieceColor, Letter letter, Number2 number) {

        var numberIndex = -1;

        switch (pawnMove) {
            case ONE_SQUARE -> numberIndex = switch(pieceColor) {
                case WHITE -> number.index + 1;
                case BLACK -> number.index - 1;
                case NONE -> throw new IllegalArgumentException("PieceColor.NONE not supported!");
            };
            case TWO_SQUARES -> numberIndex = switch(pieceColor) {
                case WHITE -> number.index + 2;
                case BLACK -> number.index - 2;
                case NONE -> throw new IllegalArgumentException("PieceColor.NONE not supported! (2)");
            };
        }

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(letter.index)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(numberIndex))) {
            final var squareIndex = (8 * (7 - numberIndex)) + letter.index;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Optional<Square> findPawnAttackPreviousOrNextLetterSquare(PawnAttackDirection movementDirection, PieceColor pieceColor, Letter letter, Number2 number) {

        final var letterIndex = switch(movementDirection) {
            case PREVIOUS -> letter.index - 1;
            case NEXT -> letter.index + 1;
        };

        final var numberIndex = switch(pieceColor) {
            case WHITE -> number.index + 1;
            case BLACK -> number.index - 1;
            case NONE -> throw new IllegalArgumentException("PieceColor.NONE not supported!");
        };

        if (LetterNumber.isEnumLegal(LetterNumber.getLetterEnum(letterIndex)) &&
                LetterNumber.isEnumLegal(LetterNumber.getNumberEnum(numberIndex))) {
            final var squareIndex = (8 * (7 - numberIndex)) + letterIndex;
            return Optional.of(board.get(squareIndex));
        }

        return Optional.empty();
    }

    public Board deepCopy() {
        final var list = new ArrayList<Square>();
        for (int i = 0; i < board.size(); i++) {
            final var oldSquare = board.get(i);
            list.add(oldSquare.copy());
        }
        return new Board(list);
    }
}
