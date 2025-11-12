import java.util.*;

/**
 * FarmGroup aggregates multiple Farmer objects.
 */
public class FarmGroup {
    private String name;
    private List<Farmer> members = new ArrayList<>();

    public FarmGroup(String name) {
        this.name = name;
    }

    public void addMember(Farmer farmer) {
        members.add(farmer);
    }

    public void removeMember(Farmer farmer) {
        members.remove(farmer);
    }

    public List<Farmer> getMembers() {
        return members;
    }
}

