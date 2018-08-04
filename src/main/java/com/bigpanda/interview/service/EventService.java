package com.bigpanda.interview.service;

import com.bigpanda.interview.entities.PandaEvent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public interface EventService {
    Map<String, LongAdder> getEventTypeCount();

    Map<String, LongAdder> getWordCount();

    Map.Entry<String, Long> getEventTypeCount(Optional<String> eventType);

    Map.Entry<String, Long> getWordCount(Optional<String> word);

    void processEvent(PandaEvent pandaEvent);
}
