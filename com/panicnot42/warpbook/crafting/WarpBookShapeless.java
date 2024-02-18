package com.panicnot42.warpbook.crafting;

import com.panicnot42.warpbook.WarpBookMod;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class WarpBookShapeless extends ShapelessRecipes {
   ItemStack field_77580_a;

   public WarpBookShapeless(ItemStack recipeOutput, List recipeItems) {
      super(recipeOutput, recipeItems);
      this.field_77580_a = recipeOutput;
   }

   public ItemStack func_77572_b(InventoryCrafting inventory) {
      return new ItemStack(WarpBookMod.warpBookItem, 1, 16);
   }
}
