package com.realdecoy.service;

import com.realdecoy.storage.GameStorage;
import lombok.AllArgsConstructor;
import com.realdecoy.model.Game;
import com.realdecoy.model.Player;
import com.realdecoy.model.Choice;
import com.realdecoy.model.Round;
import org.springframework.stereotype.Service;
import com.realdecoy.exception.GameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(Player player) throws GameException  {
        Game newGame = new Game();
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> !it.getStatus().equals(""))
                .findFirst().orElse(newGame);

        if(game.getGameId().length() == 0) {
            game.setGameId(UUID.randomUUID().toString());
        }

        boolean validUser = true;
        if(player.getUsername() == "dannyboi" && player.getPassword() == "dre@margh_shelled"){
            validUser = true;
        }
        if(player.getUsername() == "matty7" && player.getPassword() == "win&win99"){
            validUser = true;
        }

        if(validUser == true){
            if(game.getPlayer1().getUsername().length() != 0){
                connectToGame(player, game.getGameId());
            } else {
                game.setPlayer1(player);
                game.setStatus("started");
                GameStorage.getInstance().setGame(game);
            }
        } else {
            game.setStatus("invalid login");
        }

        return game;
    };

    public Game connectToGame(Player player2, String gameId) throws GameException {
        Game game = checkGameValidity(gameId);

        if (game.getPlayer2() != null) {
            throw new GameException("Game is not valid anymore");
        }

        List<String> roles = new ArrayList<>();
        roles.add("dealer");
        roles.add("spotter");

        Random randomizer = new Random();
        String role = roles.get(randomizer.nextInt(roles.size()));

        player2.setType(role);

        Player player1 = game.getPlayer1();
        if(role == "dealer"){
            player1.setType("spotter");
        } else {
            player1.setType("dealer");
        }

        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setStatus("in-progress");
        GameStorage.getInstance().setGame(game);

        return game;
    }

    public Game gameChoice(Choice gameChoice) throws GameException {
        Game game = checkGameValidity(gameChoice.getGameId());

        //List<Round> rounds = Arrays.asList(game.getRounds());
        Round [] rounds = game.getRounds();
        //Round latestRound = rounds.get(rounds.size() - 1);
        int round_num = game.getRound();
        Round currentRound = rounds[round_num];

        Player currentPlayer = gameChoice.getPlayer();

        if(currentPlayer.getType() == "dealer" && currentRound.getQueenPosition() != 0){
            return game;
        }

        if(currentPlayer.getType() == "spotter" && currentRound.getGuessPosition() != 0){
            return game;
        }

        if(currentPlayer.getType() == "dealer") {
            currentRound.setQueenPosition(gameChoice.getValue());
        } else {
            currentRound.setGuessPosition(gameChoice.getValue());
        }

        //currentRound.getQueenPosition != null && currentRound.getGuessPosition != null
        boolean match = false;
        if(currentPlayer.getType() == "spotter"){
            match = currentRound.getQueenPosition() == currentRound.getGuessPosition();
            int [] winners = game.getWinners();

            if(match == true){
                winners[round_num] = 2;
            } else {
                winners[round_num] = 1;
            }

            game.setWinners(winners);

            game.setRound(round_num + 1);
        }


        if(round_num == 5){
            game.setStatus("complete");
        } else {
            game.setStatus("in-progress");
            Round newRound = new Round(0, 0 );
            int newRoundNum = game.getRound();
            rounds[newRoundNum] = newRound;
            game.setRounds(rounds);
        }

        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game checkGameValidity(String gameId) throws GameException {
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game == null) {
            throw new GameException("Game not found");
        }

        if (game.getPlayer2() != null) {
            throw new GameException("Game is not valid anymore");
        }

        if (game.getStatus().equals("complete")) {
            throw new GameException("Game is already finished");
        }

        return game;
    }

}


