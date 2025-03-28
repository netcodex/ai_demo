package org.lizard.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 *
 * @author X
 * @since 2025-03-24 22:06
 * @version 1.0
 **/
@RestController
public class ChatController {

    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map<String, String>
        generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Map<String, String> map = new java.util.HashMap<>();
        map.put("generation", this.chatModel.call(message));
        return map;
    }

    /**
     * 流式响应（适合长回答）
     *
     * @param message
     *            问题
     * @return 响应
     */
    @GetMapping(value = "/ai/generateStream",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse>
        generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }
}
