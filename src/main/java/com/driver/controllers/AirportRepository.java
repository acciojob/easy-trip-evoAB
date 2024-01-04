package com.driver.controllers;


import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    HashMap<String , Airport> airportDB  = new HashMap<>();

    HashMap<Integer , Flight> flightDB  = new HashMap<>();

    HashMap<Integer , Passenger> passengerDB  = new HashMap<>();

    HashMap<Integer , List<Integer>> passengerInFlightDB  = new HashMap<>();
    public void addAirport(Airport a){
        airportDB.put(a.getAirportName() , a);
    }
    public String bookATicket(Integer fightId  , Integer passengerId){
        Flight f  = flightDB.get(fightId);
        int max  = f.getMaxCapacity();
        List<Integer> temp  = passengerInFlightDB.getOrDefault(fightId , new ArrayList<>());
        if(temp.size()>max) return "FAILURE";
        if(!temp.isEmpty()) {
            for (Integer i : temp) {
                if (Objects.equals(i, passengerId))
                    return "FAILURE";
            }
        }
        temp.add(passengerId);
        passengerInFlightDB.put(fightId , temp);
        return "SUCCESS";
    }
    public void addPassenger(Passenger p){
        passengerDB.put(p.getPassengerId() , p);
    }
    public String getLargestAirportName(){
        int max  = 0;
        List<String> al  = new ArrayList<>();
        for(String s: airportDB.keySet()){
            Airport temp  = airportDB.get(s);
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
            if(s.compareToIgnoreCase(pans)<0)
                pans=s;
        }
        return pans;
    }

    public void addFlight(Flight f){
        flightDB.put(f.getFlightId() , f);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City from ,City to){
        double ans = Double.MAX_VALUE;
        for(Integer i : flightDB.keySet()){
            Flight f  = flightDB.get(i);
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
        for(String a : airportDB.keySet()){
            if(a.equals(airPortName)){
                Airport airport  = airportDB.get(a);
                City city  = airport.getCity();
                for(Integer i : passengerInFlightDB.keySet()){
                    Flight f = flightDB.get(i);
                    if(f.getToCity()==city||f.getFromCity()==city){
                        if(date.compareTo(f.getFlightDate())==0) {
                            List<Integer> al  = passengerInFlightDB.get(i);
                            pans += al.size();
                        }
                    }
                }
            }
        }
        return pans;
    }

    public int calculateFlightFare(Integer flightId){
        List<Integer> temp  = passengerInFlightDB.get(flightId);
        int n  = temp.size();
        return 3000+n*50;
    }

    public String cancelATicket(Integer flghtId , Integer pId){
        if(!flightDB.containsKey(flghtId)) return "FAILURE";
        List<Integer> temp  = passengerInFlightDB.getOrDefault(flghtId , new ArrayList<>());
        int s  = -1;
        if(!temp.isEmpty()) {
            for (Integer i : temp) {
                if (Objects.equals(i, pId)) {
                    s = pId;
                    break;
                }
            }
            if (s == -1) return "FAILURE";
        }
        else{
            passengerInFlightDB.put(flghtId , temp);
        }
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer pId){
        int count  = 0;
        for(Integer f : passengerInFlightDB.keySet()){
            List<Integer> temp = passengerInFlightDB.get(f);
            for(Integer i : temp){
                if(Objects.equals(i, pId))
                    count++;
            }
        }
        return count;
    }

    public String getAirportNameFromFlightId(Integer flghtId){
        if(!flightDB.containsKey(flghtId)) return null;

        Flight flight  = flightDB.get(flghtId);
        City city  = flight.getFromCity();
        for(String name : airportDB.keySet()){
            Airport a  = airportDB.get(name);
            City acity  = a.getCity();
            if(acity==city)
                return name;
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flghtId){
        int pans  = 0;
        List<Integer> temp  = passengerInFlightDB.get(flghtId);
        if(temp.isEmpty()){
            return 0;
        }
        for(int i = 0;i<temp.size();i++){
            pans  += 3000+i*50;
        }
        return pans;
    }

}