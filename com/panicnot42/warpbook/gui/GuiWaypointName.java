package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.net.packet.PacketWaypointName;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiWaypointName extends GuiScreen {
   private GuiTextField waypointName;
   private GuiButton doneButton;

   public GuiWaypointName(EntityPlayer entityPlayer) {
   }

   public void func_73876_c() {
      this.waypointName.func_146178_a();
   }

   public void func_73866_w_() {
      Keyboard.enableRepeatEvents(true);
      this.field_146292_n.clear();
      this.field_146292_n.add(this.doneButton = new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96 + 12, I18n.func_135052_a("gui.done", new Object[0])));
      this.waypointName = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - 150, 60, 300, 20);
      this.waypointName.func_146203_f(128);
      this.waypointName.func_146195_b(true);
      this.waypointName.func_146180_a("");
      this.doneButton.field_146124_l = this.waypointName.func_146179_b().trim().length() > 0;
   }

   public void func_146281_b() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void func_146284_a(GuiButton par1GuiButton) {
      if (par1GuiButton.field_146124_l) {
         PacketWaypointName packet = new PacketWaypointName(this.waypointName.func_146179_b());
         WarpBookMod.network.sendToServer(packet);
         this.field_146297_k.func_147108_a((GuiScreen)null);
      }

   }

   protected void func_73869_a(char par1, int par2) {
      this.waypointName.func_146201_a(par1, par2);
      this.doneButton.field_146124_l = this.waypointName.func_146179_b().trim().length() > 0;
      if (par2 == 28 || par2 == 156) {
         this.func_146284_a(this.doneButton);
      }

   }

   protected void func_73864_a(int par1, int par2, int par3) {
      super.func_73864_a(par1, par2, par3);
      this.waypointName.func_146192_a(par1, par2, par3);
   }

   public void func_73863_a(int par1, int par2, float par3) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, I18n.func_135052_a("warpbook.bindpage", new Object[0]), this.field_146294_l / 2, 20, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("warpbook.namewaypoint", new Object[0]), this.field_146294_l / 2 - 150, 47, 10526880);
      this.waypointName.func_146194_f();
      super.func_73863_a(par1, par2, par3);
   }
}
