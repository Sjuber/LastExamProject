package com.CVP.cv_project.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum CVState {
    inDraft("Kladde"),
    published("Udgivet"),
    archived("Arkiveret");
    private final String state;

    private static final Map<String, CVState> BY_STATE = new HashMap<>();

    static {
        for (CVState s: values()) {
            BY_STATE.put(s.state, s);
        }
    }
    CVState(String state) {
        this.state = state;
    }

    public static CVState valueOfState(String state) {
        return BY_STATE.get(state);
    }

    public String getState() {
        return this.state;
    }

    public static CVState of(String stateName) {
        return Stream.of(CVState.values())
                .filter(p -> p.getState() == stateName)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

