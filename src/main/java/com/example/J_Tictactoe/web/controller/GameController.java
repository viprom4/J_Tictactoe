package com.example.J_Tictactoe.web.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.service.GameService;
import com.example.J_Tictactoe.domain.service.UserService;
import com.example.J_Tictactoe.web.mapper.WebMapper;
import com.example.J_Tictactoe.web.model.BoardRequest;
import com.example.J_Tictactoe.web.model.GameResponse;
import com.example.J_Tictactoe.web.model.LeaderboardResponse;

@RestController 
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/history")
    public List<GameResponse> getHistory(@RequestAttribute("userId") UUID userId) {
        return gameService.getCompletedGames(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GameResponse getGame(@PathVariable UUID id) {
        return mapToResponse(gameService.getGame(id));
    }

    @PostMapping("/{id}")
    public GameResponse makeMove(@PathVariable UUID id, @RequestBody BoardRequest request) {
        GameBoard submitted = WebMapper.toDomain(request);
        Game game = gameService.processUserMove(id, submitted);
        return mapToResponse(game);
    }

    @PostMapping("/bot")
    public GameResponse createGameWithBot(@RequestAttribute("userId") UUID userId) {
        Game game = gameService.createGameWithBot(userId);
        return mapToResponse(game);
    }

    @PostMapping("/multiplayer")
    public GameResponse createMultiplayerGame(@RequestAttribute("userId") UUID userId) {
        Game game = gameService.createMultiplayerGame(userId);
        return mapToResponse(game);
    }

    @GetMapping("/available")
    public List<GameResponse> getAvailableGames() {
        return gameService.getAvailableGames().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
    }

    @PostMapping("/{id}/join")
    public GameResponse joinGame(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        Game game = gameService.joinGame(id, userId);
        return mapToResponse(game);
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardResponse> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        return gameService.getLeaderboard(limit);
    }

    @DeleteMapping("/{id}")
    public void cancelGame(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        gameService.cancelGame(id, userId);
    }

    private GameResponse mapToResponse(Game game) {
        GameResponse response = WebMapper.toResponse(game);
        
        if (game.getPlayer1() != null) {
            response.setPlayer1Login(userService.getUserById(game.getPlayer1()).getLogin());
        }
        
        if (game.getPlayer2() != null) {
            response.setPlayer2Login(userService.getUserById(game.getPlayer2()).getLogin());
        } else {
            response.setPlayer2Login("Компьютер");
        }
        
        return response;
    }
}
