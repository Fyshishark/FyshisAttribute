package com.fyshishark.fyshisattribute.gui;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.util.AttributePlayer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class VignetteOverlay {
    private static final ResourceLocation OVERLAY = new ResourceLocation(FyshisAttribute.MODID, "textures/gui/vignette.png");
    private static float alpha;
    
    public static final IGuiOverlay VIGNETTE_OVERLAY = (gui, graphics, partialTick, screenWidth, screenHeight) -> {
        if(Minecraft.getInstance().player instanceof AttributePlayer p) {
            if(p.isFloating()) {
                gui.setupOverlayRenderState(true, false);
                renderScreenOverlay(OVERLAY, alpha, screenWidth, screenHeight);
                if(alpha < 1) {
                    alpha = Math.min(1, alpha + 0.005f);
                }
            } else {
                alpha = 0f;
            }
        }
    };
    
    public static void renderScreenOverlay(ResourceLocation resource, float alpha, int width, int height) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, resource);
        Tesselator ts = Tesselator.getInstance();
        BufferBuilder builder = ts.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(0.0, height, -90.0).uv(0.0F, 1.0F).endVertex();
        builder.vertex(width, height, -90.0).uv(1.0F, 1.0F).endVertex();
        builder.vertex(width, 0.0, -90.0).uv(1.0F, 0.0F).endVertex();
        builder.vertex(0.0, 0.0, -90.0).uv(0.0F, 0.0F).endVertex();
        ts.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
    }
}
