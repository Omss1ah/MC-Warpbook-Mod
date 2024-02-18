package com.panicnot42.warpbook.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class WarpBookInventoryItem implements IInventory {
   private String name = "warpbook.name";
   private final ItemStack stack;
   public static final int INV_SIZE = 54;
   ItemStack[] inventory = new ItemStack[54];

   public WarpBookInventoryItem(ItemStack heldItem) {
      this.stack = heldItem;
      if (!this.stack.func_77942_o()) {
         this.stack.func_77982_d(new NBTTagCompound());
      }

      NBTTagList items = this.stack.func_77978_p().func_150295_c("WarpPages", (new NBTTagCompound()).func_74732_a());

      for(int i = 0; i < items.func_74745_c(); ++i) {
         NBTTagCompound item = items.func_150305_b(i);
         int slot = item.func_74762_e("Slot");
         if (slot >= 0 && slot < this.func_70302_i_()) {
            this.func_70299_a(slot, ItemStack.func_77949_a(item));
         }
      }

   }

   public int func_70302_i_() {
      return this.inventory.length;
   }

   public ItemStack func_70301_a(int i) {
      return this.inventory[i];
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
      this.inventory[slot] = itemStack;
      if (itemStack != null && itemStack.field_77994_a > this.func_70297_j_()) {
         itemStack.field_77994_a = this.func_70297_j_();
      }

      this.func_70296_d();
   }

   public String func_145825_b() {
      return I18n.func_135052_a(this.name, new Object[0]);
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer entityplayer) {
      return true;
   }

   public boolean func_94041_b(int i, ItemStack itemstack) {
      return WarpBookSlot.itemValid(itemstack);
   }

   public void func_70295_k_() {
   }

   public void func_70305_f() {
   }

   public void func_70296_d() {
      for(int i = 0; i < this.func_70302_i_(); ++i) {
         if (this.func_70301_a(i) != null && this.func_70301_a(i).field_77994_a == 0) {
            this.func_70299_a(i, (ItemStack)null);
         }
      }

      NBTTagList items = new NBTTagList();

      for(int i = 0; i < this.func_70302_i_(); ++i) {
         if (this.func_70301_a(i) != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.func_74768_a("Slot", i);
            this.func_70301_a(i).func_77955_b(item);
            items.func_74742_a(item);
         }
      }

      this.stack.func_77978_p().func_74782_a("WarpPages", items);
   }

   public boolean func_145818_k_() {
      return false;
   }
}
