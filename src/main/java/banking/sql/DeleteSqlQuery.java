package banking.sql;

public class DeleteSqlQuery implements SqlQuery{

    private final String query;

    public DeleteSqlQuery() {
        this.query = "";
    }

    public DeleteSqlQuery(final String query) {
        this.query = (query.trim()) + " ";
    }

    public final DeleteSqlQuery from(final String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new DeleteSqlQuery(this.query.trim() + " " + tableName + " ");
    }

    public final DeleteSqlQuery where(final String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new DeleteSqlQuery(this.query.trim() + " WHERE " + statement + " ");
    }

    @Override
    public String toString() {
        return create();
    }

    @Override
    public String create() {
        return "DELETE FROM " + query.trim() + ";";
    }
}
