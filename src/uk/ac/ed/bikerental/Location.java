package uk.ac.ed.bikerental;

/** <h2>The Location type.</h2>
 * <p>Utility class use to represent the location.
 * It uses the address and the postcode in its representation</p>
 * @author Konrad & Linda
 */
public class Location {

    /**
     *  Postcode associated to the location
     */
    private String postcode;

    /**
     *  Address of the location
     */
    private String address;

  /**
   * Instantiates a new Location.
   *
   * @param postcode the postcode, must be at least 6 characters long
   * @param address the address
   * @throws AssertionError if postcode length is less than 6
   */
  public Location(String postcode, String address) {
        assert postcode.length() >= 6;
        this.postcode = postcode;
        this.address = address;
    }

  /**
   * Checks if both location are in the same postal area.
   * They are if first two characters of their postcodes are the same.
   *
   * @param other the other Location object
   * @return true if in the same postal area
   */
  public boolean isNearTo(Location other) {
        return postcode.substring(0, 2).equals(other.postcode.substring(0, 2));
    }

  /**
   * Gets postcode.
   *
   * @return the postcode
   */
  public String getPostcode() {
        return postcode;
    }

  /**
   * Gets address.
   *
   * @return the address
   */
  public String getAddress() {
        return address;
    }
}
