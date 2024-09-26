package com.scouter.blizzard.enchantedblock;

import net.minecraft.client.renderer.ShaderInstance;

import javax.annotation.Nullable;

public class BInternalShaders {
    private static ShaderInstance enchantedCutoutGlint;

    @Nullable
    public static ShaderInstance getEnchantedCutoutGlint() {
        return enchantedCutoutGlint;
    }

    public static void setEnchantedCutoutGlint(ShaderInstance instance) {
        enchantedCutoutGlint = instance;
    }
}
