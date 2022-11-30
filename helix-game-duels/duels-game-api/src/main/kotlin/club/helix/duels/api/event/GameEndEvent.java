package club.helix.duels.api.event;

import club.helix.components.account.HelixUser;
import club.helix.duels.api.game.DuelsGame;
import club.helix.duels.api.game.DuelsPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent <T extends DuelsGame> extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final DuelsPlayer winner, loser;
    private final HelixUser winnerAccount, loserAccount;
    private final T game;

    public GameEndEvent(DuelsPlayer winner, DuelsPlayer loser, HelixUser winnerAccount, HelixUser loserAccount, T game) {
        this.winner = winner;
        this.loser = loser;
        this.winnerAccount = winnerAccount;
        this.loserAccount = loserAccount;
        this.game = game;
    }

    public DuelsPlayer getWinner() {
        return winner;
    }

    public HelixUser getWinnerAccount() {
        return winnerAccount;
    }

    public HelixUser getLoserAccount() {
        return loserAccount;
    }

    public DuelsPlayer getLoser() {
        return loser;
    }

    public T getGame() {
        return game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
