package com.panicnot42.warpbook.util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandUtils {
   private static final ChatStyle errorStyle = new ChatStyle();
   private static final ChatStyle usageStyle;
   private static final ChatStyle infoStyle;

   public static void printUsage(ICommandSender sender, CommandBase createWaypointCommand) {
      ChatComponentText prefix = new ChatComponentText("Usage:");
      ChatComponentText usage = new ChatComponentText(createWaypointCommand.func_71518_a(sender));
      prefix.func_150255_a(usageStyle);
      usage.func_150255_a(usageStyle);
      sender.func_145747_a(prefix);
      sender.func_145747_a(usage);
   }

   public static void showError(ICommandSender sender, CommandUtils.ChatType type, String string) {
      showError(sender, String.format("'%s' is not a valid %s", string, getFriendlyName(type)));
   }

   public static void showError(ICommandSender sender, String message) {
      ChatComponentText text = new ChatComponentText(message);
      text.func_150255_a(errorStyle);
      sender.func_145747_a(text);
   }

   private static String getFriendlyName(CommandUtils.ChatType type) {
      switch(type) {
      case TYPE_int:
         return "integer";
      case TYPE_player:
         return "player";
      default:
         return "duck";
      }
   }

   public static String stringConcat(String[] var2, int start) {
      StringBuilder builder = new StringBuilder();

      for(int i = start; i < var2.length; ++i) {
         builder.append(var2[i]);
         builder.append(" ");
      }

      if (var2.length > 0) {
         builder.deleteCharAt(builder.length() - 1);
      }

      return builder.toString();
   }

   public static void info(ICommandSender sender, String message) {
      ChatComponentText text = new ChatComponentText(message);
      text.func_150255_a(infoStyle);
      sender.func_145747_a(text);
   }

   static {
      errorStyle.func_150238_a(EnumChatFormatting.RED);
      usageStyle = new ChatStyle();
      usageStyle.func_150238_a(EnumChatFormatting.YELLOW);
      infoStyle = new ChatStyle();
      infoStyle.func_150238_a(EnumChatFormatting.WHITE);
   }

   public static enum ChatType {
      TYPE_int,
      TYPE_player;
   }
}
