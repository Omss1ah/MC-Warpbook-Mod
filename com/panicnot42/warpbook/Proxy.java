package com.panicnot42.warpbook;

import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.Waypoint;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Proxy {
   public void registerRenderers() {
   }

   public void handleWarp(EntityPlayer player, ItemStack page) {
      if (!player.field_70170_p.field_72995_K) {
         if (page == null) {
            return;
         }

         Waypoint wp = this.extractWaypoint(player, page);
         if (wp == null) {
            if (player.field_70170_p.field_72995_K && page.func_77960_j() != 5) {
               CommandUtils.showError(player, I18n.func_135052_a("help.waypointnotexist", new Object[0]));
            }

            return;
         }

         boolean crossDim = player.field_71093_bK != wp.dim;
         PacketEffect oldDim = new PacketEffect(true, MathUtils.round(player.field_70165_t, RoundingMode.DOWN), MathUtils.round(player.field_70163_u, RoundingMode.DOWN), MathUtils.round(player.field_70161_v, RoundingMode.DOWN));
         PacketEffect newDim = new PacketEffect(false, wp.x, wp.y, wp.z);
         TargetPoint oldPoint = new TargetPoint(player.field_71093_bK, player.field_70165_t, player.field_70163_u, player.field_70161_v, 64.0D);
         TargetPoint newPoint = new TargetPoint(wp.dim, (double)wp.x, (double)wp.y, (double)wp.z, 64.0D);
         player.func_71020_j(calculateExhaustion(player.func_130014_f_().field_73013_u, WarpBookMod.exhaustionCoefficient, crossDim));
         if (crossDim && !player.field_70170_p.field_72995_K) {
            transferPlayerToDimension((EntityPlayerMP)player, wp.dim, ((EntityPlayerMP)player).field_71133_b.func_71203_ab());
         }

         player.func_70634_a((double)((float)wp.x - 0.5F), (double)((float)wp.y + 0.5F), (double)((float)wp.z - 0.5F));
         WarpBookMod.network.sendToAllAround(oldDim, oldPoint);
         WarpBookMod.network.sendToAllAround(newDim, newPoint);
      }

   }

   protected Waypoint extractWaypoint(EntityPlayer player, ItemStack page) {
      NBTTagCompound pageTagCompound = page.func_77978_p();
      WarpWorldStorage storage = WarpWorldStorage.instance(player.func_130014_f_());
      Waypoint wp;
      if (pageTagCompound.func_74764_b("hypername")) {
         wp = storage.getWaypoint(pageTagCompound.func_74779_i("hypername"));
      } else if (pageTagCompound.func_74764_b("playeruuid") && PlayerUtils.isPlayerOnline(UUID.fromString(pageTagCompound.func_74779_i("playeruuid")))) {
         if (player.field_70170_p.field_72995_K) {
            return null;
         }

         EntityPlayer playerTo = PlayerUtils.getPlayerByUUID(UUID.fromString(pageTagCompound.func_74779_i("playeruuid")));
         if (player == playerTo) {
            return null;
         }

         wp = new Waypoint("", "", MathUtils.round(playerTo.field_70165_t, RoundingMode.DOWN), MathUtils.round(playerTo.field_70163_u, RoundingMode.DOWN), MathUtils.round(playerTo.field_70161_v, RoundingMode.DOWN), playerTo.field_71093_bK);
      } else {
         wp = new Waypoint("", "", pageTagCompound.func_74762_e("posX"), pageTagCompound.func_74762_e("posY"), pageTagCompound.func_74762_e("posZ"), pageTagCompound.func_74762_e("dim"));
      }

      return wp;
   }

   private static float calculateExhaustion(EnumDifficulty difficultySetting, float exhaustionCoefficient, boolean crossDim) {
      float scaleFactor = 0.0F;
      switch(difficultySetting) {
      case EASY:
         scaleFactor = 1.0F;
         break;
      case NORMAL:
         scaleFactor = 1.5F;
         break;
      case HARD:
         scaleFactor = 2.0F;
         break;
      case PEACEFUL:
         scaleFactor = 0.0F;
      }

      return exhaustionCoefficient * scaleFactor * (crossDim ? 2.0F : 1.0F);
   }

   public void goFullPotato(EntityPlayer player, ItemStack itemStack) {
      DamageSource potato = new DamageSource("potato");
      potato.func_76359_i();
      potato.func_76348_h();
      potato.func_151518_m();
      player.field_70170_p.func_72885_a((Entity)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, 12.0F, true, true);
      player.func_70097_a(potato, player.func_110138_aP());
   }

   @SubscribeEvent
   public void onHurt(LivingHurtEvent event) {
      if (WarpBookMod.deathPagesEnabled && event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entity;
         if (event.source != DamageSource.field_76380_i && player.func_110143_aJ() <= event.ammount) {
            ItemStack[] arr$ = player.field_71071_by.field_70462_a;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               ItemStack item = arr$[i$];
               if (item != null && item.func_77973_b() instanceof WarpBookItem && WarpBookItem.getRespawnsLeft(item) > 0) {
                  WarpBookItem.decrRespawnsLeft(item);
                  WarpWorldStorage.instance(player.field_70170_p).setLastDeath(player.func_146103_bH().getId(), player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_71093_bK);
                  break;
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      if (WarpBookMod.deathPagesEnabled) {
         ItemStack page = new ItemStack(WarpBookMod.warpPageItem, 1);
         Waypoint death = WarpWorldStorage.getLastDeath(event.player.func_146103_bH().getId());
         if (death != null) {
            WarpPageItem.writeWaypointToPage(page, WarpWorldStorage.getLastDeath(event.player.func_146103_bH().getId()));
            event.player.field_71071_by.func_70441_a(page);
            WarpWorldStorage.instance(event.player.field_70170_p).clearLastDeath(event.player.func_146103_bH().getId());
         }
      }

   }

   public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld) {
      WorldProvider pOld = oldWorld.field_73011_w;
      WorldProvider pNew = newWorld.field_73011_w;
      double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
      double x = entity.field_70165_t * moveFactor;
      double z = entity.field_70161_v * moveFactor;
      oldWorld.field_72984_F.func_76320_a("placing");
      x = MathHelper.func_151237_a(x, -2.9999872E7D, 2.9999872E7D);
      z = MathHelper.func_151237_a(z, -2.9999872E7D, 2.9999872E7D);
      if (entity.func_70089_S()) {
         entity.func_70012_b(x, entity.field_70163_u, z, entity.field_70177_z, entity.field_70125_A);
         newWorld.func_72838_d(entity);
         newWorld.func_72866_a(entity, false);
      }

      oldWorld.field_72984_F.func_76319_b();
      entity.func_70029_a(newWorld);
   }

   public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager manager) {
      int oldDim = player.field_71093_bK;
      WorldServer worldserver = manager.func_72365_p().func_71218_a(player.field_71093_bK);
      player.field_71093_bK = dimension;
      WorldServer worldserver1 = manager.func_72365_p().func_71218_a(player.field_71093_bK);
      player.field_71135_a.func_147359_a(new S07PacketRespawn(player.field_71093_bK, player.field_70170_p.field_73013_u, player.field_70170_p.func_72912_H().func_76067_t(), player.field_71134_c.func_73081_b()));
      worldserver.func_72973_f(player);
      player.field_70128_L = false;
      transferEntityToWorld(player, worldserver, worldserver1);
      manager.func_72375_a(player, worldserver);
      player.field_71135_a.func_147364_a(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70177_z, player.field_70125_A);
      player.field_71134_c.func_73080_a(worldserver1);
      manager.func_72354_b(player, worldserver1);
      manager.func_72385_f(player);
      Iterator iterator = player.func_70651_bq().iterator();

      while(iterator.hasNext()) {
         PotionEffect potioneffect = (PotionEffect)iterator.next();
         player.field_71135_a.func_147359_a(new S1DPacketEntityEffect(player.func_145782_y(), potioneffect));
      }

      FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
   }
}
