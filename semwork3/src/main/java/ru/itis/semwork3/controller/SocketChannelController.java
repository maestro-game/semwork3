package ru.itis.semwork3.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.service.ContentSourceService;
import ru.itis.semwork3.service.MessageService;

import java.util.List;

@Controller
@AllArgsConstructor
@MessageMapping("/channels")
public class SocketChannelController {
    private final ContentSourceService contentSourceService;
    private final MessageService messageService;

    @MessageMapping("/get")
    @SendToUser("/main/channels/get")
    public List<PreviewSourceDto> getAll(SimpMessageHeaderAccessor headerAccessor) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findAllByUserId(Long.valueOf(userDetails.getUsername()));
    }

    @MessageMapping("/{id}/get")
    @SendToUser("/main/channels/{id}/get")
    public MainSourceDto get(@DestinationVariable("id") Long id) {
        return contentSourceService.findById(id).orElse(null);
    }

    @MessageMapping("/{id}/send")
    @SendTo("/main/channels/{id}")
    public InnerMessageDto send(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable("id") Long id, @Payload String text) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return messageService.saveNew(text, Long.valueOf(userDetails.getUsername()), id).orElse(null);
    }

    @MessageMapping("/create")
    @SendToUser("/main/channels/create")
    public MainSourceDto create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody NewSourceDto dto) {
        dto.setAdmin(User.builder().id(Long.valueOf(userDetails.getUsername())).build());
        return contentSourceService.saveNew(dto).orElse(null);
    }

    @MessageMapping("/{id}/delete")
    @SendToUser("/main/channels/delete")
    public Boolean delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id) {
        return contentSourceService.delete(id, User.builder().id(Long.valueOf(userDetails.getUsername())).build());
    }
}
