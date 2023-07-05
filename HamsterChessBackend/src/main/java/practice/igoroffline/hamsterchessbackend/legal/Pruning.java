package practice.igoroffline.hamsterchessbackend.legal;

import practice.igoroffline.hamsterchessbackend.board.Piece;
import practice.igoroffline.hamsterchessbackend.board.PieceColor;
import practice.igoroffline.hamsterchessbackend.main.GameMaster;
import practice.igoroffline.hamsterchessbackend.main.Messages;
import practice.igoroffline.hamsterchessbackend.piece.Rook;

public class Pruning {

    public static void prune(GameMaster gameMaster, PieceColor pieceColor) {

        final var oppositePieceColor = pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;

        gameMaster.getBoard().getBoard().forEach(square -> {
            if (square.getPieceColor() == oppositePieceColor && square.getPiece() == Piece.ROOK) {
                final var rookMoves = Rook.rookMoves(square, gameMaster.getBoard());
                if (rookMoves.opponentsKingInCheck()) {
                    kingStillInCheck(gameMaster, pieceColor);
                }
            }
        });
    }

    private static void kingStillInCheck(GameMaster gameMaster, PieceColor pieceColor) {
        switch (pieceColor) {
            case WHITE -> gameMaster.setWhiteKingInCheck(true);
            case BLACK -> gameMaster.setBlackKingInCheck(true);
            default -> throw new IllegalArgumentException(Messages.UNKNOWN_PIECE_COLOR);
        }
    }
}
