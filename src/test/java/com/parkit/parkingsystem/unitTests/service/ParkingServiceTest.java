package com.parkit.parkingsystem.unitTests.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;
/***
 * this test class contains the units tests of the class ParkingService
 */
@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {


    private ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    /***
     * this test insure that the process of incoming vehicle for a regular customer in correctly work
     * when the data information are correct
     */
    @Test
    public void regularCustomerIncomingVehicleTest() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AC-25-BB");
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
            when(ticketDAO.checkIfRegularCustomer(anyString())).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }
    /***
     * this test insure that the process of incoming vehicle for a non regular customer in correctly work
     * when the data information are correct
     */
    @Test
    public void nonRegularCustomerIncomingVehicleTest() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AC-25-BB");
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
            when(ticketDAO.checkIfRegularCustomer(anyString())).thenReturn(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }
    /***
     * this test insure that the process of exciting vehicle works correctly when the data information are correct
     */
    @Test
    public void processExitingVehicleTest(){
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.checkIfRegNumberIsInTheParking(anyString())).thenReturn(true);
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }
    /***
     * this test check that there is no interaction with the parkingSpotDAO when the process of exciting fails because
     * there is data information incorrect
     */
    @Test
    public void failInUpdatingTicket(){
        ParkingService parkingService;
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
            when(ticketDAO.checkIfRegNumberIsInTheParking(anyString())).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processExitingVehicle();
        verifyZeroInteractions(parkingSpotDAO);
    }
    /***
     * this test check that
     * we cannot get a vehicle out if it is not in the parking lot
     */
    @Test
    public void tryToGetAVehicleOutThatIsNotInTheParkingLot(){
        ParkingService parkingService;
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.checkIfRegNumberIsInTheParking(anyString())).thenReturn(false);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

        parkingService.processExitingVehicle();

        verifyZeroInteractions(parkingSpotDAO);
    }
    /***
     * this test check that it is not possible to park a vehicle in the parking and a vehicle is already parked with the same vehicle registration number
     */
    @Test
    public void vehicleEntryAlreadyInTheParking(){
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AC-25-BB");
            when(ticketDAO.checkIfRegNumberIsInTheParking(anyString())).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, never()).saveTicket(any(Ticket.class));

    }
}

