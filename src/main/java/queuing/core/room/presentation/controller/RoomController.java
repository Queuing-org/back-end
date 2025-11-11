package queuing.core.room.presentation.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import queuing.core.global.response.ResponseBody;
import queuing.core.room.application.model.CreateRoomCommand;
import queuing.core.room.application.model.GetListRoomCommand;
import queuing.core.room.application.model.GetListRoomResult;
import queuing.core.room.application.usecase.CreateRoomUseCase;
import queuing.core.room.application.usecase.GetListRoomUseCase;
import queuing.core.room.presentation.request.CreateRoomRequest;
import queuing.core.room.presentation.response.ListRoomResponse;
import queuing.core.room.presentation.response.MusicTagResponse;
import queuing.core.room.presentation.response.RoomSummaryResponse;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final CreateRoomUseCase createRoomUseCase;
    private final GetListRoomUseCase getListRoomUseCase;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateRoomRequest request) {
        String slug = createRoomUseCase.create(new CreateRoomCommand(request.title(), request.password(), request.tags()));
        return ResponseEntity.created(URI.create(slug)).build();
    }

    @GetMapping
    public ResponseEntity<ResponseBody<ListRoomResponse>> getList(
        @RequestParam(required = false) Long lastId,
        @RequestParam(defaultValue = "30") int size
    ) {
        GetListRoomResult getListRoomResult = getListRoomUseCase.getList(new GetListRoomCommand(lastId, size));
        List<RoomSummaryResponse> items = getListRoomResult.items().stream()
            .map(room -> new RoomSummaryResponse(
                room.id(),
                room.slug(),
                room.title(),
                room.isPrivate(),
                room.createdAt(),
                room.tags().stream()
                    .map(tag -> new MusicTagResponse(
                        tag.slug(),
                        tag.name()
                    )).toList()
            )).toList();

        return ResponseEntity.ok()
            .body(ResponseBody.success(new ListRoomResponse(items, getListRoomResult.hasNext())));
    }
}
