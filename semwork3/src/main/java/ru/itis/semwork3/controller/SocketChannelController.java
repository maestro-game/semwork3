package ru.itis.semwork3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.service.ContentSourceService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@MessageMapping("/channels")
public class SocketChannelController {
    private final ContentSourceService contentSourceService;

    @MessageMapping("/get")
    @SendToUser("/main/channels/get")
    public List<PreviewSourceDto> getAll(SimpMessageHeaderAccessor headerAccessor) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findAllByUserId(userDetails.getUsername());
    }

    @MessageMapping("/{id}/get")
    @SendToUser("/main/channels/{id}/get")
    public MainSourceDto get(SimpMessageHeaderAccessor headerAccessor,
                             @DestinationVariable("id") String id) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(0, 20, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/{id}/get/am/{amount}")
    @SendToUser("/main/channels/{id}/get")
    public MainSourceDto getWithAmount(SimpMessageHeaderAccessor headerAccessor,
                                       @DestinationVariable("id") String id,
                                       @DestinationVariable("amount") int amount) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(0, amount, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/{id}/get/{page}")
    @SendToUser("/main/channels/{id}/get")
    public MainSourceDto getWithPage(SimpMessageHeaderAccessor headerAccessor,
                                     @DestinationVariable("id") String id,
                                     @DestinationVariable("page") int page) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(page, 20, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/{id}/get/{page}/{amount}")
    @SendToUser("/main/channels/{id}/get")
    public MainSourceDto getWithPageAndAmount(SimpMessageHeaderAccessor headerAccessor,
                                              @DestinationVariable("id") String id,
                                              @DestinationVariable("amount") int amount,
                                              @DestinationVariable("page") int page) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(page, amount, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/create")
    @SendToUser("/main/channels/create")
    public MainSourceDto create(SimpMessageHeaderAccessor headerAccessor,
                                @RequestBody NewSourceDto dto) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        dto.setAdmin(User.builder().id(userDetails.getUsername()).build());
        return contentSourceService.saveNew(dto).orElse(null);
    }

    @MessageMapping("/{id}/delete")
    @SendToUser("/main/channels/delete")
    public Boolean delete(SimpMessageHeaderAccessor headerAccessor,
                          @PathVariable("id") String id) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.delete(id, User.builder().id(userDetails.getUsername()).build());
    }
}
