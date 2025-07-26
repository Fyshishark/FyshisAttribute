package com.fyshishark.fyshisattribute.compat.parcool;

import com.alrex.parcool.api.unstable.action.ParCoolActionEvent;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.impl.*;
import com.fyshishark.fyshisattribute.util.AttributeEntity;
import com.fyshishark.fyshisattribute.util.AttributePlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParcoolEvent {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onParcoolAction(final ParCoolActionEvent event) {
        Player player = event.getPlayer();
        AttributeEntity ae = (AttributeEntity) player;
        AttributePlayer ap = (AttributePlayer) player;
        
        if(player instanceof AttributeEntity attribute) {
            Action action = event.getAction();
            if (action instanceof ClingToCliff
                    || action instanceof HorizontalWallRun
                    || action instanceof VerticalWallRun) {
                if(action.getNotDoingTick() == 1) {
                    ae.toggleAirJump(true);
                } else if (action.isDoing()) {
                    ae.toggleAirJump(false);
                }
                

            } else if (action instanceof ClimbUp
                    || action instanceof WallJump
                    || action instanceof JumpFromBar) {
                if(action.getDoingTick() == 1) {
                    ae.setJump(attribute.getMaxJumps() - 1);
                    ae.setJumpDelay(10);
                    ap.resetAircrouch();
                }
            }
        }
    }
}
