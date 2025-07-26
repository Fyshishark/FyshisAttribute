package com.fyshishark.fyshisattribute.registry;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.Keys.SOUND_EVENTS, FyshisAttribute.MODID);
    public static final RegistryObject<SoundEvent>
            MULTIJUMP = add("multijump"),
            AIRCROUCH_START = add("aircrouch_start"),
            AIRCROUCH_LOOP = add("aircrouch_loop"),
            AIRCROUCH_STOP = add("aircrouch_stop"),
            AIRCROUCH_CANCEL = add("aircrouch_cancel"),
            AIRCROUCH_INTERRUPTED = add("aircrouch_interrupt");
    
    public static RegistryObject<SoundEvent> add(String id) {
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FyshisAttribute.MODID, id)));
    }
}
