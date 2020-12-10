package stubailey18.diceblackjackback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {

    // each session is mapped to a player ID on joining the game
    // this enables the removing of the player from the game when the session is removed
    private Map<WebSocketSession, Integer> sessions = new ConcurrentHashMap<>();

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
            int playerId = game.addPlayer(playerName);
            sessions.put(webSocketSession, playerId);
            webSocketSession.sendMessage(new TextMessage(String.format("playerId::%s", playerId)));
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

        var sessionsToBeRemoved = new LinkedList<WebSocketSession>();
        sessions.keySet().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(String.format("game::%s", mapper.writeValueAsString(game))));
            } catch (Exception e) {

                // the player has refreshed/navigated away/closed the browser etc.
                // remove him/her from the game and remove the session from the map of sessions
                int playerId = sessions.get(session);
                game.removePlayer(playerId);
                sessionsToBeRemoved.add(session);
            }
        });
        sessionsToBeRemoved.forEach(session -> sessions.remove(session));
        sessionsToBeRemoved.clear();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session, 0);
    }
}
