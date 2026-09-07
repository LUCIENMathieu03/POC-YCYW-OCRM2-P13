package com.poc_ycyw.ycyw.controller;

import com.poc_ycyw.ycyw.dto.SendMessageRequestDTO;
import com.poc_ycyw.ycyw.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages/conversation")
    public ResponseEntity<?> getMyConversation() {
        return ResponseEntity.ok(messageService.getConversationForCurrentClient());
    }

    @GetMapping("/messages/conversation/{clientId}")
    public ResponseEntity<?> getConversationWithClient(@PathVariable Integer clientId) {
        try {
            return ResponseEntity.ok(messageService.getConversationWithClient(clientId));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequestDTO dto) {
        try {
            return ResponseEntity.ok(messageService.sendMessage(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/conversations")
    public ResponseEntity<?> getAdminConversations() {
        try {
            return ResponseEntity.ok(messageService.getAdminConversations());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
