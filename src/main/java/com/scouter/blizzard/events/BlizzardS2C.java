package com.scouter.blizzard.events;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlizzardS2C {
    private final boolean hasBlizzard;
    private final ChunkPos pos;
    private final int blizzardStrength;
    public BlizzardS2C(ChunkPos pos, BlizzardData data){
        this.blizzardStrength = data.getBlizzardStrength();
        this.pos = pos;
        this.hasBlizzard = data.isHasBlizzard();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(this.blizzardStrength);
        buf.writeChunkPos(this.pos);
        buf.writeBoolean(this.hasBlizzard);
    }

    public BlizzardS2C(FriendlyByteBuf buf){
        this.blizzardStrength = buf.readInt();
        this.pos = buf.readChunkPos();
        this.hasBlizzard = buf.readBoolean();
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlizzardClientData.setData(pos, new BlizzardData(blizzardStrength, hasBlizzard));
        });
        return true;
    }

}
