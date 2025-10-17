package org.nerix.automob;

public final class LiveStats {
    private static final LiveStats I = new LiveStats();
    public static LiveStats get() { return I; }

    // EWMA (~par minute)
    private double rewardPerMin = 0.0;
    private double dmgToPlayerPerMin = 0.0;
    private int agentKills = 0;
    private int agentDeaths = 0;

    private static double ema(double prev, double x, double alpha) {
        return prev * (1 - alpha) + x * alpha;
    }

    /** amount = demi-coeurs (½ coeur = 1.0) */
    public void addPlayerDamage(double amountHalfHearts) {
        dmgToPlayerPerMin = ema(dmgToPlayerPerMin, amountHalfHearts * 60.0 / 20.0, 0.1);
    }

    /** shaping local (le backend RL calculera sa reward propre plus tard) */
    public void addReward(double r) {
        rewardPerMin = ema(rewardPerMin, r * 60.0 / 20.0, 0.1);
    }

    public void incAgentKill()  { agentKills++; }
    public void incAgentDeath() { agentDeaths++; }

    public double rpm() { return rewardPerMin; }
    public double dpm() { return dmgToPlayerPerMin; }
    public int k() { return agentKills; }
    public int d() { return agentDeaths; }
}
