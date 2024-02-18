package com.panicnot42.warpbook.net.packet;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.util.PlayerUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class PacketSyncPlayers implements IMessage, IMessageHandler<PacketSyncPlayers, IMessage> {
   ArrayList<GameProfile> profiles = new ArrayList();

   public PacketSyncPlayers() {
   }

   public PacketSyncPlayers(ArrayList<GameProfile> profiles) {
      this.profiles = profiles;
   }

   public void fromBytes(ByteBuf buf) {
      while(buf.readableBytes() != 0) {
         UUID uuid = new UUID(buf.readLong(), buf.readLong());
         byte[] bytes = new byte[buf.readInt()];

         for(int i = 0; i < bytes.length; ++i) {
            bytes[i] = buf.readByte();
         }

         this.profiles.add(new GameProfile(uuid, new String(bytes)));
      }

   }

   public void toBytes(ByteBuf buf) {
      Iterator i$ = this.profiles.iterator();

      while(i$.hasNext()) {
         GameProfile profile = (GameProfile)i$.next();
         buf.writeLong(profile.getId().getMostSignificantBits());
         buf.writeLong(profile.getId().getLeastSignificantBits());
         byte[] bytes = profile.getName().getBytes();
         buf.writeInt(bytes.length);
         buf.writeBytes(bytes);
      }

   }

   public IMessage onMessage(PacketSyncPlayers message, MessageContext ctx) {
      PlayerUtils.instance().setProfiles(message.profiles);
      return null;
   }
}
