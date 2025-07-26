package com.fyshishark.fyshisattribute.network.packet;

import com.fyshishark.fyshisattribute.util.AttributePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class C2SActivateAircrouch {
    public C2SActivateAircrouch() { }
    public C2SActivateAircrouch(FriendlyByteBuf friendlyByteBuf) { }
    public void encode(FriendlyByteBuf friendlyByteBuf) { }
    
    public boolean handler(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if(player == null) return;
            AttributePlayer attribute = (AttributePlayer) player;
            attribute.activateAircrouch();
        });
        return true;
    }
    
}
