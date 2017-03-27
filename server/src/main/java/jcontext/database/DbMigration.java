package jcontext.database;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
class DbMigration {
    private final Set<String> sqlResources;

    @Inject
    DbMigration(@Named("sqlResources") Set<String> sqlResources) {
        this.sqlResources = sqlResources;
    }

    void migrateIfNeeded(Connection connection) throws SQLException {
        if (connection.getMetaData().getSchemas().next()) return;
        ClassLoader classLoader = DbManager.class.getClassLoader();

        List<String> migrationScripts = sqlResources.stream()
                .sorted(String::compareTo)
                .map(classLoader::getResourceAsStream)
                .map(DbMigration::getStringForStream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (String migration : migrationScripts) {
            if (connection.createStatement().executeUpdate(migration) == 0) {
                log.info("Migration script failed: {}", migration);
            }
        }

        ResultSet newSchemas = connection.getMetaData().getSchemas();
        while (newSchemas.next()) {
            log.info("Schema: {}", newSchemas.getString(1));
        }
    }

    private static String getStringForStream(InputStream stream) {
        try {
            return IOUtils.toString(stream, Charsets.UTF_8);
        } catch (IOException e) {
            log.error("Error reading from stream", e);
            return null;
        }
    }
}
