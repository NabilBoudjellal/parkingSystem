package com.parkit.parkingsystem.integration.daoIT;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.config.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * This test class contain ParkingSpotDAO tests
 */
public class ParkingSpotDaoIT {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    /***
     * this methode setup the objects we will use before all processing tests
     */
    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
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
     * this test insures that the updating of the DB using the methode updateParking is called for car vehicle type
     * and a spot is available for update is working fine
     */
    @Test
    public void updateParkingCarInTableTest(){
        int actualAvailableCarSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingSpotDAO.updateParking(new ParkingSpot(1, ParkingType.CAR,false));
        int nextAvailableCarSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(actualAvailableCarSpot +1 , nextAvailableCarSpot);
    }
    /***
     * this test insures that the updating of the DB using the methode updateParking is called for bike vehicle type
     * and a spot is available for update is working fine
     */
    @Test
    public void updateParkingBikeInTableTest(){
        int actualAvailableBikeSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        parkingSpotDAO.updateParking(new ParkingSpot(4, ParkingType.BIKE,false));
        int nextAvailableBikerSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(actualAvailableBikeSpot + 1, nextAvailableBikerSpot);
    }
    /***
     * this test insures that the when no car spot is available for update the methode getNextAvailableSlot returns 0
     */
    @Test
    public void test_When_NoCarParkingSpot_IsAvailable(){
        dataBasePrepareService.putAllParkingSpotsNotAvailable();
        int actualAvailableCareSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(0,actualAvailableCareSpot);
    }
    /***
     * this test insures that the when no bike spot is available for update the methode getNextAvailableSlot returns 0
     */
    @Test
    public void test_When_NoBikeParkingSpot_IsAvailable(){
        dataBasePrepareService.putAllParkingSpotsNotAvailable();
        int actualAvailableBikeSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(0,actualAvailableBikeSpot);
    }
}
