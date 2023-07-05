package practice.igoroffline.hamsterchessbackend.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

        final var filledSquares = List.of(
                new Square(Letter.E, Number2.N5, Piece.KING, PieceColor.BLACK),
                new Square(Letter.E, Number2.N3, Piece.KING, PieceColor.WHITE));

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

    public Board deepCopy() {
        final var list = new ArrayList<Square>();
        for (int i = 0; i < board.size(); i++) {
            final var oldSquare = board.get(i);
            list.add(oldSquare.copy());
        }
        return new Board(list);
    }
}
