package com.bigpanda.interview.controller;

import com.bigpanda.interview.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

@Controller
@RequestMapping("/pandaEvent/")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("eventTypeStats")
    @ResponseBody
    public ResponseEntity<Map<String, LongAdder>> getEventTypeCount() {
        Map<String, LongAdder> eventTypeCount = eventService.getEventTypeCount();
        return new ResponseEntity<>(eventTypeCount, HttpStatus.OK);
    }

    @GetMapping("wordStats")
    @ResponseBody
    public ResponseEntity<Map<String, LongAdder>> getWordCount() {
        Map<String, LongAdder> eventTypeCount = eventService.getWordCount();
        return new ResponseEntity<>(eventTypeCount, HttpStatus.OK);
    }

    @GetMapping("eventTypeStats/{eventType}")
    @ResponseBody
    public ResponseEntity<Map.Entry<String, Long>> getEventTypeCount(@PathVariable("eventType") Optional<String> eventType) {
        Map.Entry<String, Long> eventTypeCount = eventService.getEventTypeCount(eventType);
        return new ResponseEntity<>(eventTypeCount, HttpStatus.OK);
    }

    @GetMapping("wordStats/{word}")
    @ResponseBody
    public ResponseEntity<Map.Entry<String, Long>> getWordCount(@PathVariable("word") Optional<String> word) {
        Map.Entry<String, Long> eventTypeCount = eventService.getWordCount(word);
        return new ResponseEntity<>(eventTypeCount, HttpStatus.OK);
    }
}
