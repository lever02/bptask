package com.bigpanda.interview;

import com.bigpanda.interview.entities.PandaEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ApplicationEntitiesTests {

    private JacksonTester<PandaEvent> json;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, this.objectMapper);
    }

    @Test
    public void testDeserialize() throws IOException {
        String pandaEvent = "{ \"event_type\": \"foo\", \"data\": \"sit\", \"timestamp\": 1533388758 }";
        assertThat(this.json.parse(pandaEvent).getObject().getType()).isEqualTo("foo");
        assertThat(this.json.parse(pandaEvent).getObject().getData()).isEqualTo("sit");
        assertThat(this.json.parse(pandaEvent).getObject().getDate()).hasYear(2018);
    }

    @Test
    public void IncrementMapTest() throws IOException {
        String pandaEvent = "{ \"event_type\": \"foo\", \"data\": \"sit\", \"timestamp\": 1533388758 }";
        ObjectContent<PandaEvent> parse = this.json.parse(pandaEvent);
        ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>();
        map.computeIfAbsent(parse.getObject().getType(), key -> new LongAdder()).increment();
        map.computeIfAbsent(parse.getObject().getType(), key -> new LongAdder()).increment();
        assertThat(map.get(parse.getObject().getType()).longValue()).isEqualTo(2);
    }
}
