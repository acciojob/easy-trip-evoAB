package com.driver.controllers;


import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class AirportRepository {

    HashMap<String , Airport> aDb  = new HashMap<>();

    HashMap<Integer , Flight> fDb  = new HashMap<>();

    HashMap<Integer , Passenger> pDb  = new HashMap<>();

    HashMap<Integer , List<Integer>> tDb  = new HashMap<>();
    public void addAirport(Airport a){
        aDb.put(a.getAirportName() , a);
    }
    public String bookATicket(Integer fightId  , Integer passengerId){
        Flight f  = fDb.get(fightId);
        int max  = f.getMaxCapacity();
        List<Integer> temp  = tDb.getOrDefault(fightId , new ArrayList<>());
        if(temp.size()>max) return "FAILURE";
        if(temp.size()>0) {
            for (Integer i : temp) {
                if (i == passengerId)
                    return "FAILURE";
            }
        }
        temp.add(passengerId);
        tDb.put(fightId , temp);
        return "SUCCESS";
    }
    public void addPassenger(Passenger p){
        pDb.put(p.getPassengerId() , p);
    }
    public String getLargestAirportName(){
        int max  = 0;
        List<String> al  = new ArrayList<>();
        for(String s: aDb.keySet()){
            Airport temp  = aDb.get(s);
            int terminal  = temp.getNoOfTerminals();
            if(max<terminal){
                max  = terminal;
                al.add(temp.getAirportName());
            }
            else if(max==terminal){
                al.add(temp.getAirportName());
            }
        }
        return al.size()==1?al.get(0):lexographically(al);
    }

    public String lexographically(List<String>list)
    {
        String pans=list.get(0);
        for(String s:list){
            if(s.compareToIgnoreCase(pans)<0)pans=s;
        }
        return pans;
    }

    public String addFlight(Flight f){
        fDb.put(f.getFlightId() , f);
        return "SUCCESS";
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City from ,City to){
        double ans = Double.MAX_VALUE;
        for(Integer i : fDb.keySet()){
            Flight f  = fDb.get(i);
            double time  = f.getDuration();
            City fromcity = f.getFromCity();
            City toCity  = f.getToCity();
            if(fromcity==from && toCity==to){
                if(ans>time)
                    ans  = time;
            }
        }
        return ans==Double.MAX_VALUE?-1:ans;
    }

    public int getNumberOfPeopleOn(Date date , String airPortName){
        int pans  = 0;
        for(String a : aDb.keySet()){
            if(a.equals(airPortName)){
                Airport airport  = aDb.get(a);
                City city  = airport.getCity();
                for(Integer i : tDb.keySet()){
                    Flight f = fDb.get(i);
                    if(f.getToCity()==city||f.getFromCity()==city){
                        if(date.compareTo(f.getFlightDate())==0) {
                            List<Integer> al  = tDb.get(i);
                            pans += al.size();
                        }
                    }
                }
            }
        }
        return pans;
    }

    public int calculateFlightFare(Integer flightId){
        List<Integer> temp  = tDb.get(flightId);
        int n  = temp.size();
        return 3000+n*50;
    }

    public String cancelATicket(Integer fId , Integer pId){
        if(!fDb.containsKey(fId)) return "FAILURE";
        List<Integer> temp  = tDb.getOrDefault(fId , new ArrayList<>());
        int s  = -1;
        if(temp.size()>0) {
            for (Integer i : temp) {
                if (i == pId) {
                    s = pId;
                    break;
                }
            }
            if (s == -1) return "FAILURE";
        }
        else{
            temp.remove(pId);
            tDb.put(fId , temp);
        }
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer pId){
        int count  = 0;
        for(Integer f : tDb.keySet()){
            List<Integer> temp = tDb.get(f);
            for(Integer i : temp){
                if(i==pId)
                    count++;
            }
        }
        return count;
    }

    public String getAirportNameFromFlightId(Integer fId){
        if(!fDb.containsKey(fId)) return null;

        Flight flight  = fDb.get(fId);
        City city  = flight.getFromCity();
        for(String name : aDb.keySet()){
            Airport a  = aDb.get(name);
            City acity  = a.getCity();
            if(acity==city)
                return name;
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer fId){
        int pans  = 0;
        List<Integer> temp  = tDb.get(fId);
        if(temp.size()==0){
            return 0;
        }
        for(int i = 0;i<temp.size();i++){
            pans  += 3000+i*50;
        }
        return pans;
    }

}