package gg.quartzdev.qgpwithertrust.listeners;

import gg.quartzdev.qgpwithertrust.util.WitherUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WitherHurtEntity implements Listener {


    @EventHandler ()
    public void onWitherHurtEntity(EntityDamageByEntityEvent event){

        if(!(event.getDamager() instanceof Wither) && !(event.getDamager() instanceof WitherSkull)) return;

        String creatorId = WitherUtil.getCreatorID(event.getEntity());
        if(creatorId == null) return;

        Claim cachedClaim = null;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getEntity().getLocation(), false, cachedClaim);
//            Ignore if not in a claim
        if(claim == null) return;

        if(!claim.getOwnerID().toString().equals(creatorId)){
            cachedClaim = claim;
            ClaimPermission claimPermission = cachedClaim.getPermission(creatorId);
//                   if you're not trusted with build or higher
            if(claimPermission == null) return;
            if(claimPermission.equals(ClaimPermission.Access)) return;
            if(claimPermission.equals(ClaimPermission.Inventory)) return;
        }

        event.setCancelled(true);
    }

}
