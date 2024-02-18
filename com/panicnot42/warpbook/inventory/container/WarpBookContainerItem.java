package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.WarpBookDeathlySlot;
import com.panicnot42.warpbook.inventory.WarpBookEnderSlot;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.WarpBookInventorySlot;
import com.panicnot42.warpbook.inventory.WarpBookSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WarpBookContainerItem extends Container {
   public final WarpBookInventoryItem inventory;

   public WarpBookContainerItem(EntityPlayer player, InventoryPlayer inventoryPlayer, WarpBookInventoryItem inventoryItem, WarpBookSpecialInventory inventorySpecial) {
      this.inventory = inventoryItem;

      int i;
      for(i = 0; i < 54; ++i) {
         this.func_75146_a(new WarpBookSlot(this.inventory, i, 8 + 18 * (i % 9), 18 + 18 * (i / 9)));
      }

      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new WarpBookInventorySlot(inventoryPlayer, i, 8 + i * 18, 198));
      }

      if (WarpBookMod.fuelEnabled) {
         this.func_75146_a(new WarpBookEnderSlot(inventorySpecial, 0, 174, 54));
      }

      if (WarpBookMod.deathPagesEnabled) {
         this.func_75146_a(new WarpBookDeathlySlot(inventorySpecial, 1, 174, 72));
      }

   }

   public boolean func_75145_c(EntityPlayer entityplayer) {
      return true;
   }

   public ItemStack func_82846_b(EntityPlayer player, int slotNum) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(slotNum);
      if (slot != null && slot.func_75216_d()) {
         ItemStack moving = slot.func_75211_c();
         itemstack = moving.func_77946_l();
         if (0 <= slotNum && slotNum <= 53) {
            if (!this.func_75135_a(moving, 54, 89, true)) {
               return null;
            }

            slot.func_75220_a(moving, itemstack);
         } else if (WarpBookSlot.itemValid(slot.func_75211_c()) && !this.func_75135_a(moving, 0, 54, false)) {
            return null;
         }

         if (moving.field_77994_a == 0) {
            slot.func_75215_d((ItemStack)null);
         } else {
            slot.func_75218_e();
         }

         if (moving.field_77994_a == itemstack.field_77994_a) {
            return null;
         }

         slot.func_82870_a(player, moving);
      }

      return itemstack;
   }
}
