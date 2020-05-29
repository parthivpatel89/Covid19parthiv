package com.parthiv.covid19;

import java.util.Objects;

public class CovidCases {
    private String Country;
    private int TotalConfirmed;
    private int TotalDeaths;
    private int TotalRecovered;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public int getTotalConfirmed() {
        return TotalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        TotalConfirmed = totalConfirmed;
    }

    public int getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public int getTotalRecovered() {
        return TotalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        TotalRecovered = totalRecovered;
    }

    @Override
    public String toString() {
        return "CovidCases{" +
                "Country='" + Country + '\'' +
                ", TotalConfirmed=" + TotalConfirmed +
                ", TotalDeaths=" + TotalDeaths +
                ", TotalRecovered=" + TotalRecovered +
                '}';
    }

    public CovidCases(String country, int totalConfirmed, int totalDeaths, int totalRecovered) {
        Country = country;
        TotalConfirmed = totalConfirmed;
        TotalDeaths = totalDeaths;
        TotalRecovered = totalRecovered;
    }
    public CovidCases() {

    }

    //    @Override
    public int compareToTotalConfirmed(Object o) {
        CovidCases f = (CovidCases) o;
        return this.TotalConfirmed - f.TotalConfirmed ;
    }
    public int compareToTotalDeaths(Object o) {
        CovidCases f = (CovidCases) o;
        return this.TotalDeaths - f.TotalDeaths ;
    }
    public int compareToTotalRecovered(Object o) {
        CovidCases f = (CovidCases) o;
        return this.TotalRecovered - f.TotalRecovered ;
    }
}
