package io.tglanz.chesslysis.backend.chesscom;

import java.time.Instant;

public class GameDTO {
    public String pgn;
    public String timeControl;
    public Instant endTime;
    public boolean rated;
    public String timeClass;
    public String eco;

    public GamePlayerDTO white;
    public GamePlayerDTO black;
}
