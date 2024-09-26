package com.scouter.blizzard.mixins;

import com.scouter.blizzard.events.BlizzardServerLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "tickChunk", at = @At(value = "TAIL"))
    public void Blizzard$spawnSnowAndIce(LevelChunk pChunk, int pRandomTickSpeed, CallbackInfo ci) {
        ServerLevel serverLevel = (ServerLevel) (Object) this;
        BlizzardServerLevel.spawnSnowAndIce(serverLevel, pChunk, pRandomTickSpeed);
    }
}
