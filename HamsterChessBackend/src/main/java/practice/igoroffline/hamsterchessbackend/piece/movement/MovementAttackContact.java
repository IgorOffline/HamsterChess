package practice.igoroffline.hamsterchessbackend.piece.movement;

import java.util.Optional;

public record MovementAttackContact(MovementContact movementContact, Optional<MovementContact> attackContact) {
}
