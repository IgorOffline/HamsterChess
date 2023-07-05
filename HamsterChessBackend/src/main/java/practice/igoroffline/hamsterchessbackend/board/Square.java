package practice.igoroffline.hamsterchessbackend.board;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Square {

    private Letter letter;
    private Number2 number;

    @Setter
    private Piece piece;
    @Setter
    private PieceColor pieceColor;

    public int getIndex() {
        return letter.index + 8 * LetterNumber.getNumberIndexReverse(number);
    }

    public static boolean isLetterNumberEqual(Letter letter1, Letter letter2, Number2 number1, Number2 number2) {
        return letter1 == letter2 && number1 == number2;
    }

    public Square copy() {
        return new Square(letter, number, piece, pieceColor);
    }
}
