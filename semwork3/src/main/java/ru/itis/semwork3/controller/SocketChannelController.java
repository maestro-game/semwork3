package ru.itis.semwork3.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.dto.contentsource.TitleSourceDto;
import ru.itis.semwork3.dto.message.InnerMessageDto;
import ru.itis.semwork3.dto.response.ResponseDto;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.service.ContentSourceService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@MessageMapping("/channels")
public class SocketChannelController {
    private final ContentSourceService contentSourceService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/get")
    @SendToUser("/main/channels/get")
    public List<PreviewSourceDto> getAll(SimpMessageHeaderAccessor headerAccessor) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findAllByUserId(userDetails.getUsername());
    }

    @MessageMapping("/current/get")
    @SendToUser("/main/channels/current/get")
    public MainSourceDto get(SimpMessageHeaderAccessor headerAccessor,
                             @RequestBody String id) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(0, 20, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/current/get/am/{amount}")
    @SendToUser("/main/channels/current/get")
    public MainSourceDto getWithAmount(SimpMessageHeaderAccessor headerAccessor,
                                       @RequestBody String id,
                                       @DestinationVariable("amount") int amount) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(0, amount, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/current/get/{page}")
    @SendToUser("/main/channels/current/get")
    public MainSourceDto getWithPage(SimpMessageHeaderAccessor headerAccessor,
                                     @RequestBody String id,
                                     @DestinationVariable("page") int page) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.findByIdAndUser(id,
                userDetails.getUsername(),
                PageRequest.of(page, 20, Sort.by("id").ascending())).orElse(null);
    }

    @MessageMapping("/current/get/{page}/{amount}")
    @SendToUser("/main/channels/current/get")
    public MainSourceDto getWithPageAndAmount(SimpMessageHeaderAccessor headerAccessor,
                                              @RequestBody String id,
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
                          @DestinationVariable("id") String id) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        return contentSourceService.delete(id, User.builder().id(userDetails.getUsername()).build());
    }

    @MessageMapping("/search")
    @SendToUser("/main/channels/search")
    public List<TitleSourceDto> searchById(@RequestBody String id) {
        return contentSourceService.searchById(id);
    }

    @MessageMapping("/{id}/join")
    @SendToUser("/main/channels/join")
    public PreviewSourceDto join(SimpMessageHeaderAccessor headerAccessor,
                                 @DestinationVariable("id") String id,
                                 @RequestBody(required = false) String message) {
        UserDetails userDetails = ((UserDetails) headerAccessor.getSessionAttributes().get("user"));
        Optional<InnerMessageDto> respMes;
        try {
            respMes = contentSourceService.join(id, userDetails.getUsername(), message);
        } catch (NotFoundException e) {
            return null;
        }
        final PreviewSourceDto[] result = new PreviewSourceDto[1];
        respMes.ifPresentOrElse(dto -> {
            if (dto.getId() != null) {
                result[0] = contentSourceService.findPreviewById(id);
                messagingTemplate.convertAndSend("/main/channels/" + id, new ResponseDto<>(1, dto));
            } else {
                result[0] = contentSourceService.findPreviewById(respMes.get().getText());
                messagingTemplate.convertAndSendToUser(id, "/main/channels/join", result[0]);
            }
        }, () -> {
            result[0] = contentSourceService.findPreviewById(id);
        });
        return result[0];
    }
}
