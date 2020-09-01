package com.adrian.email.domain;

import com.adrian.snailgun.domain.SnailgunResponse;
import com.adrian.spendgrid.domain.SpendgridResponse;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
public class EmailRepositoryImpl implements EmailRepository {
    private static final String MAPPER = "com.adrian.email.domain.SentEmailMapper";
    private static final String INSERT_SNAILGUN_EMAIL = MAPPER + ".insertSnailgunEmail";
    private static final String UPDATE_SNAILGUN_EMAIL = MAPPER + ".updateSnailgunEmail";
    private static final String INSERT_SPENDGRID_EMAIL = MAPPER + ".insertSpendgridEmail";

    @Autowired
    private final SqlSession sqlSession;

    public EmailRepositoryImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Try<UUID> insertSnailgunEmail(SnailgunResponse snailgunResponse){
        UUID emailPrimaryKey = UUID.randomUUID();
        final Map<String, Object> map = HashMap.of("snailgun_UUID", emailPrimaryKey,
                                                    "email", snailgunResponse);

        return Try.of(() -> sqlSession.insert(INSERT_SNAILGUN_EMAIL, map.toJavaMap()))
                .flatMap(rowsUpdated -> {
                    if(rowsUpdated == 1) {
                        log.info("Snailgun email with id [{}] successfully updated", emailPrimaryKey);
                        return Try.success(emailPrimaryKey);
                    } else {
                        final String error = String.format("Failed to save Snailgun email with data %s", snailgunResponse);
                        log.warn(error);
                        return Try.failure(new IllegalStateException(error));
                    }
                });
    }

    @Override
    public Try<String> updateSnailgunEmail(SnailgunResponse snailgunResponse) {
        final Map<String, Object> map = HashMap.of("email", snailgunResponse);

        return Try.of(() -> sqlSession.update(UPDATE_SNAILGUN_EMAIL, map.toJavaMap()))
                .flatMap(rowsInserted -> {
                    if(rowsInserted == 1) {
                        log.info("Snailgun email with id [{}] successfully Updated", snailgunResponse.getId());
                        return Try.success(snailgunResponse.getId());
                    } else {
                        final String error = String.format("Failed to save Snailgun email with data %s", snailgunResponse);
                        log.warn(error);
                        return Try.failure(new IllegalStateException(error));
                    }
                });
    }

    @Override
    public Try<UUID> insertSpendgridEmail(SpendgridResponse spendgridResponse){
        UUID emailPrimaryKey = UUID.randomUUID();
        final Map<String, Object> map = HashMap.of("spendgrid_UUID", emailPrimaryKey,
                "email", spendgridResponse);

        return Try.of(() -> sqlSession.insert(INSERT_SPENDGRID_EMAIL, map.toJavaMap()))
                .flatMap(rowsInserted -> {
                    if(rowsInserted == 1) {
                        log.info("Spendgrid email with id [{}] successfully saved", emailPrimaryKey);
                        return Try.success(emailPrimaryKey);
                    } else {
                        final String error = String.format("Failed to save Spendgrid email with data %s", spendgridResponse);
                        log.warn(error);
                        return Try.failure(new IllegalStateException(error));
                    }
                });
    }
}
