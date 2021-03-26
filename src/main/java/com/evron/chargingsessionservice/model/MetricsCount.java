package com.evron.chargingsessionservice.model;

import lombok.Data;

/**
 * Model class for MetricsCount.
 */
@Data
public class MetricsCount {

    private int totalCount;
    private int startedCount;
    private int stoppedCount;

}
