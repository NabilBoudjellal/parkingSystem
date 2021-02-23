package com.parkit.parkingsystem.integration.daoIT;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.config.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
/***
 * This test class contain TicketDAOTest tests
 */
public class TicketDaoIT {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    /***
     * this methode setup the objects we will use before all processing tests
     */
    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    /***
     * this methode cleans the test DB before each test
     */
    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();

    }

    /***
     * this test methode check if the DAO tu update the parking table is working properly.
     */
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
    /***
     * this test methode update a ticket in the BD and check if the DB was updated correctly.
     */
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
    /***
     * this test methode update a ticket with wrong information and insure that the ticket is not updated.
     */
    @Test
    public void updateTicketFalseTest(){
        Ticket ticket2 = new Ticket();
        ticket2.setPrice(1000);
        ticket2.setOutTime(new Date());
        ticket2.setId(1);
        boolean response = ticketDAO.updateTicket(ticket2);

        assertFalse(response);
    }
    /***
     * the test methode update the DB with a test line and check if the methode CheckIfRegularCustomer return true
     */
    @Test
    public void CheckIfRegularCustomerTrueTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegularCustomer(regNumber);
        boolean resultCheck = ticketDAO.checkIfRegularCustomer(regNumber);
        assertTrue(resultCheck);
    }
    /***
     * the test methode update the DB with a test line and check if the methode CheckIfRegularCustomer return false
     */
    @Test
    public void CheckIfRegularCustomerFalseTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegularCustomer(regNumber);
        boolean resultCheck = ticketDAO.checkIfRegularCustomer("other String");
        assertFalse(resultCheck);
    }
    /***
     * the test methode update the DB with a test line and check and test if the methode CheckIfRegNumberIsInTheParking is working correctly and returns
     * true there is vehicle already parked in the parking
     */
    @Test
    public void CheckIfRegNumberIsInTheParkingTrueTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegNumberIsInTheParking(regNumber);
        boolean resultCheck = ticketDAO.checkIfRegNumberIsInTheParking(regNumber);
        assertTrue(resultCheck);
    }
    /***
     * the test methode update the DB with a test line and check and test if the methode CheckIfRegNumberIsInTheParking returns false
     * when there is no vehicle parked with this regNumber
     */
    @Test
    public void CheckIfRegNumberIsInTheParkingFalseTest(){
        String regNumber = "ABC";
        dataBasePrepareService.insertTestLineForCheckIfRegNumberIsInTheParking(regNumber);
        boolean resultCheck = ticketDAO.checkIfRegNumberIsInTheParking("other String");
        assertFalse(resultCheck);
    }
}
