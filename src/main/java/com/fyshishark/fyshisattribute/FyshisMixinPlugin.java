package com.fyshishark.fyshisattribute;

import com.fyshishark.fyshisattribute.compat.CompatibilityMod;
import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class FyshisMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Map<String, Supplier<Boolean>>
        PARCOOL = ImmutableMap.of("com.fyshishark.fyshisattribute.mixin.compat.parcool.*", () -> CompatibilityMod.isInstalled("parcool"));
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return PARCOOL.getOrDefault(mixinClassName, TRUE).get();
    }
    
    @Override public void onLoad(String s) { }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> set, Set<String> set1) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) { }
    @Override public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) { }
}
