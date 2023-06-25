package practice.igoroffline.hamsterChessBackend.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class Board {

    private final List<Square> board;

    public Board() {
        this.board = new ArrayList<>();
        createBoard();
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
}
