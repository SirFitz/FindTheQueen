package com.realdecoy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Round {
    private int QueenPosition;
    private int GuessPosition;
}
