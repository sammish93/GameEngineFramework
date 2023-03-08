package no.hiof.samuelcd.tbage.models.feats;

import java.util.Objects;
import java.util.TreeMap;

public class FeatPool {

    private TreeMap<String, Feat> featMap;

    private FeatPool(TreeMap<String, Feat> featMap) {
        // On null it should use a static ability table that is hard coded.
        this.featMap = Objects.requireNonNullElseGet(featMap, TreeMap::new);
    }

    public static FeatPool create() {
        return new FeatPool(null);
    }

    public static FeatPool create(TreeMap<String, Feat> featMap) {
        return new FeatPool(featMap);
    }

    public Feat getFeatFromPool(String abilityName) {
        return featMap.get(abilityName);
    }

    public void addFeatToPool(Feat feat) {
        featMap.put(feat.getName(), feat);
    }

    public void removeFeatFromPool(Feat feat) {
        featMap.remove(feat.getName());
    }

    public void removeFeatFromPool(String featName) {
        featMap.remove(featName);
    }
}
