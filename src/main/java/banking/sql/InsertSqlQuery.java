package banking.sql;

public class InsertSqlQuery implements SqlQuery {

    private final String query;

    public InsertSqlQuery() {
        this.query = "";
    }

    public InsertSqlQuery(final String query) {
        this.query = (query.trim()) + " ";
    }

    public final InsertSqlQuery into(final String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new InsertSqlQuery(this.query.trim() + " " + tableName + " ");
    }

    public final InsertSqlQuery fields(final String... fields) {
        if (fields == null || fields.length == 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        InsertSqlQuery result = new InsertSqlQuery(this.query.trim() + "(");

        for (int i = 0; i < fields.length - 1; i++) {
            result = new InsertSqlQuery(result.query.trim() + fields[i] + ", ");
        }

        return new InsertSqlQuery(result.query.trim() + fields[fields.length - 1] + ")");
    }

    public final InsertSqlQuery values(final String... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        InsertSqlQuery result = new InsertSqlQuery(this.query.trim() + " VALUES ");

        for (int i = 0; i < values.length - 1; i++) {
            result = new InsertSqlQuery(result.query.trim() + "(" + values[i] + "), ");
        }

        return new InsertSqlQuery(result.query.trim() + "(" + values[values.length - 1] + ")");
    }

    public final InsertSqlQuery where(final String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new InsertSqlQuery(this.query.trim() + " WHERE " + statement + " ");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String create() {
        return "INSERT INTO " + query + ";";
    }
}
