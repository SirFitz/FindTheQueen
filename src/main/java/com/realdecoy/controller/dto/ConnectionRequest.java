package com.realdecoy.controller.dto;

import com.realdecoy.model.Player;
import lombok.Data;

@Data
public class ConnectionRequest {
    private Player player;
    private String gameId;
}
