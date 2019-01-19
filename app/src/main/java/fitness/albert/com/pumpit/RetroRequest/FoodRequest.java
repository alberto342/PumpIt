package fitness.albert.com.pumpit.RetroRequest;

public class FoodRequest {
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "ClassPojo [query = " + query + "]";
    }

    public FoodRequest(String query) {
        this.query = query;
    }
}
