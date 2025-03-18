package com.github.controller;

import com.github.model.AddLinkRequest;
import com.github.model.LinkResponse;
import com.github.model.ListLinkResponse;
import com.github.model.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: бизнес логика
@RestController
@RequestMapping("/")
public class ScrapperController {

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinkResponse> getTrackedList(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return ResponseEntity.ok(new ListLinkResponse(List.of(), 0));
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") Long chatId,
                                                @RequestBody AddLinkRequest request) {
        return ResponseEntity.ok(new LinkResponse(1L, request.getLink(), List.of(), List.of()));
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(@RequestHeader("Tg-Chat-Id") Long chatId,
                                                   @RequestBody RemoveLinkRequest request) {
        return ResponseEntity.ok(new LinkResponse(1L, request.getLink(), List.of(), List.of()));
    }
}
