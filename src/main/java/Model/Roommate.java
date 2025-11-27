package Model;

public class Roommate {
    private long id;
    private String name;
    private String city;
    private Integer minBudget;
    private Integer maxBudget;
    private String notes;

    public Roommate(long id, String name, String city, Integer minBudget,
                             Integer maxBudget, String notes) {

        if (minBudget < 0 || maxBudget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        if (minBudget > maxBudget) {
            throw new IllegalArgumentException("Minimum budget cannot be greater than maximum budget");
        }
        this.id = id;
        this.name = name;
        this.city = city;
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Integer getMinBudget() {
        return minBudget;
    }
    public Integer getMaxBudget() {
        return maxBudget;
    }
    public String getNotes() {
        return notes;
    }
}
