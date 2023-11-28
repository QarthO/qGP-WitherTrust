package gg.quartzdev.qgpwithertrust.listeners;

import gg.quartzdev.qgpwithertrust.qGPWitherTrust;
import gg.quartzdev.qgpwithertrust.util.WitherUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.InventoryHolder;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WitherBlockBreak implements Listener {

    qGPWitherTrust plugin;

    public WitherBlockBreak(){
        this.plugin = qGPWitherTrust.getInstance();
    }

//    Detects when a wither fires a skull and brands the skull with the creator
    @EventHandler
    public void onWitherFiresSkull(ProjectileLaunchEvent event){
        Projectile witherSkull = event.getEntity();
        if(witherSkull.getShooter() == null || !(witherSkull.getShooter() instanceof Wither)) return;
        Wither wither = (Wither) witherSkull.getShooter();
        String creatorId = WitherUtil.getCreatorID(wither);
        if(creatorId == null) return;
        WitherUtil.brandEntity(witherSkull, creatorId);
    }
    @EventHandler (priority = EventPriority.LOWEST)
    public void onWitherSkullExplode(EntityExplodeEvent event){
        if(!(event.getEntity() instanceof WitherSkull)) return;

        String creatorId = WitherUtil.getCreatorID(event.getEntity());
        if(creatorId == null) return;
        List<Block> blocks = event.blockList();
        if(blocks.isEmpty()) return;
        Claim cachedClaim = null;
        for(Block block : blocks){
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, cachedClaim);
//            If you're in a claim
            if(claim != null)
//               and the claim isn't yours
                if(!claim.getOwnerID().toString().equals(creatorId)){
                    cachedClaim = claim;
                    ClaimPermission claimPermission = cachedClaim.getPermission(creatorId);
//                    and you aren't trusted
                    if(claimPermission == null)
//                        then don't let the wither break the block
                        continue;
            }

//            RNG to check if item should drop
//            Not every block destroyed by with will drop, this uses the same odds
            ThreadLocalRandom random = ThreadLocalRandom.current();
            float rng = random.nextFloat(0.01F, 100.0F);

            if(rng > event.getYield() || !(block.getState() instanceof InventoryHolder)) {
                block.setType(Material.AIR);
            } else
                block.breakNaturally();
        }
    }
}
