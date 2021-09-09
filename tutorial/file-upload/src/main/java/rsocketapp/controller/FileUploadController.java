package rsocketapp.controller;

import rsocketapp.model.Constants;
import rsocketapp.model.Status;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rsocketapp.service.FileUploadService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@Controller
public class FileUploadController {

    private FileUploadService service;

    @MessageMapping("file.upload")
    public Flux<Status> upload(@Headers Map<String, Object> metadata, @Payload Flux<DataBuffer> content) throws IOException {
        var fileName = metadata.get(Constants.FILE_NAME);
        var fileExtn = metadata.get(Constants.FILE_EXTN);
        var path = Paths.get(fileName + "." + fileExtn);
        return Flux.concat(service.uploadFile(path, content), Mono.just(Status.COMPLETED))
                .onErrorReturn(Status.FAILED);

    }

    FileUploadController(FileUploadService service) {
        this.service = service;
    }
}
