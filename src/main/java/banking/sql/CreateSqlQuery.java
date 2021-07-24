package banking.sql;

import java.util.Arrays;

public class CreateSqlQuery implements SqlQuery{

    private final String query;

    public CreateSqlQuery() {
        this.query = "";
    }

    public CreateSqlQuery(final String query) {
        this.query = (query.trim()) + " ";
    }

    public final CreateSqlQuery database(final String dataBaseName) {
        if (dataBaseName == null) {
            throw new IllegalArgumentException();
        }

        return new CreateSqlQuery(this.query.trim() + " DATABASE " + dataBaseName.trim());
    }

    public final CreateSqlQuery databaseIfNotExists(final String dataBaseName) {
        if (dataBaseName == null) {
            throw new IllegalArgumentException();
        }

        return new CreateSqlQuery(this.query.trim() + " DATABASE IF NOT EXISTS " + dataBaseName.trim());
    }

    public final CreateSqlQuery table(final String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException();
        }

        return new CreateSqlQuery(this.query.trim() + " TABLE " + tableName.trim());
    }

    public final CreateSqlQuery tableIfNotExists(final String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException();
        }

        return new CreateSqlQuery(this.query.trim() + " TABLE IF NOT EXISTS " + tableName.trim());
    }

    public final CreateSqlQuery fields(final String... fields) {
        if (fields == null) {
            throw new IllegalArgumentException();
        }

        Arrays.stream(fields).forEach(field -> {
            if (field == null) {
                throw new IllegalArgumentException();
            }
        });

        CreateSqlQuery result = new CreateSqlQuery(this.query.trim() + "(");

        for (int i = 0; i < fields.length - 1; i++) {
            result = new CreateSqlQuery(" " + result.query.trim() + fields[i] + ", ");
        }

        return new CreateSqlQuery(" " + result .query.trim() + fields[fields.length - 1] + ")");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String create() {
        return "CREATE " + query.trim() + ";";
    }
}
