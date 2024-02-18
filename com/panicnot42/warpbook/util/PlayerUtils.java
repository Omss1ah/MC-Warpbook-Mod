package com.panicnot42.warpbook.util;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.net.packet.PacketSyncPlayers;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtils {
   private ArrayList<GameProfile> profiles = new ArrayList();
   private static PlayerUtils instance;

   public static PlayerUtils instance() {
      return instance == null ? (instance = new PlayerUtils()) : instance;
   }

   private PlayerUtils() {
   }

   public void updateClient(EntityPlayerMP player, ServerConnectionFromClientEvent e) {
      this.profiles.add(player.func_146103_bH());
      WarpWorldStorage.profiles.add(player.func_146103_bH());
      WarpWorldStorage.instance(player.field_70170_p).func_76185_a();
      FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel("warpbook", Side.SERVER);
      channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.DISPATCHER);
      channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(e.manager));
      channel.writeAndFlush(new PacketSyncPlayers(this.profiles)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
   }

   public void removeClient(EntityPlayerMP player) {
      this.profiles.remove(player.func_146103_bH());
   }

   public static EntityPlayer getPlayerByUUID(UUID uuid) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
         Iterator i$ = MinecraftServer.func_71276_C().func_71203_ab().field_72404_b.iterator();

         while(i$.hasNext()) {
            Object player = i$.next();
            if (((EntityPlayerMP)player).func_146103_bH().getId().equals(uuid)) {
               return (EntityPlayer)player;
            }
         }
      }

      return null;
   }

   public static boolean isPlayerOnline(UUID uuid) {
      Iterator i$ = instance().profiles.iterator();

      GameProfile profile;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         profile = (GameProfile)i$.next();
      } while(!profile.getId().equals(uuid));

      return true;
   }

   public static String getNameByUUID(UUID uuid) {
      Iterator i$ = WarpWorldStorage.profiles.iterator();

      GameProfile profile;
      do {
         if (!i$.hasNext()) {
            return "Unknown Name";
         }

         profile = (GameProfile)i$.next();
      } while(!profile.getId().equals(uuid));

      return profile.getName();
   }

   public void setProfiles(ArrayList<GameProfile> profiles) {
      this.profiles = profiles;
   }
}
