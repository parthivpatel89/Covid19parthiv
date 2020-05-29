package com.parthiv.covid19;

import java.util.ArrayList;

public class Covid {
    private ArrayList<CovidCases> covid;

    public ArrayList<CovidCases> getCovid() {
        return covid;
    }

    public void setCovid(ArrayList<CovidCases> covid) {
        this.covid = covid;
    }

    public Covid(ArrayList<CovidCases> covid) {
        this.covid = covid;
    }

    @Override
    public String toString() {
        return "Covid{" +
                "covid=" + covid +
                '}';
    }
}
