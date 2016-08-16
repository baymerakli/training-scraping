import model.City;

public class Main {

    public static void main(String[] args) {

//        CountryFiller.getCountries();
        PrayTimeFiller.startFilling();

        City city = new City();
        city.setId("9366");
        city.setName("YAPRAKLI");
        PrayTimeFiller.getCityWithPrayTimes("2","522",city);
    }

}
