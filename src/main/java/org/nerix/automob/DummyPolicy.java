package org.nerix.automob;

import java.util.concurrent.ThreadLocalRandom;

public final class DummyPolicy implements Policy {
    @Override
    public Action act(float[] f) {
        float vx = f[13], vz = f[14];
        float len = (float) Math.max(1e-5, Math.sqrt(vx*vx + vz*vz));
        vx /= len; vz /= len;

        var rnd = ThreadLocalRandom.current();
        vx += (float) (rnd.nextGaussian() * 0.05);
        vz += (float) (rnd.nextGaussian() * 0.05);

        return new Action(clamp(vx), clamp(vz), 0f, 0f, 0f);
    }

    private static float clamp(float v) {
        if (v > 1f) return 1f;
        if (v < -1f) return -1f;
        return v;
    }
}
