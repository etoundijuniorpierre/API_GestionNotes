package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CacheStatsResponse {
    private String cacheName;
    private Long size;
    private Long hitCount;
    private Long missCount;
    private Double hitRate;
    private Long evictionCount;
    private Double averageLoadTime;
}
