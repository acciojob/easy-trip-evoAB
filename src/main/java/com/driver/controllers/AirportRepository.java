package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Repository
public class AirportRepository {
    HashMap<String, Airport> airportDB = new HashMap<>();
    HashMap<Integer, Flight> flightDB = new HashMap<>();
    HashMap<Integer, Passenger> passengerDB = new HashMap<>();
    HashMap<Integer, List<Integer>> passengerFlightDB = new HashMap<>();
    public void addAirport(Airport airport){
        airportDB.put(airport.getAirportName(),airport);
    }
    public String getLargestAirportName(){
        String ans="";
        int terminals=0;
        for(Airport airport:airportDB.values()){
            if(airport.getNoOfTerminals()>terminals){
                ans=airport.getAirportName();
                terminals=airport.getNoOfTerminals();
            } else if (airport.getNoOfTerminals()==terminals) {
                if (airport.getAirportName().compareTo(ans)<0){
                    ans=airport.getAirportName();
                }
            }
        }
        return ans;
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        double minTime = Double.MAX_VALUE;
        for (Flight flight:flightDB.values()){
            if (flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity))
                minTime=Math.min(minTime, flight.getDuration());
        }
        if (minTime!=Double.MAX_VALUE)
            return minTime;
        return -1;
    }
    public int getNumberOfPeopleOn(Date date, String airportName){
        if(!airportDB.containsKey(airportName))
            return 0;
        Airport airport = airportDB.get(airportName);
        City city=airport.getCity();
        int count=0;
        for (Flight flight:flightDB.values()){
            if(date.equals(flight.getFlightDate()))
                if (flight.getToCity().equals(city) || flight.getFromCity().equals(city))
                    count+=(passengerFlightDB.get(flight.getFlightId()).size());
        }
        return count;
    }
    public int calculateFlightFare(int flightId){
        return 3000 + passengerFlightDB.get(flightId).size()*50;
    }
    public int bookATicket(int flightId, int passengerId){
//        if(!passengerFlightDB.containsKey(flightId))
//            return -1;
//        if (!passengerFlightDB.get(flightId).contains(passengerId))
//            return -1;
//        if(flightDB.get(flightId).getMaxCapacity()<=passengerFlightDB.get(flightId).size())
//            return -1;
//
//        List<Integer> passengers = passengerFlightDB.get(flightId);
//        if(passengers==null)
//            passengers = new ArrayList<>();
//        passengers.add(passengerId);
//        passengerFlightDB.put(flightId,passengers);
//        return 1;
        if(Objects.nonNull(passengerFlightDB.get(flightId)) &&(passengerFlightDB.get(flightId).size()<flightDB.get(flightId).getMaxCapacity())){


            List<Integer> passengers =  passengerFlightDB.get(flightId);

            if(passengers.contains(passengerId)){
                return -1;
            }

            passengers.add(passengerId);
            passengerFlightDB.put(flightId,passengers);
            return 1;
        }
        else if(Objects.isNull(passengerFlightDB.get(flightId))){
            passengerFlightDB.put(flightId,new ArrayList<>());
            List<Integer> passengers =  passengerFlightDB.get(flightId);

            if(passengers.contains(passengerId)){
                return -1;
            }

            passengers.add(passengerId);
            passengerFlightDB.put(flightId,passengers);
            return 1;

        }
        return -1;
    }
    public boolean cancelATicket(int flightId, int passangerId){
        if(!passengerFlightDB.containsKey(flightId))
            return false;
        if (!passengerFlightDB.get(flightId).contains(passangerId))
            return false;
        List<Integer> passengers = passengerFlightDB.get(flightId);
        passengers.remove(passangerId);
        return true;
    }
    public int countOfBookingsDoneByPassengerAllCombined(int passangerId){
        int count=0;
        for (List <Integer> passangers : passengerFlightDB.values()){
            for (int passanger : passangers)
                if(passangerId==passanger)
                    count++;
        }
        return count;
    }
    public void addFlight(Flight flight){
        flightDB.put(flight.getFlightId(), flight);
    }
    public String getAirportNameFromFlightId(Integer flightId) {
        if(flightDB.containsKey(flightId)){
            City city = flightDB.get(flightId).getFromCity();
            for (Airport airport : airportDB.values()){
                if(city.equals(airport.getCity()))
                    return airport.getAirportName();
            }
        }
        return null;
    }
    public int calculateRevenueOfAFlight(int flightId){

        int noOfPeopleBooked = passengerFlightDB.get(flightId).size();
        int variableFare = (noOfPeopleBooked*(noOfPeopleBooked+1))*25;
        int fixedFare = 3000*noOfPeopleBooked;
        int totalFare = variableFare + fixedFare;

        return totalFare;
    }
    public void addPassenger(Passenger passenger){
        passengerDB.put(passenger.getPassengerId(),passenger);
    }
}
