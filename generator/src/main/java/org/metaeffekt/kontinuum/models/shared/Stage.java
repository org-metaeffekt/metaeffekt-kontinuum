package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;

public enum Stage {
    PRE(".pre"),
    FETCH("00_fetched"),
    EXTRACT("01_extracted"),
    PREPARE("02_prepared"),
    AGGREGATE("03_aggregated"),
    RESOLVE("04_resolved"),
    SCAN("05_scanned"),
    ADVISE("06_advised"),
    GROUP("07_grouped"),
    REPORT("08_reported"),
    SUMMARIZE("09_summarized");

    @Getter
    private final String stageDirectory;

    Stage(String stageDirectory) {
        this.stageDirectory = stageDirectory;
    }
}
