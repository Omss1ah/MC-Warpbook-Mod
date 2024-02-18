package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.Waypoint;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WarpPageItem extends Item {
   private static final String[] itemMetaNames = new String[]{"unbound", "bound", "hyperbound", "deathly", "potato", "player"};
   private static final String[] itemTextures = new String[]{"unboundwarppage", "boundwarppage", "hyperboundwarppage", "deathlywarppage", "spudpage", "playerpage"};
   @SideOnly(Side.CLIENT)
   private IIcon[] itemIcons;

   public WarpPageItem() {
      super.func_77627_a(true).func_77625_d(16).func_77637_a(WarpBookMod.tabBook).func_77656_e(0).func_77655_b("warppage");
   }

   @SideOnly(Side.CLIENT)
   public IIcon func_77617_a(int meta) {
      return this.itemIcons[MathHelper.func_76125_a(meta, 0, itemMetaNames.length - 1)];
   }

   public String func_77667_c(ItemStack itemStack) {
      return super.func_77658_a() + "." + itemMetaNames[MathHelper.func_76125_a(itemStack.func_77960_j(), 0, itemMetaNames.length - 1)];
   }

   public void func_94581_a(IIconRegister iconRegister) {
      this.itemIcons = new IIcon[itemTextures.length];

      for(int i = 0; i < itemTextures.length; ++i) {
         this.itemIcons[i] = iconRegister.func_94245_a("warpbook:" + itemTextures[i]);
      }

   }

   public int func_77626_a(ItemStack itemStack) {
      return 1;
   }

   public ItemStack func_77659_a(ItemStack itemStack, World world, EntityPlayer player) {
      if (player.func_70093_af()) {
         switch(itemStack.func_77960_j()) {
         case 0:
            itemStack.func_77964_b(5);
            itemStack.func_77982_d(new NBTTagCompound());
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
               itemStack.func_77978_p().func_74778_a("playeruuid", player.func_146103_bH().getId().toString());
            }
            break;
         case 1:
         case 3:
         case 4:
         case 5:
            itemStack.func_77964_b(0);
            itemStack.func_77982_d(new NBTTagCompound());
         case 2:
         }
      } else {
         switch(itemStack.func_77960_j()) {
         case 0:
            ItemStack newStack = new ItemStack(WarpBookMod.warpPageItem, 1, 1);
            writeWaypointToPage(newStack, MathUtils.round(player.field_70165_t, RoundingMode.HALF_DOWN), MathUtils.round(player.field_70163_u, RoundingMode.HALF_DOWN), MathUtils.round(player.field_70161_v, RoundingMode.HALF_DOWN), player.field_71093_bK);
            player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWaypointGuiIndex, world, (int)player.field_70165_t, (int)player.field_70163_u, (int)player.field_70161_v);
            WarpBookMod.formingPages.put(player, newStack);
            break;
         case 1:
         case 2:
         case 5:
            WarpBookMod.proxy.handleWarp(player, itemStack);
            if (!player.field_71075_bZ.field_75098_d) {
               --itemStack.field_77994_a;
            }
         case 3:
         default:
            break;
         case 4:
            itemStack = new ItemStack(GameRegistry.findItem("minecraft", "poisonous_potato"), 1);
            WarpBookMod.proxy.goFullPotato(player, itemStack);
         }
      }

      return itemStack;
   }

   public static void writeWaypointToPage(ItemStack page, int x, int y, int z, int dim) {
      page.func_77964_b(1);
      if (!page.func_77942_o()) {
         page.func_77982_d(new NBTTagCompound());
      }

      page.func_77978_p().func_74768_a("posX", x);
      page.func_77978_p().func_74768_a("posY", y);
      page.func_77978_p().func_74768_a("posZ", z);
      page.func_77978_p().func_74768_a("dim", dim);
   }

   public static void writeWaypointToPage(ItemStack page, Waypoint wp) {
      writeWaypointToPage(page, wp.x, wp.y, wp.z, wp.dim);
      page.func_77978_p().func_74778_a("name", wp.friendlyName);
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack item, EntityPlayer player, List list, boolean thing) {
      switch(item.func_77960_j()) {
      case 1:
         try {
            list.add(item.func_77978_p().func_74779_i("name"));
            list.add(I18n.func_135052_a("warpbook.bindmsg", new Object[]{item.func_77978_p().func_74762_e("posX"), item.func_77978_p().func_74762_e("posY"), item.func_77978_p().func_74762_e("posZ"), item.func_77978_p().func_74762_e("dim")}));
         } catch (Exception var6) {
         }
         break;
      case 2:
         String name = item.func_77978_p().func_74779_i("hypername");
         list.add(name);
         list.add(WarpWorldStorage.instance(player.field_70170_p).getWaypoint(name).friendlyName);
      case 3:
      default:
         break;
      case 4:
         list.add(I18n.func_135052_a("help.potato.flavour1", new Object[0]));
         list.add(I18n.func_135052_a("help.potato.flavour2", new Object[0]));
         break;
      case 5:
         list.add(PlayerUtils.getNameByUUID(UUID.fromString(item.func_77978_p().func_74779_i("playeruuid"))));
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_150895_a(Item item, CreativeTabs tab, List items) {
      items.add(new ItemStack(item, 1, 0));
      items.add(new ItemStack(item, 1, 3));
      items.add(new ItemStack(item, 1, 4));
   }

   public boolean hasContainerItem(ItemStack itemStack) {
      return itemStack.func_77960_j() == 1;
   }

   public ItemStack getContainerItem(ItemStack itemStack) {
      return itemStack.func_77960_j() == 1 ? itemStack.func_77946_l() : null;
   }
}
