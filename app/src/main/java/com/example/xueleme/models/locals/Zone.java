package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.ZoneDetail;

public class Zone {
    public Integer id;
    public String zoneName;
    public static Zone fromDetail(ZoneDetail detail) {
        Zone zone = new Zone();
        zone.id = detail.id;
        zone.zoneName = detail.zoneName;
        return zone;
    }
}
