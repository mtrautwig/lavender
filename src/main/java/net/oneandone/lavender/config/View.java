package net.oneandone.lavender.config;

import net.oneandone.sushi.cli.ArgumentException;

import java.util.HashMap;
import java.util.Map;

public class View {
    /** maps type to target */
    private final Map<String, Target> map;

    public View() {
        this.map = new HashMap<>();
    }

    public void add(String type, Cluster cluster, Docroot docroot, Alias alias) {
        map.put(type, new Target(cluster, docroot, alias));
    }

    public Target get(String type) {
        Target target;

        target = map.get(type);
        if (target == null) {
            throw new ArgumentException("no such type: " + type);
        }
        return target;
    }
}
