package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.container.WarpBookContainerItem;
import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiManager implements IGuiHandler {
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return ID == WarpBookMod.WarpBookInventoryGuiIndex ? new WarpBookContainerItem(player, player.field_71071_by, new WarpBookInventoryItem(player.func_70694_bm()), new WarpBookSpecialInventory(player.func_70694_bm())) : null;
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      if (ID == WarpBookMod.WarpBookWarpGuiIndex) {
         return new GuiBook(player);
      } else if (ID == WarpBookMod.WarpBookInventoryGuiIndex) {
         return new GuiWarpBookItemInventory(new WarpBookContainerItem(player, player.field_71071_by, new WarpBookInventoryItem(player.func_70694_bm()), new WarpBookSpecialInventory(player.func_70694_bm())));
      } else {
         return ID == WarpBookMod.WarpBookWaypointGuiIndex ? new GuiWaypointName(player) : null;
      }
   }
}
