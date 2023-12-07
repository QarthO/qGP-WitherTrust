package gg.quartzdev.qgpwithertrust.util;

import com.google.common.base.Charsets;
import gg.quartzdev.qgpwithertrust.qGPWitherTrust;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.UUID;

public class WitherUtil {

    public static @Nullable String getCreatorID(Entity entity){
        NamespacedKey key = new NamespacedKey(qGPWitherTrust.getInstance(), "creator");
        return entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static void brandEntity(Entity entity, Player player){

        UUID creatorID = player.getUniqueId();

        if(!Bukkit.getOnlineMode())
            creatorID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));

        brandEntity(entity, creatorID.toString());
    }

    public static void brandEntity(Entity entity, String creatorID){
        NamespacedKey key = new NamespacedKey(qGPWitherTrust.getInstance(), "creator");
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, creatorID);
    }
}
