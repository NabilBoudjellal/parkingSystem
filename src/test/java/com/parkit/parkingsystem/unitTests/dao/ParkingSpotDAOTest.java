package com.parkit.parkingsystem.unitTests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ParkingSpotDAOTest {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testUpdateParkingCarInTableTest(){
        int actualAvailableCarSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingSpotDAO.updateParking(new ParkingSpot(1, ParkingType.CAR,false));
        int nextAvailableCarSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(actualAvailableCarSpot +1 , nextAvailableCarSpot);
    }

    @Test
    public void testUpdateParkingBikeInTableTest(){
        int actualAvailableBikeSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        parkingSpotDAO.updateParking(new ParkingSpot(4, ParkingType.BIKE,false));
        int nextAvailableBikerSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(actualAvailableBikeSpot + 1, nextAvailableBikerSpot);
    }
    @Test
    public void testWhenNoCarParkingSpotIsAvailable(){
        dataBasePrepareService.putAllParkingSpotsNotAvailable();
        int actualAvailableCareSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(0,actualAvailableCareSpot);
    }
    @Test
    public void testWhenNoBikeParkingSpotIsAvailable(){
        dataBasePrepareService.putAllParkingSpotsNotAvailable();
        int actualAvailableBikeSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(0,actualAvailableBikeSpot);
    }
}
