package recordrequests;

import model.GameData;

import java.util.List;

public record HoldsListGames(List<GameData> games) {

    public List<GameData> returnList(){
        return games;
    }
}
