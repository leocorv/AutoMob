package org.nerix.automob;

public interface Policy {
    Action act(float[] features);
}
