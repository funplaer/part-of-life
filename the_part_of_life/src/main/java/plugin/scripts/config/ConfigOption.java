package plugin.scripts.config;

public class ConfigOption {

    private final String name;
    private final Class<?> type;
    private final double min;
    private final double max;

    public ConfigOption(
            String name,
            Class<?> type,
            double min,
            double max
    ) {
        this.name = name;
        this.type = type;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}