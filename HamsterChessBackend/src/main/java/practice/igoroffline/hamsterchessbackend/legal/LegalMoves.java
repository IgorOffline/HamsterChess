package practice.igoroffline.hamsterchessbackend.legal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.PieceColor;
import practice.igoroffline.hamsterchessbackend.board.Square;
import practice.igoroffline.hamsterchessbackend.main.GameMaster;
import practice.igoroffline.hamsterchessbackend.piece.King;
import practice.igoroffline.hamsterchessbackend.piece.Rook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
public class LegalMoves {

    private Map<Square, List<Square>> legalMoves = new HashMap<>();

    public void calculate(GameMaster gameMaster) {

        final var phase1LegalMoves = new HashMap<Square, List<Square>>();
        final var phase2LegalMoves = new HashMap<Square, List<Square>>();

        final var pieceColor = gameMaster.isWhiteToMove() ? PieceColor.WHITE : PieceColor.BLACK;
        final var oppositePieceColor = pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        gameMaster.setWhiteKingInCheck(false);
        gameMaster.setBlackKingInCheck(false);

        Optional<Square> king = Optional.empty();

        for (final var square : gameMaster.getBoard().getBoard()) {
            if (square.getPiece() == Piece.KING && square.getPieceColor() == pieceColor) {
                king = Optional.of(square);
                break;
            }
        }

        final var kingLegalMoves = King.kingMoves(king.orElseThrow(), gameMaster.getBoard());

        gameMaster.getBoard().getBoard().forEach(boardSquare -> {
            if (boardSquare.getPiece() == Piece.ROOK && boardSquare.getPieceColor() == pieceColor) {
                final var rookMoves = Rook.rookMoves(boardSquare, gameMaster.getBoard());
                phase1LegalMoves.put(boardSquare, rookMoves.squares());
                kingLegalMoves.removeIf(square ->
                        square.getLetter() == boardSquare.getLetter() && square.getNumber() == boardSquare.getNumber());
            } else if (boardSquare.getPiece() == Piece.ROOK && boardSquare.getPieceColor() == oppositePieceColor) {
                final var oppositeRookMoves = Rook.rookMoves(boardSquare, gameMaster.getBoard());
                kingLegalMoves.removeAll(oppositeRookMoves.squares());
                if (oppositeRookMoves.opponentsKingInCheck()) {
                    gameMaster.setWhiteKingInCheck(pieceColor == PieceColor.WHITE);
                    gameMaster.setBlackKingInCheck(pieceColor == PieceColor.BLACK);
                }
            }
        });

        phase1LegalMoves.put(king.get(), kingLegalMoves);

        if (!gameMaster.isWhiteKingInCheck() && !gameMaster.isBlackKingInCheck()) {
            legalMoves = phase1LegalMoves;
        } else {
            phase1LegalMoves.keySet().forEach(piece -> {
                final var pieceLegalMoves = phase1LegalMoves.get(piece);
                final var prunedMoves = pruneMoves(gameMaster, pieceLegalMoves, piece);
                phase2LegalMoves.put(piece, prunedMoves);
            });

            legalMoves = phase2LegalMoves;

            checkmateCheck(gameMaster);
        }
    }

    private List<Square> pruneMoves(GameMaster gameMaster, List<Square> pieceLegalMoves, Square piece) {

        final var prunedMoves = new ArrayList<Square>();

        pieceLegalMoves.forEach(legalMove -> {
            final var newBoard = gameMaster.getBoard().deepCopy();
            final var newLegalMoves = new LegalMoves();
            final var newGameMaster = new GameMaster(newBoard, newLegalMoves);
            final var pieceNewBoard = newBoard.getBoard().stream().filter(square ->
                    square.getLetter() == piece.getLetter() && square.getNumber() == piece.getNumber()).findFirst();
            // assert
            pieceNewBoard.orElseThrow();
            newGameMaster.setFromSquare(pieceNewBoard);
            final var toSquareNewBoard = newBoard.getBoard().stream().filter(
                    square -> square.getLetter() == legalMove.getLetter() && square.getNumber() == legalMove.getNumber()).findFirst();
            // assert
            toSquareNewBoard.orElseThrow();
            newGameMaster.setToSquare(toSquareNewBoard);
            newGameMaster.move();
            Pruning.prune(newGameMaster, piece.getPieceColor());

            final var pruneWhite = piece.getPieceColor() == PieceColor.WHITE && !newGameMaster.isWhiteKingInCheck();
            final var pruneBlack = piece.getPieceColor() == PieceColor.BLACK && !newGameMaster.isBlackKingInCheck();

            if (pruneWhite || pruneBlack) {
                prunedMoves.add(toSquareNewBoard.get());
            }
        });

        return prunedMoves;
    }

    private void checkmateCheck(GameMaster gameMaster) {

        var legalMovesCounter = 0;

        for (final var piecesLegalMoves : legalMoves.values()) {
            legalMovesCounter += piecesLegalMoves.size();
        }

        if (legalMovesCounter == 0) {
            if (gameMaster.isWhiteKingInCheck()) {
                gameMaster.setWhiteKingCheckmated(true);
            } else {
                gameMaster.setBlackKingCheckmated(true);
            }
        }
    }
}
