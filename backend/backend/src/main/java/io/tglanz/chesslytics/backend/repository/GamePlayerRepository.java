package io.tglanz.chesslytics.backend.repository;

import io.tglanz.chesslytics.backend.model.GamePlayer;
import io.tglanz.chesslytics.backend.model.GamePlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, GamePlayerId> {}
