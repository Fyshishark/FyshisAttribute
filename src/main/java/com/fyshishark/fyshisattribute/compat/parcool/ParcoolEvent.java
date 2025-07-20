package com.fyshishark.fyshisattribute.compat.parcool;

import com.alrex.parcool.api.unstable.action.ParCoolActionEvent;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.impl.ClimbUp;
import com.alrex.parcool.common.action.impl.ClingToCliff;
import com.alrex.parcool.common.action.impl.JumpFromBar;
import com.alrex.parcool.common.action.impl.WallJump;
import com.fyshishark.fyshisattribute.util.LivingEntityFyshiAttribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParcoolEvent {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onParcoolAction(final ParCoolActionEvent event) {
        if(event.getPlayer() instanceof LivingEntityFyshiAttribute player) {
            Action action = event.getAction();
            if (event.getAction() instanceof ClingToCliff clinging) {
                player.toggleAirJump(clinging.getDoingTick() <= 0);
            } else if (action instanceof ClimbUp
                    || action instanceof WallJump
                    || action instanceof JumpFromBar) {
                if(action.getDoingTick() == 1) {
                    player.setJump(player.getMaxJumps() - 1);
                    player.setJumpDelay(10);
                }
            }
        }
    }
}
