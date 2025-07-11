package model;

public class Distributor {
    private int DistributorID;
    private String DistributorName;
    private String Address;
    private String DistributorEmail;
    private String DistributorPhone;

    public Distributor() {
    }

    public Distributor(int distributorID, String distributorName, String address, String distributorEmail, String distributorPhone) {
        DistributorID = distributorID;
        DistributorName = distributorName;
        Address = address;
        DistributorEmail = distributorEmail;
        DistributorPhone = distributorPhone;
    }

    public int getDistributorID() {
        return DistributorID;
    }

    public void setDistributorID(int distributorID) {
        DistributorID = distributorID;
    }

    public String getDistributorName() {
        return DistributorName;
    }

    public void setDistributorName(String distributorName) {
        DistributorName = distributorName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDistributorEmail() {
        return DistributorEmail;
    }

    public void setDistributorEmail(String distributorEmail) {
        DistributorEmail = distributorEmail;
    }

    public String getDistributorPhone() {
        return DistributorPhone;
    }

    public void setDistributorPhone(String distributorPhone) {
        DistributorPhone = distributorPhone;
    }
}

