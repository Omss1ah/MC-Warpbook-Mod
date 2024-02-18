package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WarpBookItem extends Item {
   public WarpBookItem() {
      this.func_77625_d(1).func_77637_a(WarpBookMod.tabBook).func_77655_b("warpbook").func_111206_d("warpbook:warpbook").func_77656_e(WarpBookMod.fuelEnabled ? 16 : 0);
   }

   public int func_77626_a(ItemStack itemStack) {
      return 1;
   }

   public ItemStack func_77659_a(ItemStack itemStack, World world, EntityPlayer player) {
      WarpBookMod.lastHeldBooks.put(player, itemStack);
      if (player.func_70093_af()) {
         player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookInventoryGuiIndex, world, (int)player.field_70165_t, (int)player.field_70163_u, (int)player.field_70161_v);
      } else {
         player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWarpGuiIndex, world, (int)player.field_70165_t, (int)player.field_70163_u, (int)player.field_70161_v);
      }

      return itemStack;
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack item, EntityPlayer player, List list, boolean thing) {
      try {
         list.add(I18n.func_135052_a("warpbook.booktooltip", new Object[]{item.func_77978_p().func_150295_c("WarpPages", (new NBTTagCompound()).func_74732_a()).func_74745_c()}));
      } catch (Exception var6) {
      }

   }

   public boolean func_82789_a(ItemStack p_82789_1_, ItemStack p_82789_2_) {
      return false;
   }

   public static int getRespawnsLeft(ItemStack item) {
      if (item.func_77978_p() == null) {
         item.func_77982_d(new NBTTagCompound());
      }

      return item.func_77978_p().func_74765_d("deathPages");
   }

   public static int getFuelLeft(ItemStack item) {
      return 16 - item.func_77960_j();
   }

   public static void setRespawnsLeft(ItemStack item, int deaths) {
      if (item.func_77978_p() == null) {
         item.func_77982_d(new NBTTagCompound());
      }

      item.func_77978_p().func_74777_a("deathPages", (short)deaths);
   }

   public static void setFuelLeft(ItemStack item, int fuel) {
      item.func_77964_b(16 - fuel);
   }

   public static void decrRespawnsLeft(ItemStack item) {
      setRespawnsLeft(item, getRespawnsLeft(item) - 1);
   }

   public static void decrFuelLeft(ItemStack item) {
      setFuelLeft(item, getFuelLeft(item) - 1);
   }
}
