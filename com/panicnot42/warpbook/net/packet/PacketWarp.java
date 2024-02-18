package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.util.net.NetUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class PacketWarp implements IMessage, IMessageHandler<PacketWarp, IMessage> {
   int pageSlot;

   public PacketWarp() {
   }

   public PacketWarp(int pageSlot) {
      this.pageSlot = pageSlot;
   }

   public static ItemStack getPageById(EntityPlayer player, int pageSlot) {
      try {
         if (WarpBookMod.fuelEnabled) {
            if (WarpBookItem.getFuelLeft((ItemStack)WarpBookMod.lastHeldBooks.get(player)) <= 0) {
               return null;
            }

            WarpBookItem.decrFuelLeft((ItemStack)WarpBookMod.lastHeldBooks.get(player));
         }

         NBTTagList stack = ((ItemStack)WarpBookMod.lastHeldBooks.get(player)).func_77978_p().func_150295_c("WarpPages", 10);
         ItemStack page = ItemStack.func_77949_a(stack.func_150305_b(pageSlot));
         return page;
      } catch (ClassCastException var4) {
         return null;
      }
   }

   public IMessage onMessage(PacketWarp message, MessageContext ctx) {
      EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
      ItemStack page = getPageById(player, message.pageSlot);
      WarpBookMod.proxy.handleWarp(player, page);
      return null;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pageSlot = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.pageSlot);
   }
}
