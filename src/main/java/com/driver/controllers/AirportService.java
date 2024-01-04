package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Service
public class AirportService {
    @Autowired
    AirportRepository airportRepository;

    public void addAirport(Airport airport){
        airportRepository.addAirport(airport);
    }
    public String getLargestAirportName(){
        return airportRepository.getLargestAirportName();
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        return airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity, toCity);
    }
    public int getNumberOfPeopleOn(Date date, String airportName){
        return airportRepository.getNumberOfPeopleOn(date, airportName);
    }
    public int calculateFlightFare(int flightId){
        return airportRepository.calculateFlightFare(flightId);
    }
    public String bookATicket(int flightId, int passangerId){
        return airportRepository.bookATicket(flightId, passangerId);
    }
    public String cancelATicket(int flightId, int passangerId){
        return airportRepository.cancelATicket(flightId, passangerId);
    }
    public int countOfBookingsDoneByPassengerAllCombined(int passangerId){
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passangerId);
    }
    public void addFlight(Flight flight){
        airportRepository.addFlight(flight);
    }
    public String getAirportNameFromFlightId(int flightId){
        return airportRepository.getAirportNameFromFlightId(flightId);
    }
    public int calculateRevenueOfAFlight(int flightId){
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }

    public void addPassenger(Passenger passenger){
        airportRepository.addPassenger(passenger);
    }
}
