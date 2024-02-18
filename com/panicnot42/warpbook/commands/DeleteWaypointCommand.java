package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class DeleteWaypointCommand extends CommandBase {
   public String func_71517_b() {
      return "waypointdelete";
   }

   public String func_71518_a(ICommandSender var1) {
      return "/waypointdelete name";
   }

   public void func_71515_b(ICommandSender var1, String[] var2) {
      WarpWorldStorage storage = WarpWorldStorage.instance(var1.func_130014_f_());
      if (var2.length != 1) {
         CommandUtils.printUsage(var1, this);
      } else {
         if (storage.deleteWaypoint(var2[0])) {
            CommandUtils.info(var1, I18n.func_135052_a("help.waypointdelete", new Object[0]));
         } else {
            CommandUtils.showError(var1, I18n.func_135052_a("help.notawaypoint", new Object[]{var2[0]}));
         }

      }
   }

   public int compareTo(Object o) {
      return 42;
   }
}
