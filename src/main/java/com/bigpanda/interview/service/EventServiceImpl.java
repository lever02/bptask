package com.bigpanda.interview.service;

import com.bigpanda.interview.entities.PandaEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Service
public class EventServiceImpl implements EventService {

    private Map<String, LongAdder> eventTypeMap;
    private Map<String, LongAdder> wordCountMap;

    @PostConstruct
    public void init() {
        eventTypeMap = new ConcurrentHashMap<>();
        wordCountMap = new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, LongAdder> getEventTypeCount() {
        return eventTypeMap;
    }

    @Override
    public Map<String, LongAdder> getWordCount() {
        return wordCountMap;
    }

    @Override
    public Map.Entry<String, Long> getEventTypeCount(Optional<String> eventType) {
        return new AbstractMap.SimpleImmutableEntry<>(eventType.orElseThrow(() -> new RuntimeException("YANGI")),
                eventTypeMap.get(eventType.get()) == null ? 0L : eventTypeMap.get(eventType.get()).longValue());
    }

    @Override
    public Map.Entry<String, Long> getWordCount(Optional<String> word) {
        return new AbstractMap.SimpleImmutableEntry<>(word.orElseThrow(() -> new RuntimeException("YANGI")),
                wordCountMap.get(word.get()) == null ? 0L : wordCountMap.get(word.get()).longValue());
    }

    @Override
    public void processEvent(PandaEvent pandaEvent) {
        eventTypeMap.computeIfAbsent(pandaEvent.getType(), key -> new LongAdder()).increment();
        Arrays.stream(splitDataToWordsOnly(pandaEvent)).forEach(word ->
                wordCountMap.computeIfAbsent(word, key -> new LongAdder()).increment());
    }

    private String[] splitDataToWordsOnly(PandaEvent pandaEvent) {
        return pandaEvent.getData().replaceAll("[^a-zA-Z]", " ").toLowerCase().split("\\s");
    }
}
