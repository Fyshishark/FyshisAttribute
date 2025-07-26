package com.fyshishark.fyshisattribute.network;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.network.packet.C2SActivateAircrouch;
import com.fyshishark.fyshisattribute.network.packet.C2SAircrouchTick;
import com.fyshishark.fyshisattribute.network.packet.C2SConsumeAircrouch;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static SimpleChannel INSTANCE;
    private static int packetId;
    private static int id() {
        return packetId++;
    }
    
    public static void register() {
        INSTANCE = ChannelBuilder.named(new ResourceLocation(FyshisAttribute.MODID, "main"))
                .serverAcceptedVersions(version -> true)
                .clientAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> "1.0")
                .simpleChannel();
        
        INSTANCE.messageBuilder(C2SConsumeAircrouch.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SConsumeAircrouch::encode)
                .decoder(C2SConsumeAircrouch::new)
                .consumerMainThread(C2SConsumeAircrouch::handler)
                .add();

        INSTANCE.messageBuilder(C2SActivateAircrouch.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SActivateAircrouch::encode)
                .decoder(C2SActivateAircrouch::new)
                .consumerMainThread(C2SActivateAircrouch::handler)
                .add();

        INSTANCE.messageBuilder(C2SAircrouchTick.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SAircrouchTick::encode)
                .decoder(C2SAircrouchTick::new)
                .consumerMainThread(C2SAircrouchTick::handler)
                .add();
    }
    
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
