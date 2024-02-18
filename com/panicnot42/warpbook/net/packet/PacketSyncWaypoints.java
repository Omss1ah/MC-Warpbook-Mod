package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import net.minecraft.nbt.NBTTagCompound;

public class PacketSyncWaypoints implements IMessage, IMessageHandler<PacketSyncWaypoints, IMessage> {
   public HashMap<String, Waypoint> table;

   public PacketSyncWaypoints() {
   }

   public PacketSyncWaypoints(HashMap<String, Waypoint> table) {
      this.table = table;
   }

   public void fromBytes(ByteBuf buffer) {
      NBTTagCompound tag = ByteBufUtils.readTag(buffer);
      this.table = NBTUtils.readHashMapFromNBT(tag.func_150295_c("data", 10), Waypoint.class);
   }

   public void toBytes(ByteBuf buffer) {
      NBTTagCompound tag = new NBTTagCompound();
      NBTUtils.writeHashMapToNBT(tag.func_150295_c("data", 10), this.table);
      ByteBufUtils.writeTag(buffer, tag);
   }

   public IMessage onMessage(PacketSyncWaypoints message, MessageContext ctx) {
      WarpWorldStorage.table = message.table;
      return null;
   }
}
