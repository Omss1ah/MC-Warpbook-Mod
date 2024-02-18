package com.panicnot42.warpbook;

import com.panicnot42.warpbook.commands.CreateWaypointCommand;
import com.panicnot42.warpbook.commands.DeleteWaypointCommand;
import com.panicnot42.warpbook.commands.GiveWarpCommand;
import com.panicnot42.warpbook.commands.ListWaypointCommand;
import com.panicnot42.warpbook.crafting.WarpBookShapeless;
import com.panicnot42.warpbook.crafting.WarpPageShapeless;
import com.panicnot42.warpbook.gui.GuiManager;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.net.packet.PacketSyncPlayers;
import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.net.packet.PacketWaypointName;
import com.panicnot42.warpbook.util.PlayerUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerDisconnectionFromClientEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
   modid = "warpbook",
   name = "Warp Book",
   version = "2.0.null"
)
public class WarpBookMod {
   @Instance("warpbook")
   public static WarpBookMod instance;
   public static final Logger logger = LogManager.getLogger("warpbook");
   public static final SimpleNetworkWrapper network;
   public static WarpBookItem warpBookItem;
   public static WarpPageItem warpPageItem;
   @SidedProxy(
      clientSide = "com.panicnot42.warpbook.client.ClientProxy",
      serverSide = "com.panicnot42.warpbook.Proxy"
   )
   public static Proxy proxy;
   private static int guiIndex;
   public static float exhaustionCoefficient;
   public static boolean deathPagesEnabled;
   public static boolean fuelEnabled;
   public static final int WarpBookWarpGuiIndex;
   public static final int WarpBookWaypointGuiIndex;
   public static final int WarpBookInventoryGuiIndex;
   public static HashMap<EntityPlayer, ItemStack> lastHeldBooks;
   public static HashMap<EntityPlayer, ItemStack> formingPages;
   public static CreativeTabs tabBook;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      Configuration config = new Configuration(event.getSuggestedConfigurationFile());
      config.load();
      exhaustionCoefficient = (float)config.get("tweaks", "exhaustion coefficient", 10.0D).getDouble(10.0D);
      deathPagesEnabled = config.get("features", "death pages", true).getBoolean(true);
      fuelEnabled = config.get("features", "fuel", false).getBoolean(false);
      warpBookItem = new WarpBookItem();
      warpPageItem = new WarpPageItem();
      GameRegistry.registerItem(warpBookItem, "warpbook");
      GameRegistry.registerItem(warpPageItem, "warppage");
      List<ItemStack> bookRecipe = new ArrayList();
      if (config.get("tweaks", "hard recipes", false).getBoolean(false)) {
         bookRecipe.add(new ItemStack(Items.field_151122_aG));
         bookRecipe.add(new ItemStack(Items.field_151156_bN));
         GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new Object[]{new ItemStack(Items.field_151121_aF), new ItemStack(Items.field_151061_bv)});
      } else {
         bookRecipe.add(new ItemStack(Items.field_151122_aG));
         bookRecipe.add(new ItemStack(Items.field_151079_bi));
         GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new Object[]{new ItemStack(Items.field_151121_aF), new ItemStack(Items.field_151079_bi)});
      }

      ItemStack emptyBook = new ItemStack(warpBookItem);
      ItemStack boundpage = new ItemStack(warpPageItem, 1, 1);
      List<ItemStack> recipe = new ArrayList();
      recipe.add(boundpage);
      recipe.add(new ItemStack(warpPageItem));
      GameRegistry.addRecipe(new WarpPageShapeless(boundpage, recipe));
      GameRegistry.addRecipe(new WarpBookShapeless(emptyBook, bookRecipe));
      if (deathPagesEnabled) {
         GameRegistry.addShapedRecipe(new ItemStack(warpPageItem, 1, 3), new Object[]{" x ", "yzy", "   ", 'z', new ItemStack(warpPageItem, 1), 'y', new ItemStack(Items.field_151045_i), 'x', new ItemStack(Items.field_151071_bq)});
      }

      GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem, 1, 5), new Object[]{new ItemStack(warpPageItem, 1), new ItemStack(Items.field_151174_bG)});
      config.save();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.registerRenderers();
      NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      int disc = 0;
      WarpWorldStorage.postInit();
      int var3 = disc + 1;
      network.registerMessage(PacketWarp.class, PacketWarp.class, disc, Side.SERVER);
      network.registerMessage(PacketWaypointName.class, PacketWaypointName.class, var3++, Side.SERVER);
      network.registerMessage(PacketSyncWaypoints.class, PacketSyncWaypoints.class, var3++, Side.CLIENT);
      network.registerMessage(PacketSyncPlayers.class, PacketSyncPlayers.class, var3++, Side.CLIENT);
      network.registerMessage(PacketEffect.class, PacketEffect.class, var3++, Side.CLIENT);
      MinecraftForge.EVENT_BUS.register(proxy);
      MinecraftForge.EVENT_BUS.register(this);
      FMLCommonHandler.instance().bus().register(proxy);
      FMLCommonHandler.instance().bus().register(this);
   }

   @EventHandler
   public void serverStarting(FMLServerStartingEvent event) {
      ServerCommandManager manager = (ServerCommandManager)MinecraftServer.func_71276_C().func_71187_D();
      manager.func_71560_a(new CreateWaypointCommand());
      manager.func_71560_a(new ListWaypointCommand());
      manager.func_71560_a(new DeleteWaypointCommand());
      manager.func_71560_a(new GiveWarpCommand());
   }

   @SubscribeEvent
   public void clientJoined(ServerConnectionFromClientEvent e) {
      EntityPlayerMP player = ((NetHandlerPlayServer)e.handler).field_147369_b;
      if (!player.field_70170_p.field_72995_K) {
         WarpWorldStorage.instance(player.field_70170_p).updateClient(player, e);
         PlayerUtils.instance().updateClient(player, e);
      }

   }

   @SubscribeEvent
   public void clientLeft(ServerDisconnectionFromClientEvent e) {
      EntityPlayerMP player = ((NetHandlerPlayServer)e.handler).field_147369_b;
      PlayerUtils.instance().removeClient(player);
   }

   static {
      network = NetworkRegistry.INSTANCE.newSimpleChannel("warpbook");
      guiIndex = 42;
      deathPagesEnabled = true;
      fuelEnabled = false;
      WarpBookWarpGuiIndex = guiIndex++;
      WarpBookWaypointGuiIndex = guiIndex++;
      WarpBookInventoryGuiIndex = guiIndex++;
      lastHeldBooks = new HashMap();
      formingPages = new HashMap();
      tabBook = new CreativeTabs("tabWarpBook") {
         @SideOnly(Side.CLIENT)
         public Item func_78016_d() {
            return WarpBookMod.warpBookItem;
         }
      };
   }
}
