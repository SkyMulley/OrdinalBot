package uk.noxiousbot.NoxiousBot;

import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.sun.xml.internal.bind.v2.model.core.ID;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VoiceListener {
    private IDiscordClient client;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private PlayerManager pm;
    private Random rand = new Random();
    private IGuild guild = client.getGuildByID(464614503825276928L);
    public VoiceListener(IDiscordClient client, PlayerManager pm) {
        this.client = client;
        this.pm = pm;
        start();
    }
    private void start() {
        service.scheduleAtFixedRate(() -> {
            for(IUser user : client.getUsers()) {
                if(!user.isBot()) {
                    IVoiceState state = user.getVoiceStateForGuild(guild);
                    if(state != null) {
                        if (state.isSelfDeafened() || state.isDeafened() || state.isMuted() || state.isSelfMuted() || state.getChannel().getConnectedUsers().size() != 1) {
                            pm.rebuildPlayerLists();
                            HashMap<Long, Integer> players = pm.getPlayers();
                            int score = players.get(user.getLongID());
                            players.remove(user.getLongID());
                            players.put(user.getLongID(), score + rand.nextInt(10));
                            pm.setPlayers(players);
                        }
                    }
                }
            }
        }, 1,60, TimeUnit.SECONDS);
    }
}
