package stubailey18.diceblackjackback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Game game = new Game();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {

        /*
         * messages:
         * - join:<playerName>
         * - hit:<playerId>
         * - stand:<playerId>
         * - reset
         * each message should result in the sending of the updated Game to all players
         */

        var tokenizer = new StringTokenizer(message.getPayload(), ":");
        var operation = tokenizer.nextToken();
        if (operation.equalsIgnoreCase("join")) {
            var playerName = tokenizer.nextToken();
            game.addPlayer(playerName);
        }
        if (operation.equalsIgnoreCase("hit")) {
            var playerId = Integer.parseInt(tokenizer.nextToken());
            game.hit(playerId);
        }
        if (operation.equalsIgnoreCase("stand")) {
            var playerId = Integer.parseInt(tokenizer.nextToken());
            game.stand(playerId);
        }
        if (operation.equalsIgnoreCase("reset")) {
            game.reset();
        }
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(mapper.writeValueAsString(game)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}
