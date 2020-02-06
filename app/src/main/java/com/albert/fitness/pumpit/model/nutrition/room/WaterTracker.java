package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "water_tracker_table")
public class WaterTracker {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tracker_id")
    private int trackerId;

    private String date;

    @ColumnInfo(name = "water_qty")
    private String waterQty;

    public WaterTracker() {
    }

    public WaterTracker(String date, String waterQty) {
        this.date = date;
        this.waterQty = waterQty;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWaterQty() {
        return waterQty;
    }

    public void setWaterQty(String waterQty) {
        this.waterQty = waterQty;
    }
}
