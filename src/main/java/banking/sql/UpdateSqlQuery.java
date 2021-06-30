package banking.sql;

public class UpdateSqlQuery implements SqlQuery{

    private final String query;

    public UpdateSqlQuery() {
        this.query = "";
    }

    public UpdateSqlQuery(final String query) {
        this.query = (query.trim()) + " ";
    }

    public final UpdateSqlQuery table(final String name){
        if (name == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new UpdateSqlQuery(this.query + " " + name + " ");
    }

    public final UpdateSqlQuery set(final String... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        UpdateSqlQuery result = new UpdateSqlQuery(this.query.trim() + " SET ");

        for (int i = 0; i < values.length - 1; i++) {
            result = new UpdateSqlQuery(result + values[i] + ", ");
        }

        return new UpdateSqlQuery(result + values[values.length - 1] + " ");
    }

    public final UpdateSqlQuery where(final String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new UpdateSqlQuery(this.query.trim() + " WHERE " + statement.trim());
    }

    @Override
    public String toString() {
        return create();
    }

    @Override
    public String create() {
        return "UPDATE " + query.trim() + ";";
    }
}
