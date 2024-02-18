package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.WarpBookItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class WarpBookInventorySlot extends Slot {
   public WarpBookInventorySlot(InventoryPlayer inventory, int i, int j, int k) {
      super(inventory, i, j, k);
   }

   public boolean func_82869_a(EntityPlayer par1EntityPlayer) {
      return this.func_75216_d() && !(this.func_75211_c().func_77973_b() instanceof WarpBookItem);
   }
}
