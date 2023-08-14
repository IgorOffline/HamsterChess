package practice.igoroffline.hamsterchessbackend.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.Board;
import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.PieceColor;
import practice.igoroffline.hamsterchessbackend.board.Square;
import practice.igoroffline.hamsterchessbackend.legal.EnrichedLegalMoves;
import practice.igoroffline.hamsterchessbackend.legal.LegalMoves;

import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
public class GameMaster {

    private final Board board;
    private final LegalMoves legalMoves;

    @Setter
    private Optional<Square> fromSquare;
    @Setter
    private Optional<Square> toSquare;

    private boolean whiteToMove = true;

    @Setter
    private boolean whiteKingInCheck = false;
    @Setter
    private boolean blackKingInCheck = false;

    @Setter
    private boolean whiteKingCheckmated = false;
    @Setter
    private boolean blackKingCheckmated = false;

    @Setter
    private boolean enPassantPossible = false;

    public EnrichedLegalMoves getEnrichedLegalMoves() {
        return new EnrichedLegalMoves(legalMoves);
    }

    public GameMaster() {
        this.board = new Board();
        this.legalMoves = new LegalMoves();
        legalMoves.calculate(this);
    }

    public GameMaster(Board board, LegalMoves legalMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
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

        enPassantPossible = false;

        // assert
        fromSquare.orElseThrow();
        toSquare.orElseThrow();

        if (fromSquare.get().getPiece() == Piece.PAWN && (Math.abs(fromSquare.get().getNumber().index - toSquare.get().getNumber().index) == 2)) {
            enPassantPossible = true;
        }

        toSquare.get().setPiece(fromSquare.get().getPiece());
        toSquare.get().setPieceColor(fromSquare.get().getPieceColor());
        fromSquare.get().setPiece(Piece.NONE);
        fromSquare.get().setPieceColor(PieceColor.NONE);

        whiteToMove = !whiteToMove;
    }

    private void calculate() {
        legalMoves.calculate(this);
    }
}
