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

        final var filledSquares = List.of(new Square(Letter.E, Number2.N4, Piece.KING, PieceColor.WHITE));

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
        // TODO implement me
        throw new UnsupportedOperationException();
    }

    public Optional<Square> findPreviousNumberSquare(Letter letter, Number2 number) {
        // TODO implement me
        throw new UnsupportedOperationException();
    }

    public Optional<Square> findNextLetterSquare(Letter letter, Number2 number) {
        // TODO implement me
        throw new UnsupportedOperationException();
    }

    public Optional<Square> findPreviousLetterSquare(Letter letter, Number2 number) {
        // TODO implement me
        throw new UnsupportedOperationException();
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
