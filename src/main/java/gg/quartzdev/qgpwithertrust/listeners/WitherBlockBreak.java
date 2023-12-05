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
    @EventHandler
    public void onWitherSkullExplode(EntityExplodeEvent event){
        if((event.getEntity() instanceof WitherSkull) || event.getEntity() instanceof Wither)
                handleWitherExplosions(event);
    }

    public void handleWitherExplosions(EntityExplodeEvent event){
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
//                   if you're not trusted with build or higher
                    if(claimPermission == null) continue;
                    if(claimPermission.equals(ClaimPermission.Access)) continue;
                    if(claimPermission.equals(ClaimPermission.Inventory)) continue;
                }

            breakBlock(block, event.getYield());
        }
    }

    public void breakBlock(Block block, float yield){

//        RNG
        ThreadLocalRandom random = ThreadLocalRandom.current();
        float rng = random.nextFloat(0.01F, 100.0F);

//        rng check
//        - QOL: will always drop any blocks that have an inventory, like a chest
        if(rng > yield && !(block.getState() instanceof InventoryHolder))
//            sets to air, doesn't drop the block
            block.setType(Material.AIR);
        else
//            breaks and drops the block
            block.breakNaturally();
    }
}
