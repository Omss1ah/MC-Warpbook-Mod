package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.util.net.NetUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PacketEffect implements IMessage, IMessageHandler<PacketEffect, IMessage> {
   boolean enter;
   int x;
   int y;
   int z;

   public PacketEffect() {
   }

   public PacketEffect(boolean enter, int x, int y, int z) {
      this.enter = enter;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public IMessage onMessage(PacketEffect message, MessageContext ctx) {
      EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
      int particles = (2 - Minecraft.func_71410_x().field_71474_y.field_74362_aa) * 50;
      int i;
      if (message.enter) {
         for(i = 0; i < 5 * particles; ++i) {
            player.field_70170_p.func_72869_a("largesmoke", (double)message.x, (double)message.y + player.field_70170_p.field_73012_v.nextDouble() * 2.0D, (double)message.z, player.field_70170_p.field_73012_v.nextDouble() / 10.0D - 0.05D, 0.0D, player.field_70170_p.field_73012_v.nextDouble() / 10.0D - 0.05D);
         }
      } else {
         for(i = 0; i < particles; ++i) {
            player.field_70170_p.func_72869_a("portal", (double)message.x - 0.5D, (double)message.y + player.field_70170_p.field_73012_v.nextDouble() * 2.0D, (double)message.z - 0.5D, player.field_70170_p.field_73012_v.nextDouble() - 0.5D, player.field_70170_p.field_73012_v.nextDouble() - 0.5D, player.field_70170_p.field_73012_v.nextDouble() - 0.5D);
         }
      }

      return null;
   }

   public void fromBytes(ByteBuf buffer) {
      this.enter = buffer.readBoolean();
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.enter);
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
   }
}
