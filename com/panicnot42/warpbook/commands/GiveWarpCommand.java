package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GiveWarpCommand extends CommandBase {
   public String func_71517_b() {
      return "givewarp";
   }

   public String func_71518_a(ICommandSender var1) {
      return "/givewarp name [player]";
   }

   public void func_71515_b(ICommandSender var1, String[] var2) {
      WarpWorldStorage storage = WarpWorldStorage.instance(var1.func_130014_f_());
      String name;
      Object player;
      switch(var2.length) {
      case 1:
         name = var2[0];
         if (!(var1 instanceof EntityPlayer)) {
            CommandUtils.showError(var1, I18n.func_135052_a("help.noplayerspecified", new Object[0]));
            return;
         }

         player = (EntityPlayer)var1;
         break;
      case 2:
         name = var2[0];

         try {
            player = CommandBase.func_82359_c(var1, var2[1]);
            break;
         } catch (PlayerNotFoundException var8) {
            CommandUtils.showError(var1, CommandUtils.ChatType.TYPE_player, var2[1]);
            return;
         }
      default:
         CommandUtils.printUsage(var1, this);
         return;
      }

      if (!storage.waypointExists(name)) {
         CommandUtils.showError(var1, I18n.func_135052_a("help.waypointdoesnotexist", new Object[]{name}));
      } else {
         ItemStack hyperStack = new ItemStack(WarpBookMod.warpPageItem);
         hyperStack.func_77964_b(2);
         NBTTagCompound compound = new NBTTagCompound();
         compound.func_74778_a("hypername", name);
         hyperStack.func_77982_d(compound);
         ((EntityPlayer)player).field_71071_by.func_70441_a(hyperStack);
      }
   }

   public int compareTo(Object o) {
      return 42;
   }
}
