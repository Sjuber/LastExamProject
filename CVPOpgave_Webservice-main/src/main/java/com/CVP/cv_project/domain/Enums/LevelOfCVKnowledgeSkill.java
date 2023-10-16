package com.CVP.cv_project.domain.Enums;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum LevelOfCVKnowledgeSkill {
    Lettere_øvet(Integer.toString(1)),
    Øvet(Integer.toString(2)),
    Erfaren(Integer.toString(3)),
    Ekspert(Integer.toString(4));
        private final String state;

        private static final Map<String, LevelOfCVKnowledgeSkill> BY_STATE = new HashMap<>();

        static {
            for (LevelOfCVKnowledgeSkill s: values()) {
                BY_STATE.put(s.state, s);
            }
        }
        LevelOfCVKnowledgeSkill(String state) {
            this.state = state;
        }

        public static LevelOfCVKnowledgeSkill valueOfState(String state) {
            return BY_STATE.get(state);
        }

        public String getState() {
            return this.state;
        }

        public static LevelOfCVKnowledgeSkill of(String stateName) {
            return Stream.of(LevelOfCVKnowledgeSkill.values())
                    .filter(p -> p.getState() == stateName)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
}
