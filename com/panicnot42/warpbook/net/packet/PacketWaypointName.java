package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.util.net.NetUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PacketWaypointName implements IMessage, IMessageHandler<PacketWaypointName, IMessage> {
   String name;

   public PacketWaypointName() {
   }

   public PacketWaypointName(String name) {
      this.name = name;
   }

   public IMessage onMessage(PacketWaypointName message, MessageContext ctx) {
      EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
      --player.func_70694_bm().field_77994_a;
      if (player.func_70694_bm().field_77994_a == 0) {
         player.field_71071_by.func_70299_a(player.field_71071_by.field_70461_c, (ItemStack)null);
      }

      ItemStack newPage = (ItemStack)WarpBookMod.formingPages.get(player);
      newPage.func_77978_p().func_74778_a("name", message.name);
      EntityItem item = new EntityItem(player.field_70170_p, player.field_70165_t, player.field_70163_u, player.field_70161_v, newPage);
      player.field_70170_p.func_72838_d(item);
      return null;
   }

   public void fromBytes(ByteBuf buffer) {
      byte[] data = new byte[buffer.readableBytes()];
      buffer.readBytes(data);
      this.name = new String(data);
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBytes(this.name.getBytes());
   }
}
