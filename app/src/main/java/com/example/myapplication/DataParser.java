package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String,String> getSingleNearbyPlace(JSONObject googlePlaceJson){
        HashMap<String,String> googlePlaceMap= new HashMap<>();
        String NameOfPlace="-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        String reference="";
        try {
            if (! googlePlaceJson.isNull("name")){
                NameOfPlace= googlePlaceJson.getString("name");
            }
            if (! googlePlaceJson.isNull("vicinity")){
                vicinity= googlePlaceJson.getString("vicinity");
            }
            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference= googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name",NameOfPlace);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return  googlePlaceMap;


    }
    private List<HashMap<String,String>> getAllNearbyPlaces (JSONArray jsonArray){
        int counter= jsonArray.length();
        List<HashMap<String,String>> NearbyPlacesList = new ArrayList<>();
        HashMap<String,String> NearbyPlaceMap =null;
        for (int i=0 ; i<counter;i++){
            try {
                NearbyPlaceMap =getSingleNearbyPlace((JSONObject) jsonArray.get(i));
                NearbyPlacesList.add(NearbyPlaceMap);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        return NearbyPlacesList;
    }

    public List<HashMap<String,String>> parse(String jSONdata){
        JSONArray jsonArray=null;
        JSONObject jsonObject;

        try {
            jsonObject=new JSONObject(jSONdata);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return getAllNearbyPlaces(jsonArray);
    }
}
