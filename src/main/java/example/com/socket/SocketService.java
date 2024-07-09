package example.com.socket;

import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.handler.SocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketService {

    @Value("${socket.server.url}")
    private String socketServerUrl;

    public void notifyChatroomEntered(Long memberId, String chatroomUuid) {
        RestTemplate rt = new RestTemplate();

        String url = socketServerUrl + "/notify/chatroom/enter";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("memberId", memberId);
        requestBody.put("chatroomUuid", chatroomUuid);

        try {
            ResponseEntity<String> response = rt.postForEntity(url, requestBody, String.class);

            log.info("response of notifyChatroomEntered: {}", response.getStatusCode().toString());
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new SocketHandler(ErrorStatus.SOCKET_API_RESPONSE_ERR);
            }
        } catch (Exception e) {
            log.error("Error occurred while notifyChatroomEntered method", e);
            throw new SocketHandler(ErrorStatus.SOCKET_API_RESPONSE_ERR);
        }
    }
}
