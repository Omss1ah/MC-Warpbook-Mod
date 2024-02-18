package com.panicnot42.warpbook.crafting;

import com.panicnot42.warpbook.item.WarpPageItem;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class WarpPageShapeless extends ShapelessRecipes {
   ItemStack field_77580_a;

   public WarpPageShapeless(ItemStack recipeOutput, List recipeItems) {
      super(recipeOutput, recipeItems);
      this.field_77580_a = recipeOutput;
   }

   public ItemStack func_77572_b(InventoryCrafting inventory) {
      ItemStack output = this.field_77580_a.func_77946_l();

      try {
         for(int i = 0; i < inventory.func_70302_i_(); ++i) {
            if (inventory.func_70301_a(i) != null && inventory.func_70301_a(i).func_77973_b() instanceof WarpPageItem && inventory.func_70301_a(i).func_77960_j() == 1) {
               output.func_77982_d(inventory.func_70301_a(i).func_77978_p());
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return output;
   }
}
