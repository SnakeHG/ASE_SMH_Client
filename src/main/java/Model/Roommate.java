package Model;

import java.util.Objects;

public class Roommate {
    private long id;
    private String name;
    private String city;
    private Integer minBudget;
    private Integer maxBudget;
    private String notes;

    private long requestId;
    private Status status;

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

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) { this.requestId = requestId; }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roommate roommate = (Roommate) o;
        return id == roommate.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
