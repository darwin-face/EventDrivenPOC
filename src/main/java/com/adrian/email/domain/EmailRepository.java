package com.adrian.email.domain;

import com.adrian.snailgun.domain.SnailgunResponse;
import com.adrian.spendgrid.domain.SpendgridResponse;
import io.vavr.control.Try;

import java.util.UUID;

public interface EmailRepository {
    Try<UUID> insertSnailgunEmail(SnailgunResponse snailgunResponse);

    Try<String> updateSnailgunEmail(SnailgunResponse snailgunResponse);

    Try<UUID> insertSpendgridEmail(SpendgridResponse spendgridResponse);
}
