package org.nerix.automob;

public record Action(float moveDx, float moveDz, float attackProb, float shootYaw, float jumpProb) {
    public static Action zero() { return new Action(0f, 0f, 0f, 0f, 0f); }
}
