package model;

public class Distributor {
    private int idDistributor;
    private String distributorName;
    private String address;
    private String phone;

    public Distributor() {
    }

    public Distributor(int idDistributor, String distributorName, String address, String phone) {
        this.idDistributor = idDistributor;
        this.distributorName = distributorName;
        this.address = address;
        this.phone = phone;
    }

    public int getIdDistributor() {
        return idDistributor;
    }

    public void setIdDistributor(int idDistributor) {
        this.idDistributor = idDistributor;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

