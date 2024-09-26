package com.scouter.blizzard.mixins;

import com.scouter.blizzard.events.BlizzardWeatherRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class BlizzardMixin {
    // [VanillaCopy] inside of LevelRenderer.renderRainSnow, edits noted
    @Inject(method = "renderSnowAndRain", at = @At(value = "TAIL"))
    public void Blizzard$renderSnowAndRain(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo ci) {
        LevelRenderer levelRenderer = (LevelRenderer) (Object) this;
        BlizzardWeatherRenderer.renderSnowAndRain(levelRenderer, pLightTexture, pPartialTick, pCamX, pCamY, pCamZ);
    }

    @Inject(method = "tickRain", at = @At(value = "TAIL"))
    public void Blizzard$renderSnowParticleAndPlaySound(Camera camera, CallbackInfo ci) {
        LevelRenderer levelRenderer = (LevelRenderer) (Object) this;
        BlizzardWeatherRenderer.renderSnowParticleAndPlaySound(levelRenderer, camera);
    }
}
