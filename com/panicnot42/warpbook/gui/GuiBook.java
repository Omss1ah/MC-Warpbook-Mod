package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen {
   private final EntityPlayer entityPlayer;
   private NBTTagList items;

   public GuiBook(EntityPlayer entityPlayer) {
      this.entityPlayer = entityPlayer;
   }

   public void func_73866_w_() {
      Keyboard.enableRepeatEvents(true);
      this.field_146292_n.clear();
      if (!this.entityPlayer.func_70694_bm().func_77942_o()) {
         this.entityPlayer.func_70694_bm().func_77982_d(new NBTTagCompound());
      }

      this.items = this.entityPlayer.func_70694_bm().func_77978_p().func_150295_c("WarpPages", (new NBTTagCompound()).func_74732_a());
      if (this.items.func_74745_c() == 0) {
         CommandUtils.showError(this.entityPlayer, I18n.func_135052_a("help.nopages", new Object[0]));
         this.field_146297_k.func_147108_a((GuiScreen)null);
      } else {
         for(int i = 0; i < this.items.func_74745_c(); ++i) {
            NBTTagCompound compound = ItemStack.func_77949_a(this.items.func_150305_b(i)).func_77978_p();

            try {
               this.field_146292_n.add(new GuiButton(i, (this.field_146294_l - 404) / 2 + i % 6 * 68, 16 + 24 * (i / 6), 64, 16, getButtonText(compound)));
            } catch (Exception var4) {
            }
         }

      }
   }

   private static String getButtonText(NBTTagCompound compound) {
      if (compound.func_74764_b("hypername")) {
         return StringUtils.shorten(compound.func_74779_i("hypername"), 10);
      } else if (compound.func_74764_b("name")) {
         return StringUtils.shorten(compound.func_74779_i("name"), 10);
      } else {
         return compound.func_74764_b("playeruuid") ? StringUtils.shorten(PlayerUtils.getNameByUUID(UUID.fromString(compound.func_74779_i("playeruuid"))), 10) : "";
      }
   }

   public void func_146281_b() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void func_146284_a(GuiButton guiButton) {
      PacketWarp packet = new PacketWarp(guiButton.field_146127_k);
      ItemStack page = PacketWarp.getPageById(this.entityPlayer, guiButton.field_146127_k);
      WarpBookMod.proxy.handleWarp(Minecraft.func_71410_x().field_71439_g, page);
      WarpBookMod.network.sendToServer(packet);
      this.field_146297_k.func_147108_a((GuiScreen)null);
   }

   public void func_73863_a(int par1, int par2, float par3) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, I18n.func_135052_a("warpbook.dowarp", new Object[0]), this.field_146294_l / 2, 6, 16777215);
      super.func_73863_a(par1, par2, par3);
   }

   public boolean func_73868_f() {
      return false;
   }
}
