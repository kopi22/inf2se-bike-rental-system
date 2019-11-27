package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BikeType {
	private static Map<String, BigDecimal> typesToReplacementMapValue = new HashMap<>();

	private String type;

	private BikeType(String type) {
		this.type = type;
	}

	public static BikeType getBikeType(String type) throws Exception {
		if (typesToReplacementMapValue.containsKey(type)) {
			return new BikeType(type);
		} else {
			throw new Exception("NOT A TYPE");
		}
	}

	public static void addType(String bikeTypeName, BigDecimal value) {
		typesToReplacementMapValue.putIfAbsent(bikeTypeName, value);
	}


	public BigDecimal getReplacementValue() {
		return typesToReplacementMapValue.get(type);
	}

	public String getTypeName() {
		return type;
	}
}