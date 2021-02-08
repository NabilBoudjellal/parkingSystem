package com.parkit.parkingsystem.unitTests.modelTests;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

    private static final int id = 55;
    private static final ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,true);
    private static final String vehicleRegNumber = "ABCABC";
    private static final double price =15;
    private static final Date inTime = new Date();
    private static final Date outTime = new Date();
    private static Ticket ticket;

    @BeforeAll
    private static void setParkingSpot() {
        ticket = new Ticket();

        ticket.setId(id);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(price);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
    }
    @Test
    public void getIdTest(){
        assertEquals(ticket.getId(), id);
    }
    @Test
    public void getParkingSpotTest(){
        assertEquals(ticket.getParkingSpot(), parkingSpot);
    }
    @Test
    public void getVehicleRegNumberTest(){
        assertEquals(ticket.getVehicleRegNumber(), vehicleRegNumber);
    }
    @Test
    public void getPriceTest(){
        assertEquals(ticket.getPrice(), price);
    }
    @Test
    public void getInTimeTest(){
        assertEquals(ticket.getInTime(), inTime);
    }
    @Test
    public void getOutTimeTest(){
        assertEquals(ticket.getOutTime(), outTime);
    }
}
