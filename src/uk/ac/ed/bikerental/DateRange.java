package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.BooleanSupplier;

// TODO: test DateRange

/** The type Date range. */
public class DateRange {
    private LocalDate start, end;

  /**
   * Instantiates a new Date range.
   *
   * @param start the start
   * @param end the end
   */
  public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

  /**
   * Gets start.
   *
   * @return the start
   */
  public LocalDate getStart() {
        return this.start;
    }

  /**
   * Gets end.
   *
   * @return the end
   */
  public LocalDate getEnd() {
        return this.end;
    }

  /**
   * To years long.
   *
   * @return the long
   */
  public long toYears() {
        return ChronoUnit.YEARS.between(this.getStart(), this.getEnd());
    }

  /**
   * To days long.
   *
   * @return the long
   */
  public long toDays() {
        return ChronoUnit.DAYS.between(this.getStart(), this.getEnd());
    }

  /**
   * Overlaps boolean.
   *
   * @param other the other
   * @return the boolean
   */
  public Boolean overlaps(DateRange other) {

    	if (start.isAfter(other.end) || (other.start.isAfter(end))) {
    		return false;
    	}
    	
        
      return true;  
        
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
