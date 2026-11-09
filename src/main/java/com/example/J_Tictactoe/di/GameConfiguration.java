package com.example.J_Tictactoe.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.J_Tictactoe.datasource.repository.GameRepository;
import com.example.J_Tictactoe.datasource.repository.GameRepositoryImpl;
import com.example.J_Tictactoe.datasource.repository.JpaGameRepository;
import com.example.J_Tictactoe.datasource.repository.JpaUserRepository;
import com.example.J_Tictactoe.datasource.repository.UserRepository;
import com.example.J_Tictactoe.datasource.repository.UserRepositoryImpl;
import com.example.J_Tictactoe.domain.service.GameService;
import com.example.J_Tictactoe.domain.service.GameServiceImpl;
import com.example.J_Tictactoe.domain.service.MinimaxBotService;
import com.example.J_Tictactoe.domain.service.MinimaxBotServiceImpl;
import com.example.J_Tictactoe.domain.service.UserService;
import com.example.J_Tictactoe.domain.service.UserServiceImpl;
import com.example.J_Tictactoe.web.security.JwtProvider;

@Configuration
public class GameConfiguration {
    @Bean
    public GameRepository gameRepository(JpaGameRepository jpaGameRepository ) {
        return new GameRepositoryImpl(jpaGameRepository);
    }

    @Bean
    public MinimaxBotService minimaxBotService() {
        return new MinimaxBotServiceImpl();
    }

    @Bean
    public GameService gameService(GameRepository repository, MinimaxBotService bot, UserService userService) {
        return new GameServiceImpl(repository, bot, userService);
    }

    @Bean 
    public UserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new UserRepositoryImpl(jpaUserRepository);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

    @Bean
    public UserService userService(UserRepository userRepository, JwtProvider jwtProvider) {
        return new UserServiceImpl(userRepository, jwtProvider);
    }
}