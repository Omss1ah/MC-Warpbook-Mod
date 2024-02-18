package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.Waypoint;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;

public class CreateWaypointCommand extends CommandBase {
   public String func_71517_b() {
      return "waypoint";
   }

   public String func_71518_a(ICommandSender var1) {
      return "/waypoint id x y z dim description";
   }

   public void func_71515_b(ICommandSender var1, String[] var2) {
      if (var2.length < 6) {
         CommandUtils.printUsage(var1, this);
      } else {
         WarpWorldStorage storage = WarpWorldStorage.instance(var1.func_130014_f_());

         try {
            Waypoint wp = new Waypoint(CommandUtils.stringConcat(var2, 5), var2[0], CommandBase.func_71526_a(var1, var2[1]), CommandBase.func_71526_a(var1, var2[2]), CommandBase.func_71526_a(var1, var2[3]), CommandBase.func_71526_a(var1, var2[4]));
            storage.addWaypoint(wp);
            CommandUtils.info(var1, I18n.func_135052_a("help.waypointcreated", new Object[0]));
         } catch (NumberInvalidException var5) {
            CommandUtils.printUsage(var1, this);
            CommandUtils.showError(var1, CommandUtils.ChatType.TYPE_int, (String)var5.func_74844_a()[0]);
         }

      }
   }

   public int compareTo(Object o) {
      return 42;
   }
}
