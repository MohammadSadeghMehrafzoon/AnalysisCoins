package ir.misterdeveloper.analysiscoins.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CoinsResponse {


    @SerializedName("Data")
    @Expose
    private ArrayList<CoinsData.Data> data;

    public ArrayList<CoinsData.Data> getData() {
        return data;
    }

    public void setData(ArrayList<CoinsData.Data> data) {
        this.data = data;
    }
}
