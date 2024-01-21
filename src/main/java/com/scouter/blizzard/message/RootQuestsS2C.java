package com.scouter.blizzard.message;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.scouter.blizzard.codec.QuestManager;
import com.scouter.blizzard.codec.Quests;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RootQuestsS2C {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Codec<Map<ResourceLocation, Map<ResourceLocation, Quests>>> ROOT_QUESTS_MAPPER = Codec.unboundedMap(ResourceLocation.CODEC, Codec.unboundedMap(ResourceLocation.CODEC, Quests.CODEC))
            .xmap(Map::copyOf, Map::copyOf)
            .orElse(e -> {LOGGER.error("Failed to parse Root Quest Entries can't send packet! Due to " + e);},
                    new HashMap<>());
    protected static Map<ResourceLocation, Map<ResourceLocation, Quests>> SYNCED_DATA = new HashMap<>();
    private final Map<ResourceLocation, Map<ResourceLocation, Quests>> map;

    public RootQuestsS2C(Map<ResourceLocation, Map<ResourceLocation, Quests>> map) {
        this.map = map;
    }

    public void encode(FriendlyByteBuf buffer) {
        CompoundTag encodedTag = (CompoundTag) (ROOT_QUESTS_MAPPER.encodeStart(NbtOps.INSTANCE, this.map).result().orElse(new CompoundTag()));
        buffer.writeNbt(encodedTag);
    }

    public static RootQuestsS2C decode(FriendlyByteBuf buffer) {
        CompoundTag receivedTag = buffer.readNbt();
        Map<ResourceLocation, Map<ResourceLocation, Quests>> decodedMap = ROOT_QUESTS_MAPPER.parse(NbtOps.INSTANCE, receivedTag).result().orElse(new HashMap<>());
        return new RootQuestsS2C(decodedMap);
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        SYNCED_DATA = this.map;
        QuestManager.setRootQuests(SYNCED_DATA);
    }
}


