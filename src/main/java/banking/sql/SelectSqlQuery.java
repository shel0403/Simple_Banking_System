package banking.sql;

public class SelectSqlQuery implements SqlQuery{

    private final String query;

    public SelectSqlQuery() {
        this.query = "";
    }

    public SelectSqlQuery(final String query) {
        this.query = (query.trim()) + " ";
    }

    public final SelectSqlQuery select(final String... query) {
        if (query == null || query.length == 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        SelectSqlQuery result = new SelectSqlQuery(this.query);

        for (int i = 0; i < query.length - 1; i++) {
            result = new SelectSqlQuery(result.query.trim() + query[i] + ", ");
        }

        return new SelectSqlQuery(result.query.trim() + query[query.length - 1] + " ");
    }

    public final SelectSqlQuery fields(final String... fields) {
        if (fields == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        SelectSqlQuery result = new SelectSqlQuery(this.query.trim());

        for (int i = 0; i < fields.length - 1; i++) {
            result = new SelectSqlQuery(result.query.trim() + " " + fields[i] + ",");
        }

        return new SelectSqlQuery(result.query.trim() + fields[fields.length - 1] + " ");
    }

    public final SelectSqlQuery from(final String table) {
        if (table == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new SelectSqlQuery(this.query.trim() + " FROM " + table + " ");
    }

    public final SelectSqlQuery where(final String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new SelectSqlQuery(this.query.trim() + " WHERE " + statement + " ");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String create() {
        return "SELECT " + query.trim() + ";";
    }
}
