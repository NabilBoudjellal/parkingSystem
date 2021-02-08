package com.parkit.parkingsystem.unitTests.modelTests;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingSpotTest {

    private static final int id =55;
    private static final ParkingType parkingType = ParkingType.CAR;
    private static final boolean isAvailable = true;
    private static ParkingSpot parkingSpot;

    @BeforeAll
    private static void setParkingSpot() {
        parkingSpot = new ParkingSpot();
        parkingSpot.setId(id);
        parkingSpot.setParkingType(parkingType);
        parkingSpot.setAvailable(isAvailable);
    }
    @Test
    public void getIdTest(){
       assertEquals(parkingSpot.getId(), id);
    }
    @Test
    public void getParkingTypeTest(){
        assertEquals(parkingSpot.getParkingType(), parkingType);
    }
    @Test
    public void getIsAvailableTest(){
        assertEquals(parkingSpot.isAvailable(), isAvailable);
    }
}
