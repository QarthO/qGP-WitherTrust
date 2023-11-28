package gg.quartzdev.qgpwithertrust.util;

import gg.quartzdev.qgpwithertrust.qGPWitherTrust;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

public class WitherUtil {

    public static @Nullable String getCreatorID(Entity entity){
        NamespacedKey key = new NamespacedKey(qGPWitherTrust.getInstance(), "creator");
        return entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static void brandEntity(Entity entity, String creatorID){
        NamespacedKey key = new NamespacedKey(qGPWitherTrust.getInstance(), "creator");
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, creatorID);
    }
}
