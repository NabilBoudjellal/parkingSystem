package com.parkit.parkingsystem.unitTests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();

    }
    @Test
    public void saveUpdateParkingTest(){
        Ticket ticket= new Ticket();
        ParkingSpot parkingSpot= new ParkingSpot();
        parkingSpot.setAvailable(false);
        parkingSpot.setParkingType(ParkingType.CAR);
        parkingSpot.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABC");
        ticket.setPrice(99);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
        ticketDAO.saveTicket(ticket);
        Ticket ticket2 = ticketDAO.getTicket("ABC");
        assertEquals(ticket.getVehicleRegNumber(),ticket2.getVehicleRegNumber());
    }
    @Test
    public void updateTicketTrueTest(){
        Ticket ticket= new Ticket();
        ParkingSpot parkingSpot= new ParkingSpot();
        parkingSpot.setAvailable(false);
        parkingSpot.setParkingType(ParkingType.CAR);
        parkingSpot.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABC");
        ticket.setPrice(0);
        ticket.setInTime(new Date());

        ticketDAO.saveTicket(ticket);

        Ticket ticket2 = new Ticket();
        ticket2.setPrice(1000);
        ticket2.setOutTime(new Date());
        ticket2.setId(1);
        boolean response = ticketDAO.updateTicket(ticket2);

        assertTrue(response);
    }
    @Test
    public void updateTicketFalseTest(){
        Ticket ticket2 = new Ticket();
        ticket2.setPrice(1000);
        ticket2.setOutTime(new Date());
        ticket2.setId(1);
        boolean response = ticketDAO.updateTicket(ticket2);

        assertFalse(response);
    }
    @Test
    public void CheckIfRegularCustomerTrueTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegularCustomer(regNumber);
        boolean resultCheck = ticketDAO.CheckIfRegularCustomer(regNumber);
        assertTrue(resultCheck);
    }
    @Test
    public void CheckIfRegularCustomerFalseTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegularCustomer(regNumber);
        boolean resultCheck = ticketDAO.CheckIfRegularCustomer("other String");
        assertFalse(resultCheck);
    }
    @Test
    public void CheckIfRegNumberIsInTheParkingTrueTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegNumberIsInTheParking(regNumber);
        boolean resultCheck = ticketDAO.CheckIfRegNumberIsInTheParking(regNumber);
        assertTrue(resultCheck);
    }
    @Test
    public void CheckIfRegNumberIsInTheParkingFalseTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegNumberIsInTheParking(regNumber);
        boolean resultCheck = ticketDAO.CheckIfRegNumberIsInTheParking("other String");
        assertFalse(resultCheck);
    }
}
