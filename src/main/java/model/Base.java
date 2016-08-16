package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Base {

    private String id;
    private String name;

    private Map<String, PrayTime> prayTimeMap = new LinkedHashMap<String, PrayTime>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, PrayTime> getPrayTimeMap() {
        return prayTimeMap;
    }

    public void setPrayTimeMap(Map<String, PrayTime> prayTimeMap) {
        this.prayTimeMap = prayTimeMap;
    }

}
