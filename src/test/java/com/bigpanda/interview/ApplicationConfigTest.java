package com.bigpanda.interview;

import com.bigpanda.interview.entities.PandaEvent;
import com.bigpanda.interview.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationConfigTest {

    @Autowired
    private EventService eventService;

    @Test
    public void eventsCount() {
        List<String> strings = Arrays.asList
                ("{ \"event_type\": \"bar\", \"data\": \"ipsum\", \"timestamp\": 1533390331 }",
                        "{ \"event_type\": \"bar\", \"data\": \"my test 4 split\", \"timestamp\": 1533390331 }",
                        "{ \"event_type\": \"baz\", \"data\": \"sit\", \"timestamp\": 1533390331 }",
                        "{ \"�{]�0#g��",
                        "{ \"event_type\": \"bar\", \"data\": \"dolor\", \"timestamp\": 1533390336 }",
                        "{ \"event_type\": \"baz\", \"data\": \"ipsum\", \"timestamp\": 1533390336 }",
                        "{ \"event_type\": \"bar\", \"data\": \"ipsum\", \"timestamp\": 1533390336 }",
                        "{ \"event_type\": \"baz\", \"data\": \"sit\", \"timestamp\": 1533390336 }",
                        "{ \"event_type\": \"bar\", \"data\": \"ipsum\", \"timestamp\": 1533390336 }",
                        "{ \"event_type\": \"bar\", \"data\": \"test\", \"timestamp\": 1533390336 }",
                        "{ \"event_type\": \"foo\", \"data\": \"sit\", \"timestamp\": 1533390336 }"

                );
        Flux<String> stringFlux = Flux.fromIterable(strings);
        ObjectMapper mapper = new ObjectMapper();
        Flux<PandaEvent> pandaEventFlux = stringFlux.map(s -> {
            try {
                return mapper.readValue(s, PandaEvent.class);
            } catch (Exception e) {
                return new PandaEvent();
            }
        }).log();

        pandaEventFlux.filter(PandaEvent::isEmptyPandaEvent).subscribe(eventService::processEvent);
        assertThat(eventService.getWordCount(Optional.of("test")).getValue()).isEqualTo(2);

    }
}
