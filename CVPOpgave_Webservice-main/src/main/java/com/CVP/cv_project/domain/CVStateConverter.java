package com.CVP.cv_project.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

    @Converter(autoApply = true)
    public class CVStateConverter implements AttributeConverter<CVState, String> {

        @Override
        public String convertToDatabaseColumn(CVState cvState) {
            if (cvState == null) {
                return null;
            }
            return cvState.getState();
        }

        @Override
        public CVState convertToEntityAttribute(String state) {
            if (state == null) {
                return null;
            }

            return Stream.of(CVState.values())
                    .filter(c -> c.getState().equals(state))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
