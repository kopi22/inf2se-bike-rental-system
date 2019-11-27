package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/** <h2>The DateRange type.</h2>
 * <p>Utility class used to represent DateRange.
 * It uses LocalDate Java class.</p>
 * @author Konrad & Linda
 */
public class DateRange {
    private final LocalDate start;
    private final LocalDate end;

  /**
   * Instantiates a new Date range.
   *
   * @param start the start of specified date range
   * @param end the end of specified date range
   */
  public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

  /**
   * Gets start of date interval.
   *
   * @return the start
   */
  public LocalDate getStart() {
        return this.start;
    }

  /**
   * Gets end of date interval.
   *
   * @return the end
   */
  public LocalDate getEnd() {
        return this.end;
    }

  /**
   * Converts length of interval to years.
   *
   * @return the long
   */
  public long toYears() {
        return ChronoUnit.YEARS.between(this.getStart(), this.getEnd());
    }

  /**
   * Converts length of interval to days.
   *
   * @return the long
   */
  public long toDays() {
        return ChronoUnit.DAYS.between(this.getStart(), this.getEnd());
    }

  /**
   * Checks whether two date ranges overlap.
   *
   * @param other the other 
   * @return the boolean
   */
  public Boolean overlaps(DateRange other) {

      return !start.isAfter(other.end) && (!other.start.isAfter(end));

  }

    @Override
    public int hashCode() {
        // hashCode method allowing use in collections
        return Objects.hash(end, start);
    }

    @Override
    public boolean equals(Object obj) {
        // equals method for testing equality in tests
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DateRange other = (DateRange) obj;
        return Objects.equals(end, other.end) && Objects.equals(start, other.start);
    }
    
    // You can add your own methods here
}
