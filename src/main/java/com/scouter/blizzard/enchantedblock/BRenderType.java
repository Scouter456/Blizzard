package com.scouter.blizzard.enchantedblock;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class BRenderType extends RenderType{

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_CUTOUT_GLINT = new RenderStateShard.ShaderStateShard(BInternalShaders::getEnchantedCutoutGlint);


    private static final RenderType GLINT = create("glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false))
                            .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));

    private static final RenderType CUTOUT = create("cutout", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 131072, true, false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_CUTOUT_SHADER)
                    .setTextureState(BLOCK_SHEET)
                    .createCompositeState(true));


    public static final RenderType GLINT_CUTOUT_TEST = create("glint_cutout", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, true, true,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setShaderState(RENDERTYPE_CUTOUT_GLINT)
                    .setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false))
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setTexturingState(GLINT_TEXTURING)
                    .setCullState(NO_CULL)
                    .createCompositeState(true));



    public static RenderType getGlintCutout() {
        return GLINT_CUTOUT_TEST;
    }


    public static RenderType getGlintCutout(ResourceLocation texture) {
        return create("glint_cutout", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 131072, true, false,
                RenderType.CompositeState.builder()
                        .setLightmapState(LIGHTMAP)
                        .setWriteMaskState(COLOR_WRITE)
                        .setShaderState(RENDERTYPE_CUTOUT_GLINT)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setTransparencyState(GLINT_TRANSPARENCY)
                        .setTexturingState(GLINT_TEXTURING)
                        .createCompositeState(false));
    }

    public BRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }
}
