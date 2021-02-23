package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/***
 * this class contains the databases calls used to communicate with the test DB
 */
public class DataBasePrepareService {

    final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            ps = connection.prepareStatement("update parking set available = true");
            ps.execute();
            //clear ticket entries;
            ps2 =connection.prepareStatement("truncate table ticket");
            ps2.execute();
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closePreparedStatement(ps2);
            dataBaseTestConfig.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public int calculateTheNumberOfTicketSavedInTheDB(){
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            ps = connection.prepareStatement("select count(*) from ticket");
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return rs.getInt(1);
                else return 0;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closeConnection(connection);
        }
        return 0;
    }
    public boolean checkIfThisSpotIsAvailable(int SpotId) {
        Connection connection = null;
        Boolean result = true;
        try {
            connection = dataBaseTestConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement("select AVAILABLE from parking where PARKING_NUMBER = "+SpotId+" ");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getInt(1) != 0;
            }
            dataBaseTestConfig.closeResultSet(rs);
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closeConnection(connection);
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
        return result;
    }
    public void putAllParkingSpotsNotAvailable(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to not available
            PreparedStatement ps =connection.prepareStatement("update parking set available = false");
            ps.execute();
            dataBaseTestConfig.closePreparedStatement(ps);
            //connection.prepareStatement("update parking set available = false").execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public void insertTestLineForCheckIfRegularCustomer(String regNumber){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement("insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(1, ? ,20,now(),now())");
            ps.setString(1, regNumber);
            ps.execute();
            dataBaseTestConfig.closePreparedStatement(ps);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public void insertTestLineForCheckIfRegNumberIsInTheParking(String regNumber){
        Connection connection = null;
        PreparedStatement ps =null;
        try{
            connection = dataBaseTestConfig.getConnection();
            ps = connection.prepareStatement("insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(1, ? ,20,now(), null )");
            ps.setString(1, regNumber);
            ps.execute();
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public double getFareToCheckIt(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double result=-1;
        try {
            connection = dataBaseTestConfig.getConnection();
            ps = connection.prepareStatement(" select PRICE from ticket where id = 1 ");
            rs = ps.executeQuery();
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            dataBaseTestConfig.closeResultSet(rs);
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
        return result;
    }
    public Timestamp getOutDateToCheckIt(){
        Connection connection = null;
        Timestamp result= null;
        try {
            connection = dataBaseTestConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(" select OUT_TIME from ticket where id = 1 ");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                result = rs.getTimestamp(1);
            }
            dataBaseTestConfig.closeResultSet(rs);
            dataBaseTestConfig.closePreparedStatement(ps);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
        return result;
    }
}
