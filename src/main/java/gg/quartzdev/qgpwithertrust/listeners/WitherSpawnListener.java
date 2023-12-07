package gg.quartzdev.qgpwithertrust.listeners;

import gg.quartzdev.qgpwithertrust.util.WitherUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import javax.annotation.Nullable;
import java.util.*;


public class WitherSpawnListener implements Listener {

    final double PLAYER_REACH = 4.5;

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event){

        Entity wither = event.getEntity();

        if(!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_WITHER)) return;

        Location witherLocation = event.getLocation();
        Player witherCreator = this.getClosestPlayer(witherLocation, PLAYER_REACH);

//        if created via redstone ignore
        if(witherCreator == null) return;

//        puts nbt data on wither saying who spawned it, uses PDC
        WitherUtil.brandEntity(wither, witherCreator);

    }

    public @Nullable Player getClosestPlayer(Location location, double distance){
        Collection<Player> nearbyPlayers = location.getNearbyPlayers(distance);
        if(nearbyPlayers.isEmpty()) return null;
        return Collections.min(nearbyPlayers,
                Comparator.comparingDouble(nearbyPlayer -> nearbyPlayer.getLocation().distanceSquared(location))
        );

    }

}
