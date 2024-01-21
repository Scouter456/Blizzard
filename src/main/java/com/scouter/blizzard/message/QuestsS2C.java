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

public class QuestsS2C {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Codec<Map<ResourceLocation, Quests>> QUESTS_MAPPER = Codec.unboundedMap(ResourceLocation.CODEC,  Quests.CODEC)
            .xmap(Map::copyOf, Map::copyOf)
            .orElse(e -> {LOGGER.error("Failed to parse Quest Entries can't send packet! Due to " + e);},
                    new HashMap<>());
    protected static Map<ResourceLocation, Quests> SYNCED_DATA = new HashMap<>();
    private final Map<ResourceLocation, Quests> map;

    public QuestsS2C(Map<ResourceLocation,  Quests> map) {
        this.map = map;
    }

    public void encode(FriendlyByteBuf buffer) {
        CompoundTag encodedTag = (CompoundTag) (QUESTS_MAPPER.encodeStart(NbtOps.INSTANCE, this.map).result().orElse(new CompoundTag()));
        buffer.writeNbt(encodedTag);
    }

    public static QuestsS2C decode(FriendlyByteBuf buffer) {
        CompoundTag receivedTag = buffer.readNbt();
        Map<ResourceLocation, Quests> decodedMap = QUESTS_MAPPER.parse(NbtOps.INSTANCE, receivedTag).result().orElse(new HashMap<>());
        return new QuestsS2C(decodedMap);
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        SYNCED_DATA = this.map;
        QuestManager.setQuests(SYNCED_DATA);
    }
}


