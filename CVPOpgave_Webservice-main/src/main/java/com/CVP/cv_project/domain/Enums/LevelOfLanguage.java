package com.CVP.cv_project.domain.Enums;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum LevelOfLanguage {
    Modersmål(Integer.toString(4)),
    Flydende(Integer.toString(3)),
    God(Integer.toString(2)),
    Begynder(Integer.toString(1)),

    IkkeRelevant(Integer.toString(0)); //tilføjet til eksamen
        private final String state;

        private static final Map<String, LevelOfLanguage> BY_STATE = new HashMap<>();

        static {
            for (LevelOfLanguage s: values()) {
                BY_STATE.put(s.state, s);
            }
        }
    LevelOfLanguage(String state) {
            this.state = state;
        }

        public static LevelOfLanguage valueOfState(String state) {
            return BY_STATE.get(state);
        }

        public String getState() {
            return this.state;
        }

        public static LevelOfLanguage of(String stateName) {
            return Stream.of(LevelOfLanguage.values())
                    .filter(p -> p.getState() == stateName)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
}
