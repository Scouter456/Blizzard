package com.scouter.blizzard.events;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class BlizzardS2C {
    private final boolean hasBlizzard;
    private final ChunkPos pos;
    private final int blizzardStrength;
    private final ResourceKey<Level> levelResourceKey;
    public BlizzardS2C(ChunkPos pos, BlizzardData data, ResourceKey<Level> levelResourceKey){
        this.blizzardStrength = data.getBlizzardStrength();
        this.pos = pos;
        this.hasBlizzard = data.isHasBlizzard();
        this.levelResourceKey = levelResourceKey;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(this.blizzardStrength);
        buf.writeChunkPos(this.pos);
        buf.writeBoolean(this.hasBlizzard);
        StringTag encodedTag = (StringTag) (Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.levelResourceKey).result().orElse(new CompoundTag()));
        //ResourceLocation resourceLocation = this.levelResourceKey.registry();
        //buf.writeWithCodec(NbtOps.INSTANCE, Codec.STRING, resourceLocation.toString());
        CompoundTag tag = new CompoundTag();
        tag.put("level", encodedTag);
        buf.writeNbt(tag);
    }

    public BlizzardS2C(FriendlyByteBuf buf){
        this.blizzardStrength = buf.readInt();
        this.pos = buf.readChunkPos();
        this.hasBlizzard = buf.readBoolean();
        //String rl = buf.readWithCodec(NbtOps.INSTANCE, Codec.STRING);
        //this.levelResourceKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(rl));
        this.levelResourceKey =  Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, buf.readNbt().get("level")).result().orElse(null);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlizzardClientData.setData(this.levelResourceKey,pos, new BlizzardData(blizzardStrength, hasBlizzard, pos));
            context.setPacketHandled(true);
        });
        return true;
    }

}
