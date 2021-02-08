package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        if(inTime != null)
            return new Date(inTime.getTime());
        return null;
    }

    public void setInTime(Date inTime) {
        if(inTime != null) {
        Date tempInTime = new Date(inTime.getTime());
        this.inTime = tempInTime;
        } else this.inTime = null;
    }

    public Date getOutTime() {
        if(outTime != null)
            return new Date(outTime.getTime());
        return null;
    }

    public void setOutTime(Date outTime) {
        if(outTime != null) {
            Date tempOutTime = new Date(outTime.getTime());
            this.outTime = tempOutTime;
        } else this.outTime = null;
    }
}
