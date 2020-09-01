package com.adrian.email.domain;

import com.adrian.email.api.EmailCreateResponse;
import com.adrian.snailgun.domain.SnailgunEmail;
import com.adrian.spendgrid.domain.SpendgridEmail;
import com.adrian.snailgun.domain.SnailgunEmailSendException;
import com.adrian.snailgun.domain.SnailgunResponse;
import com.adrian.spendgrid.domain.SpendgridResponse;
import com.adrian.spendgrid.domain.SpendgridEmailSendException;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final String emailService;
    private final String snailgunApiKey;
    private final String spendgridApiKey;

    final String SNAILGUN_URL = "https://bw-interviews.herokuapp.com/snailgun/emails";
    final String SPENDGRID_URL = "https://bw-interviews.herokuapp.com/spendgrid/send_email";

    public EmailServiceImpl(@Value("${email.service}") String emailService,
                            EmailRepository emailRepository,
                            @Value("${email.snailgun.api-key}") String snailgunApiKey,
                            @Value("${email.spendgrid.api-key}") String spendgridApiKey) {
        this.emailService = emailService;
        this.emailRepository = emailRepository;
        this.snailgunApiKey = snailgunApiKey;
        this.spendgridApiKey = spendgridApiKey;
    }

    @Override
    public EmailCreateResponse createAndSendEmail(ValidEmail validEmail) {
        switch (emailService.toLowerCase()) {
            case "spendgrid":
                final UUID spendgridEmailId = createAndSendSpendgridEmail(validEmail).get();
                return new EmailCreateResponse(spendgridEmailId, emailService);
            case "snailgun":
                final UUID sendSnailgunEmailId = createAndSendSnailgunEmail(validEmail).get();
                return new EmailCreateResponse(sendSnailgunEmailId, emailService);
            default:
                throw new EmailServiceNotFoundException();
        }
    }
    
    //  retryable send to spendgrid with an exponential backoff
    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 5000, multiplier = 2))
    @Transactional(rollbackFor = Exception.class)
    public Try<UUID> createAndSendSpendgridEmail(ValidEmail email) {
        final SpendgridEmail spendgridEmail = SpendgridEmail.from(email);
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final Option<SpendgridResponse> spendgridResponseOpt = Option.of(restTemplate
                    .postForEntity(SPENDGRID_URL,
                            new HttpEntity<>(spendgridEmail, getHttpHeaders(spendgridApiKey)),
                            SpendgridResponse.class).getBody());
            // save email in db
            return spendgridResponseOpt.map(emailRepository::insertSpendgridEmail).get();
        } catch (HttpClientErrorException e) {
            log.warn("Failed to send email with data {}", email);
            throw new SpendgridEmailSendException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }


    //  retryable send to snailgun with an exponential backoff
    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 5000, multiplier = 2))
    @Transactional(rollbackFor = Exception.class)
    public Try<UUID> createAndSendSnailgunEmail(ValidEmail email) {
        // send to spendgrid
        final SnailgunEmail snailgunEmail = SnailgunEmail.from(email);
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final Option<SnailgunResponse> snailgunResponseOpt = Option.of(restTemplate
                    .postForEntity(SNAILGUN_URL,
                            new HttpEntity<>(snailgunEmail, getHttpHeaders(snailgunApiKey)),
                            SnailgunResponse.class).getBody());
            // save email in db
            return snailgunResponseOpt.map(emailRepository::insertSnailgunEmail).get();
        } catch (HttpClientErrorException e) {
            log.warn("Failed to send email with data {}", email);
            throw new SnailgunEmailSendException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    @Override
    public Try<String> updateSnailgunEmail(String snailgunId) {
        // send to spendgrid
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final Option<SnailgunResponse> snailgunResponseOpt = Option.of(restTemplate
                    .exchange(SNAILGUN_URL + "/" + snailgunId,
                            HttpMethod.GET,
                            new HttpEntity<>(getHttpHeaders(snailgunApiKey)),
                            SnailgunResponse.class).getBody());
            // save email in db
            return snailgunResponseOpt.map(emailRepository::updateSnailgunEmail).get();
        } catch (HttpClientErrorException e) {
            log.warn("Failed to fetch email email with Snailgun id {}", snailgunId);
            throw new SnailgunEmailSendException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    private HttpHeaders getHttpHeaders(String apiKey) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Api-Key", apiKey);
        return httpHeaders;
    }


}
