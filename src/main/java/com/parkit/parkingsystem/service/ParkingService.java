package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/***
 * this class contains processes of entering and exciting from the parking
 */
public class ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private static final FareCalculatorService fareCalculatorService = new FareCalculatorService();

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    /***
     * this methode is dedicated to the process of entering a vehicle in the parking
     */
    public void processIncomingVehicle() {
        try{
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if(parkingSpot !=null && parkingSpot.getId() > 0){
                String vehicleRegNumber = getVehicleRegNumber();
                if(ticketDAO.checkIfRegNumberIsInTheParking(vehicleRegNumber))
                    System.out.println("A vehicle with the same RegNumber is already parked, you can't enter with this RegNumber");
                else {
                    parkingSpot.setAvailable(false);
                    parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false

                    Date inTime = new Date();
                    Ticket ticket = new Ticket();
                    //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                    //ticket.setId(ticketID);
                    ticket.setParkingSpot(parkingSpot);
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(0);
                    ticket.setInTime(inTime);
                    ticket.setOutTime(null);
                    ticketDAO.saveTicket(ticket);
                    System.out.println("Generated Ticket and saved in DB");
                    //test si client récurent
                    if (ticketDAO.checkIfRegularCustomer(vehicleRegNumber))
                        System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a " + Fare.PERCENTAGE_DISCOUNT_FOR_RECURRING_CUSTOMER * 100 + "% discount");
                    System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                    System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
                }
            }
        }catch(Exception e){
            logger.error("Unable to process incoming vehicle",e);
        }
    }

    /***
     * this methode call the input reader to have the registration number
     * @return a String of the registration number
     */
    private String getVehicleRegNumber() {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /***
     * this methode gets the next parking spot available
     * @return null object of parkingSpot if there is no parking spot available a
     * and if available ParkingSpot object
     */
    public ParkingSpot getNextParkingNumberIfAvailable(){
        int parkingNumber;
        ParkingSpot parkingSpot = null;
        try{
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if(parkingNumber > 0){
                parkingSpot = new ParkingSpot(parkingNumber,parkingType, true);
            }else{
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        }catch(IllegalArgumentException ie){
            logger.error("Error parsing user input for type of vehicle", ie);
        }catch(Exception e){
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /***
     * this methode displays the list of vehicle chooses and input the user chose
     * @return the car type of the user
     */
    private ParkingType getVehicleType(){
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch(input){
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /***
     * this methode is dedicated to the process of exiting a vehicle in the parking
     */
    public void processExitingVehicle() {
        try{
            String vehicleRegNumber = getVehicleRegNumber();
            if(ticketDAO.checkIfRegNumberIsInTheParking(vehicleRegNumber))
            {
                Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
                Date outTime = new Date();
                ticket.setOutTime(outTime);
                fareCalculatorService.calculateFare(ticket);
                if (ticketDAO.updateTicket(ticket)) {
                    ParkingSpot parkingSpot = ticket.getParkingSpot();
                    parkingSpot.setAvailable(true);
                    parkingSpotDAO.updateParking(parkingSpot);
                    System.out.println("Please pay the parking fare:" + ticket.getPrice());
                    System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
                } else {
                    System.out.println("Unable to update ticket information. Error occurred");
                }
            }else{
                System.out.println("There is no vehicle with this registration number is in the parking lot");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }
}
