package uk.ac.ed.bikerental;

public class Location {
    private String postcode;
    private String address;
    
    public Location(String postcode, String address) {
        assert postcode.length() >= 6;
        this.postcode = postcode;
        this.address = address;
    }
    
    public boolean isNearTo(Location other) {
        return postcode.substring(0, 2).equals(other.postcode.substring(0, 2));
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAddress() {
        return address;
    }
}
