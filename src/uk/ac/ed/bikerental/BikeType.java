package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BikeType {
	private static Map<String, BigDecimal> typesToReplacementMapValue = Stream.of(
			new AbstractMap.SimpleEntry<>("BIKE", BigDecimal.valueOf(900.0)))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	
	String type;
	
	public BikeType(String type) throws Exception {
		if (typesToReplacementMapValue.containsKey(type)) {
			this.type = type;
		} else {
			throw new Exception("NOT A TYPE");
		}
	}

    public BigDecimal getReplacementValue() {
        return typesToReplacementMapValue.get(type);
    }
}