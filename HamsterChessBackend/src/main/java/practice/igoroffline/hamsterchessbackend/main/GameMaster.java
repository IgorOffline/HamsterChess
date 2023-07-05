package practice.igoroffline.hamsterchessbackend.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.*;
import practice.igoroffline.hamsterchessbackend.legal.LegalMoves;

import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
public class GameMaster {

    private final Board board;
    private final LegalMoves legalMoves;
    private Optional<Square> fromSquare;
    private Optional<Square> toSquare;
    private boolean whiteToMove = true;

    @Setter
    private boolean whiteKingInCheck = false;
    @Setter
    private boolean blackKingInCheck = false;

    private boolean whiteKingCheckmated = false;
    private boolean blackKingCheckmated = false;
    private final EnrichedBoard enrichedBoard;

    public GameMaster() {
        this.board = new Board();
        this.legalMoves = new LegalMoves();
        this.enrichedBoard = new EnrichedBoard(board);
    }

    public void moveAndCalculate() {
        if (legalMoves.getLegalMoves().containsKey(fromSquare.orElseThrow())) {
            legalMoves.getLegalMoves().get(fromSquare.get()).forEach(pieceLegalMove -> {
                if (toSquareEquals(pieceLegalMove)) {
                    moveAndCalculateInner();

                    return;
                }
            });
        }
    }

    private boolean toSquareEquals(Square square) {
        return Square.isLetterNumberEqual(toSquare.orElseThrow().getLetter(), square.getLetter(), toSquare.get().getNumber(), square.getNumber());
    }

    private void moveAndCalculateInner() {
        move();
        calculate();
    }

    public void move() {
        toSquare.orElseThrow().setPiece(fromSquare.orElseThrow().getPiece());
        toSquare.get().setPieceColor(fromSquare.get().getPieceColor());
        fromSquare.get().setPiece(Piece.NONE);
        fromSquare.get().setPieceColor(PieceColor.NONE);
    }

    private void calculate() {
        whiteToMove = !whiteToMove;

        legalMoves.calculate(this);
    }
}
