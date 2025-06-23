package model;

public class Unit {
    private int idUnit;
    private String unitName;

    public Unit() {
    }

    public Unit(int idUnit, String unitName) {
        this.idUnit = idUnit;
        this.unitName = unitName;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}

