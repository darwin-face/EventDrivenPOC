package com.adrian.api;

import com.adrian.QueueService;

import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    private QueueService queueService;

    GreetingController(QueueService queueService) {
        this.queueService = queueService;
    }

    private static final String template = "Get Rekt, %s!";

    @GetMapping(path = { "/greeting/{name}", "/greeting/"})
    public String greeting(@PathVariable(value = "name", required = false) String name) {

        return Option.of(name)
                .map(n -> queueService.enqueue(String.format(template, n)))
                .getOrElse(queueService.enqueue(String.format(template, "World")));
    }


}
