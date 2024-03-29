package mulley.sky.OrdinalBot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;

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
    private IDiscordClient cli;
    public PlayerManager(IDiscordClient cli) {
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
            for (IUser user : cli.getUsers()) {
                if(!user.isBot()) {
                    if (!players.containsKey(user.getLongID())) {
                        System.err.println("User not in db");
                        players.put(user.getLongID(), 0);
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

    public void addCoins(IUser player, int coins) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        int score = players.get(getPlayer(player));
        players.remove(getPlayer(player));
        players.put(getPlayer(player), score + coins);
        setPlayers(players);
    }

    public void loseCoins(IUser player, int coins) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        int score = players.get(getPlayer(player));
        players.remove(getPlayer(player));
        players.put(getPlayer(player), score - coins);
        setPlayers(players);
    }

    public void blacklistPlayer(IUser player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        players.remove(getPlayer(player));
        players.put(getPlayer(player), -1);
        setPlayers(players);
    }

    public void unblacklistPlayer(IUser player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        players.remove(getPlayer(player));
        players.put(getPlayer(player), 0);
        setPlayers(players);
    }

    public boolean isBlacklisted(IUser player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        if(players.get(getPlayer(player)) == -1) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getCoins(IUser player) {
        rebuildPlayerLists();
        HashMap<Long, Integer> players = getPlayers();
        return players.get(getPlayer(player));
    }

    private Long getPlayer(IUser user) { return user.getLongID();}
}
