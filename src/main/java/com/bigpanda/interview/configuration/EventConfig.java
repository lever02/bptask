package com.bigpanda.interview.configuration;

import com.bigpanda.interview.entities.PandaEvent;
import com.bigpanda.interview.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class EventConfig {

    @Value("${panda.generator}")
    private String pandaGenerator;
    private final EventService eventService;

    public EventConfig(EventService eventService) {
        this.eventService = eventService;
    }

    private Callable<InputStream> getProcessStream() {
        return Objects.requireNonNull(generatorProcess())::getInputStream;
    }

    private Process generatorProcess() {
        if (pandaGenerator.equals("local")) {
            if (SystemUtils.IS_OS_WINDOWS) {
                pandaGenerator = "generator-windows-amd64.exe";
            }
            if (SystemUtils.IS_OS_LINUX) {
                pandaGenerator = "generator-linux-amd64";
            }
            if (SystemUtils.IS_OS_MAC) {
                pandaGenerator = "generator-macosx-amd64";
            }
            try {
                pandaGenerator = new ClassPathResource(pandaGenerator).getURI().getPath();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        ProcessBuilder builder = new ProcessBuilder(pandaGenerator);
        builder.redirectErrorStream(true);
        try {
            return builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void processStream() {
        ExecutorService executor = Executors.newWorkStealingPool();
        executor.submit(this::getProcessStream);
        Flux<PandaEvent> decode = getPandaEventFlux();
        executor.submit(() -> {
            decode.filter(PandaEvent::isEmptyPandaEvent).subscribe(eventService::processEvent);
        });
    }

    private Flux<PandaEvent> getPandaEventFlux() {
        ObjectMapper mapper = new ObjectMapper();
        StringDecoder stringDecoder = StringDecoder.allMimeTypes();
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
        Flux<DataBuffer> read = DataBufferUtils.readInputStream(getProcessStream(), dataBufferFactory, 10);
        return stringDecoder.decode(read, ResolvableType.forClass(String.class), null, Collections.emptyMap()).map(s -> {
            try {
                return mapper.readValue(s, PandaEvent.class);
            } catch (Exception e) {
                return new PandaEvent();
            }
        }).log();
    }

    @PostConstruct
    public void init() {
        try {
            processStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
