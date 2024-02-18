package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;

public class ListWaypointCommand extends CommandBase {
   public String func_71517_b() {
      return "waypointlist";
   }

   public String func_71518_a(ICommandSender var1) {
      return "/waypointlist [page]";
   }

   public void func_71515_b(ICommandSender var1, String[] var2) {
      WarpWorldStorage storage = WarpWorldStorage.instance(var1.func_130014_f_());

      try {
         int page = var2.length == 0 ? 0 : CommandBase.func_71526_a(var1, var2[0]);
         String[] wps = storage.listWaypoints();
         if (wps.length == 0) {
            CommandUtils.showError(var1, I18n.func_135052_a("help.nowaypointsfound", new Object[0]));
            return;
         }

         CommandUtils.info(var1, String.format("-- Page %d --", page));

         for(int i = page * 8; i < page * 8 + 9; ++i) {
            try {
               CommandUtils.info(var1, wps[i]);
            } catch (ArrayIndexOutOfBoundsException var8) {
            }
         }

         CommandUtils.info(var1, String.format("-- Page %d --", page));
      } catch (NumberInvalidException var9) {
         CommandUtils.printUsage(var1, this);
         CommandUtils.showError(var1, CommandUtils.ChatType.TYPE_int, (String)var9.func_74844_a()[0]);
      }

   }

   public int compareTo(Object o) {
      return 42;
   }
}
