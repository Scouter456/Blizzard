package com.scouter.blizzard.events;

import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.message.QuestsS2C;
import com.scouter.blizzard.message.RootQuestsS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class BMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Blizzard.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(BlizzardS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BlizzardS2C::new)
                .encoder(BlizzardS2C::toBytes)
                .consumerMainThread(BlizzardS2C::handle)
                .add();

        net.registerMessage(id(), QuestsS2C.class,
                QuestsS2C::encode,
                QuestsS2C::decode,
                QuestsS2C::onPacketReceived);

        net.registerMessage(id(), RootQuestsS2C.class,
                RootQuestsS2C::encode,
                RootQuestsS2C::decode,
                RootQuestsS2C::onPacketReceived);
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }


    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}

