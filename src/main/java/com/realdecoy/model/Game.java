package com.realdecoy.model;

import lombok.Data;

@Data
public class Game {

    private String gameId;
    private Player player1;
    private Player player2;
    private String status;
    private Round [] rounds;
    private int round;
    private int [] winners;
}
