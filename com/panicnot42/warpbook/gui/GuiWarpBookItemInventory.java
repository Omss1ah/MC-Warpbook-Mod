package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.container.WarpBookContainerItem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiWarpBookItemInventory extends GuiContainer {
   private static final ResourceLocation iconLocation = new ResourceLocation("warpbook", "textures/gui/warpinv.png");
   private static final ResourceLocation deathBox = new ResourceLocation("warpbook", "textures/gui/deathbox.png");
   private static final ResourceLocation pearlBox = new ResourceLocation("warpbook", "textures/gui/pearlbox.png");
   private final WarpBookInventoryItem inventory;

   public GuiWarpBookItemInventory(WarpBookContainerItem warpBookContainerItem) {
      super(warpBookContainerItem);
      this.inventory = warpBookContainerItem.inventory;
      this.field_146999_f = 194;
      this.field_147000_g = 222;
   }

   protected void func_146979_b(int par1, int par2) {
      String s = this.inventory.func_145825_b();
      this.field_146289_q.func_78276_b(s, (this.field_146999_f - 18) / 2 - this.field_146289_q.func_78256_a(s) / 2, 6, 4210752);
   }

   protected void func_146976_a(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(iconLocation);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      if (WarpBookMod.deathPagesEnabled) {
         this.field_146297_k.func_110434_K().func_110577_a(deathBox);
         this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      }

      if (WarpBookMod.fuelEnabled) {
         this.field_146297_k.func_110434_K().func_110577_a(pearlBox);
         this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      }

   }
}
