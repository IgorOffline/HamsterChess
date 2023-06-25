package practice.igoroffline.hamsterChessBackend.board;

public enum Letter {
    L(-1), A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7), R(8);

    public final int index;

    Letter(int index) {
        this.index = index;
    }
}
