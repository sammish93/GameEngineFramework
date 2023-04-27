package sammish93.tbage.enums;

/**
 * These are the current encounter patterns available.
 * The 'FIXED' pattern allows the developer to decide exactly how a player will traverse between encounters.
 * The 'RANDOM' pattern will automatically generate a linear progression of encounters from an encounter pool.
 */
public enum EncounterPattern {
    FIXED,
    RANDOM
}
