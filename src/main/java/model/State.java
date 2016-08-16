package model;

import java.util.ArrayList;
import java.util.List;

public class State extends Base {

    private List<City> cityList = new ArrayList<>();

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
