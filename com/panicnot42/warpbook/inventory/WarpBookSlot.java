package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.WarpPageItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WarpBookSlot extends Slot {
   public WarpBookSlot(WarpBookInventoryItem inventory, int i, int j, int k) {
      super(inventory, i, j, k);
   }

   public static boolean itemValid(ItemStack itemStack) {
      return itemStack.func_77973_b() instanceof WarpPageItem && itemStack.func_77960_j() != 0 && itemStack.func_77960_j() != 3 && itemStack.func_77960_j() != 4;
   }

   public boolean func_75214_a(ItemStack itemStack) {
      return itemValid(itemStack);
   }
}
