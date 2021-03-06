package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/***
 * This class is dedicated to calculating the fares
 * @author Nabil Boudjellal
 */
public class FareCalculatorService {

    private final TicketDAO ticketDAO = new TicketDAO();


    /***
     *  This method calculates the fares and update database
     * if everything is ok
     * @param ticket the ticket we want to calculate his fares and update it
     */
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime() +" et le in time "+ ticket.getInTime());
        }

        LocalDateTime inDateTime = ticket.getInTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime outDateTime = ticket.getOutTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Duration duration = Duration.between( inDateTime, outDateTime);

        double durationInMinutes = duration.toMinutes();

        double timeToBePaid;

        if(durationInMinutes > Fare.FREE_RATE_MINUTES) {
            timeToBePaid = durationInMinutes;
        }else
        {
            timeToBePaid =0;
        }
        if(testingIfTheDiscountsApplicable(ticket.getVehicleRegNumber(),timeToBePaid)){
            System.out.println("As a recurring user of our parking lot, you'll benefit from a "+ Fare.PERCENTAGE_DISCOUNT_FOR_RECURRING_CUSTOMER * 100 +"% discount");
        timeToBePaid = timeToBePaid * (1 - Fare.PERCENTAGE_DISCOUNT_FOR_RECURRING_CUSTOMER);
    }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice((timeToBePaid * Fare.CAR_RATE_PER_HOUR)/60);
                break;
            }
            case BIKE: {
                ticket.setPrice((timeToBePaid * Fare.BIKE_RATE_PER_HOUR)/60);
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }

    /***
     * this method checks in the database if a discount is applicable for
     * the owner of the RegNumber
     * @param VehicleRegNumber the registration number of the vehicle
     * @param timeToBePaid the time that will be used to calculate the fees
     * @return true if a discount is applicable and false if not
     */
    public boolean testingIfTheDiscountsApplicable(String VehicleRegNumber, double timeToBePaid){
        return ticketDAO.checkIfRegularCustomer(VehicleRegNumber) && timeToBePaid != 0;
    }
}