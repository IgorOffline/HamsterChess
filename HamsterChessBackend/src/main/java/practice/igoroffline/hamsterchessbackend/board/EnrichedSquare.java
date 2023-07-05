package practice.igoroffline.hamsterchessbackend.board;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class EnrichedSquare extends Square {

    public int getIndex() {
        return 123;
    }

    public EnrichedSquare(Square square) {
        super(square.getLetter(), square.getNumber(), square.getPiece(), square.getPieceColor());
    }
}
