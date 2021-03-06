package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BikeType {
    private static final Map<String, BigDecimal> typesToReplacementMapValue = new HashMap<>();

    private final String type;

    private BikeType(String type) {
        this.type = type;
    }

    // use static Map to contain all the bike types in the system
    public static BikeType getBikeType(String type) throws Exception {
        if (typesToReplacementMapValue.containsKey(type)) {
            return new BikeType(type);
        } else {
            throw new Exception("NOT A TYPE");
        }
    }

    public static void addType(String bikeTypeName, BigDecimal value) {
        assert Objects.nonNull(value);

        typesToReplacementMapValue.putIfAbsent(bikeTypeName, value);
    }


    public BigDecimal getReplacementValue() {
        return typesToReplacementMapValue.get(type);
    }

    public String getTypeName() {
        return type;
    }
}