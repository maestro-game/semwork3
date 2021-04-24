package ru.itis.semwork3.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.semwork3.dto.contentsource.MainSourceDto;
import ru.itis.semwork3.dto.contentsource.NewSourceDto;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;
import ru.itis.semwork3.model.User;
import ru.itis.semwork3.service.ContentSourceService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ContentSourceService contentSourceService;

    @GetMapping
    public ResponseEntity<List<PreviewSourceDto>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contentSourceService.findAllByUserId(Long.valueOf(userDetails.getUsername())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainSourceDto> get(@PathVariable("id") Long id) {
        Optional<MainSourceDto> result = contentSourceService.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MainSourceDto> create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody NewSourceDto dto) {
        dto.setAdmin(User.builder().id(Long.valueOf(userDetails.getUsername())).build());
        var result = contentSourceService.saveNew(dto);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id) {
        return contentSourceService.delete(id, User.builder().id(Long.valueOf(userDetails.getUsername())).build())
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }
}
