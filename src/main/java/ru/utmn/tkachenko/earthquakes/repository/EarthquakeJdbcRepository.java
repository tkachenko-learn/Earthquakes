package ru.utmn.tkachenko.earthquakes.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Profile("JdbcEngine")
@Primary
public class EarthquakeJdbcRepository implements CommonRepository<Earthquake> {

    private static final String SQL_INSERT = """
            insert into earthquake
                (id, deep, type, magnitude, state, date_time)
            values
                (:id, :deep, :type, :magnitude, :state, :dateTime)     
            """;

    private static final String SQL_UPDATE = """
            update earthquake
                set deep = :deep, type = :type, magnitude = :magnitude, state = :state, date_time = :dateTime
            where id = :id
            """;

    private static final String SQL_DELETE = "delete from earthquake where id = :id";

    private static final String SQL_EXIST = "select count(*) > 0 from earthquake where id = :id";

    private static final String SQL_FIND_ALL = "select id, deep, type, magnitude, state, date_time from earthquake";

    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " where id = :id";

    private static final String SQL_COUNT = "select count(*) from earthquake";

    private final NamedParameterJdbcTemplate template;

    public EarthquakeJdbcRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }


    @Override
    public Earthquake save(Earthquake domain) {
        if (exists(domain.getId()))
            return insertOrUpdate(SQL_UPDATE, domain);
        return insertOrUpdate(SQL_INSERT, domain);
    }

    private Earthquake insertOrUpdate(final String sql, Earthquake domain) {
        Map<String, Object> namedParameter = new HashMap<>();
        namedParameter.put("id", domain.getId());
        namedParameter.put("deep", domain.getDeep());
        namedParameter.put("type", domain.getType());
        namedParameter.put("magnitude", domain.getMagnitude());
        namedParameter.put("state", domain.getState());
        namedParameter.put("dateTime", Timestamp.valueOf(domain.getDateTime()));

        template.update(sql, namedParameter);
        return findById(domain.getId());
    }

    @Override
    public Iterable<Earthquake> save(Collection<Earthquake> domains) {
        // ToDo: партиционировать domains по 1_000
        template.batchUpdate(SQL_INSERT, SqlParameterSourceUtils.createBatch(domains));
        return findAll();
    }

    @Override
    public void delete(String id) {
        Map<String, String> namedParameter = Collections.singletonMap("id", id);
        template.update(SQL_DELETE, namedParameter);
    }

    @Override
    public void delete(Earthquake domain) {
        delete(domain.getId());
    }

    @Override
    public Earthquake findById(String id) {
        Map<String, String> namedParameter = Collections.singletonMap("id", id);
        return template.queryForObject(SQL_FIND_BY_ID, namedParameter, earthquakeRowMapper);
    }

    @Override
    public Iterable<Earthquake> findAll() {
        return template.query(SQL_FIND_ALL, earthquakeRowMapper);
    }

    private RowMapper<Earthquake> earthquakeRowMapper = (ResultSet rs, int rowNum) -> {
        Earthquake earthquake = new Earthquake();
        earthquake.setId(rs.getString("id"));
        earthquake.setDeep(rs.getInt("deep"));
        earthquake.setMagnitude(rs.getDouble("magnitude"));
        earthquake.setType(rs.getString("type"));
        earthquake.setState(rs.getString("state"));
        earthquake.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        return earthquake;
    };

    @Override
    public boolean exists(String id) {
        Map<String, String> namedParameter = Collections.singletonMap("id", id);
        return template.queryForObject(SQL_EXIST, namedParameter, Boolean.class).booleanValue();
    }

    @Override
    public long count() {
        return template.queryForObject(SQL_COUNT, Collections.emptyMap(), Long.class).longValue();
    }
}
