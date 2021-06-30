package banking.sql;

public class CreateSqlQuery implements SqlQuery{

    private final String query;

    public CreateSqlQuery() {
        this.query = "";
    }

    public CreateSqlQuery(final String query) {
        this.query = (query.trim()) + " ";
    }

    public final CreateSqlQuery database(final String dataBaseName) {
        if (dataBaseName == null || dataBaseName.isEmpty()) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new CreateSqlQuery(this.query.trim() + " DATABASE " + dataBaseName.trim());
    }

    public final CreateSqlQuery table(final String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return new CreateSqlQuery(this.query.trim() + " TABLE " + tableName.trim());
    }

    public final CreateSqlQuery fields(final String... fields) {
        if (fields == null || fields.length == 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        CreateSqlQuery result = new CreateSqlQuery(this.query.trim() + "(");

        for (int i = 0; i < fields.length - 1; i++) {
            result = new CreateSqlQuery(result + fields[i] + ", ");
        }

        return new CreateSqlQuery(result + fields[fields.length - 1] + ")");
    }

    @Override
    public String toString() {
        return create();
    }

    @Override
    public String create() {
        return "CREATE " + query.trim() + ";";
    }
}
