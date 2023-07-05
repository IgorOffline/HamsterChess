package practice.igoroffline.hamsterchessbackend.piece.movement;

import practice.igoroffline.hamsterchessbackend.board.Square;

import java.util.List;

public record MovementContact(List<Square> squares, Contact contact) {
}
