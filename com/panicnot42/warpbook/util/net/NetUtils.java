package com.panicnot42.warpbook.util.net;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

public class NetUtils {
   public static EntityPlayer getPlayerFromContext(MessageContext ctx) {
      EntityPlayer player = null;
      switch(FMLCommonHandler.instance().getEffectiveSide()) {
      case CLIENT:
         player = getClientPlayer();
         break;
      case SERVER:
         INetHandler netHandler = ctx.netHandler;
         player = ((NetHandlerPlayServer)netHandler).field_147369_b;
      }

      return (EntityPlayer)player;
   }

   @SideOnly(Side.CLIENT)
   private static EntityPlayer getClientPlayer() {
      return Minecraft.func_71410_x().field_71439_g;
   }
}
