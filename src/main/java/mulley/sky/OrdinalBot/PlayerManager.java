package mulley.sky.OrdinalBot;

import discord4j.core.DiscordClient;
import discord4j.core.object.entity.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PlayerManager {
    private HashMap<Long, Integer> players = new HashMap<>();
    private Properties properties = new Properties();
    private File f = new File("players.properties");
    private DiscordClient cli;
    public PlayerManager(DiscordClient cli) {
        this.cli = cli;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            rebuildPlayerLists();
        } catch(Exception e) {e.printStackTrace();}
    }
    public HashMap<Long, Integer> getPlayers() {return players;}
    public void setPlayers(HashMap<Long, Integer> players) {this.players = players; saveDatabase();}
    public void rebuildPlayerLists() {
        try {
            properties.load(new FileInputStream(f));
            for (String key : properties.stringPropertyNames()) {
                players.put(Long.valueOf(key), Integer.parseInt(properties.get(key).toString()));
            }
            for (User user : cli.getUsers().collectList().block()) {
                if(!user.isBot()) {
                    if (!players.containsKey(user.getId().asLong())) {
                        System.err.println("User not in db");
                        players.put(user.getId().asLong(), 0);
                    }
                }
            }
            saveDatabase();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDatabase() {
        for(Map.Entry<Long,Integer> entry : players.entrySet()) {
            properties.put(entry.getKey().toString(), entry.getValue().toString());
        }
        try {
            properties.store(new FileOutputStream(f), null);
        } catch(Exception e) { e.printStackTrace();}
    }

    public void addCoins(User player, int coins) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        int score = players.get(getPlayer(player));
        players.remove(getPlayer(player));
        players.put(getPlayer(player), score + coins);
        setPlayers(players);
    }

    public void loseCoins(User player, int coins) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        int score = players.get(getPlayer(player));
        players.remove(getPlayer(player));
        players.put(getPlayer(player), score - coins);
        setPlayers(players);
    }

    public void blacklistPlayer(User player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        players.remove(getPlayer(player));
        players.put(getPlayer(player), -1);
        setPlayers(players);
    }

    public void unblacklistPlayer(User player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        players.remove(getPlayer(player));
        players.put(getPlayer(player), 0);
        setPlayers(players);
    }

    public boolean isBlacklisted(User player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        if(players.get(getPlayer(player)) == -1) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getCoins(User player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        return players.get(getPlayer(player));
    }

    private Long getPlayer(User user) { return user.getId().asLong();}
}
