package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.dto.message.RemoveMessageDto;
import ru.itis.semwork3.dto.response.ResponseDto;
import ru.itis.semwork3.service.MessageService;

@Controller
@RequiredArgsConstructor
@MessageMapping("/messages/{id}")
public class SocketMessageController {
    private final MessageService messageService;

    @MessageMapping("/send")
    @SendTo("/main/channels/{id}")
    public ResponseDto<InnerMessageDto> send(SimpMessageHeaderAccessor headerAccessor,
                                             @DestinationVariable("id") String id,
                                             @Payload String text) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return messageService.saveNew(text, userDetails.getUsername(), id)
                .map(dto -> new ResponseDto<>(1, dto)).orElse(null);
    }

    @MessageMapping("/repost/{post}")
    @SendTo("/main/channels/{id}")
    public ResponseDto<InnerMessageDto> repost(SimpMessageHeaderAccessor headerAccessor,
                                               @DestinationVariable("id") String id,
                                               @DestinationVariable("post") Long post) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return messageService.saveNewRepost(post, userDetails.getUsername(), id)
                .map(dto -> new ResponseDto<>(1, dto)).orElse(null);
    }

    @MessageMapping("/{channel}/delete")
    @SendTo("/main/channels/{id}")
    public ResponseDto<RemoveMessageDto> delete(SimpMessageHeaderAccessor headerAccessor,
                                                @DestinationVariable("channel") Long id,
                                                @DestinationVariable("id") String channelId) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        RemoveMessageDto dto = messageService.delete(id, userDetails.getUsername(), channelId);
        return dto != null ? new ResponseDto<>(2, dto) : null;
    }
}
