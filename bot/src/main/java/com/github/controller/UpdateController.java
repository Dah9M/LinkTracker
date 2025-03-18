package com.github.controller;

import com.github.model.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdateController {

    @PostMapping
    public ResponseEntity<Void> handleUpdate(@RequestBody LinkUpdate update) {
        //TODO: добавить логику рассылки, пока просто заглушка
        System.out.println("Уведомление получено");

        return ResponseEntity.ok().build();
    }
}
