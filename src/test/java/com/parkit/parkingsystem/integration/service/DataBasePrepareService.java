package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class DataBasePrepareService {

    final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("update parking set available = true").execute();

            //clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public int calculateTheNumberOfTicketSavedInTheDB(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            PreparedStatement ps = connection.prepareStatement("select count(*) from ticket");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt(1);
                else return 0;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
        return 0;
    }
    public boolean CheckIfThisSpotIsAvailable(int SpotId) {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement("select AVAILABLE from parking where PARKING_NUMBER = "+SpotId+" ");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) != 0;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
        return true;
    }
    public void putAllParkingSpotsNotAvailable(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to not available
            connection.prepareStatement("update parking set available = false").execute();
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

            connection.prepareStatement("insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(1,'"+regNumber+"',20,now(),now())").execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public void insertTestLineForCheckIfRegNumberIsInTheParking(String regNumber){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            connection.prepareStatement("insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(1,'"+regNumber+"',20,now(), null )").execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
    public double getFareToCheckIt(){
        Connection connection = null;
        double result=-1;
        try {
            connection = dataBaseTestConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(" select PRICE from ticket where id = 1 ");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            dataBaseTestConfig.closeResultSet(rs);
            dataBaseTestConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            System.out.println("error when getting fare");
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
        }catch (Exception ex){
            System.out.println("error when getting fare");
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
        return result;
    }
}
