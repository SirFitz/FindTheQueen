package com.realdecoy.model;

import lombok.Data;

@Data
public class Choice {

    private String gameId;
    private Player player;
    private int value;
}
