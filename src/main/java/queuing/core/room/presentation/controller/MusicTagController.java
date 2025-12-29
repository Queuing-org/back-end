package queuing.core.room.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import queuing.core.global.response.ResponseBody;
import queuing.core.room.application.usecase.GetListMusicTagUseCase;
import queuing.core.room.presentation.response.ListMusicTagResponse;
import queuing.core.room.presentation.response.MusicTagResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class MusicTagController {
    private final GetListMusicTagUseCase getListMusicTagUseCase;

    @GetMapping
    public ResponseEntity<ResponseBody<ListMusicTagResponse>> getList() {
        List<MusicTagResponse> items = getListMusicTagUseCase.getList().stream()
            .map(MusicTagResponse::from)
            .toList();

        ListMusicTagResponse content = new ListMusicTagResponse(items);

        return ResponseEntity.ok().body(ResponseBody.success(content));
    }
}
