package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

public class WarpBookSpecialInventory implements IInventory {
   ItemStack fuel;
   ItemStack deathly;
   ItemStack heldItem;

   public WarpBookSpecialInventory(ItemStack heldItem) {
      int deaths = WarpBookItem.getRespawnsLeft(heldItem);
      int damage = WarpBookItem.getFuelLeft(heldItem);
      this.fuel = damage == 0 ? null : new ItemStack(Items.field_151079_bi, damage);
      this.deathly = deaths == 0 ? null : new ItemStack(WarpBookMod.warpPageItem, deaths);
      if (this.deathly != null) {
         this.deathly.func_77964_b(3);
      }

      this.heldItem = heldItem;
   }

   public int func_70302_i_() {
      return 2;
   }

   public ItemStack func_70301_a(int slot) {
      return slot == 0 ? this.fuel : this.deathly;
   }

   public ItemStack func_70298_a(int slot, int quantity) {
      ItemStack stack = this.func_70301_a(slot);
      if (stack != null) {
         if (stack.field_77994_a > quantity) {
            stack = stack.func_77979_a(quantity);
            this.func_70296_d();
         } else {
            this.func_70299_a(slot, (ItemStack)null);
         }
      }

      return stack;
   }

   public ItemStack func_70304_b(int slot) {
      ItemStack stack = this.func_70301_a(slot);
      this.func_70299_a(slot, (ItemStack)null);
      return stack;
   }

   public void func_70299_a(int slot, ItemStack itemStack) {
      if (slot == 0) {
         this.fuel = itemStack;
      } else {
         this.deathly = itemStack;
      }

      if (itemStack != null && itemStack.field_77994_a > this.func_70297_j_()) {
         itemStack.field_77994_a = this.func_70297_j_();
      }

      this.func_70296_d();
   }

   public String func_145825_b() {
      return null;
   }

   public boolean func_145818_k_() {
      return false;
   }

   public int func_70297_j_() {
      return 16;
   }

   public void func_70296_d() {
      WarpBookItem.setFuelLeft(this.heldItem, this.fuel == null ? 0 : this.fuel.field_77994_a);
      WarpBookItem.setRespawnsLeft(this.heldItem, this.deathly == null ? 0 : this.deathly.field_77994_a);
   }

   public boolean func_70300_a(EntityPlayer player) {
      return true;
   }

   public void func_70295_k_() {
   }

   public void func_70305_f() {
   }

   public boolean func_94041_b(int slot, ItemStack itemStack) {
      return slot == 0 ? itemStack.func_77973_b() instanceof ItemEnderPearl : itemStack.func_77973_b() instanceof WarpPageItem && itemStack.func_77960_j() == 3;
   }
}
