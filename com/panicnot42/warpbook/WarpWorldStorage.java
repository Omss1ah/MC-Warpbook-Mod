package com.panicnot42.warpbook;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WarpWorldStorage extends WorldSavedData {
   public static HashMap<String, Waypoint> table;
   public static HashMap<UUID, Waypoint> deaths;
   public static ArrayList<GameProfile> profiles;
   private static final String IDENTIFIER = "WarpBook";

   public WarpWorldStorage(String identifier) {
      super(identifier);
   }

   public static WarpWorldStorage instance(World world) {
      if (world.field_72988_C.func_75742_a(WarpWorldStorage.class, "WarpBook") == null) {
         world.field_72988_C.func_75745_a("WarpBook", new WarpWorldStorage("WarpBook"));
      }

      WarpWorldStorage storage = (WarpWorldStorage)world.field_72988_C.func_75742_a(WarpWorldStorage.class, "WarpBook");
      return storage;
   }

   public static void postInit() {
      table = new HashMap();
      deaths = new HashMap();
      profiles = new ArrayList();
   }

   public void func_76184_a(NBTTagCompound var1) {
      table = NBTUtils.readHashMapFromNBT(var1.func_150295_c("data", 10), Waypoint.class);
      HashMap<String, Waypoint> deaths = NBTUtils.readHashMapFromNBT(var1.func_150295_c("deaths", 10), Waypoint.class);
      WarpWorldStorage.deaths = new HashMap();
      Iterator i$ = deaths.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, Waypoint> death = (Entry)i$.next();
         WarpWorldStorage.deaths.put(UUID.fromString((String)death.getKey()), death.getValue());
      }

      NBTTagList players = var1.func_150295_c("players", 10);

      for(int i = 0; i < players.func_74745_c(); ++i) {
         NBTTagCompound tag = players.func_150305_b(i);
         profiles.add(new GameProfile(new UUID(tag.func_74763_f("least"), tag.func_74763_f("most")), tag.func_74779_i("name")));
      }

   }

   public void func_76187_b(NBTTagCompound var1) {
      NBTUtils.writeHashMapToNBT(var1.func_150295_c("data", 10), table);
      HashMap<String, Waypoint> deaths = new HashMap();
      Iterator i$ = WarpWorldStorage.deaths.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<UUID, Waypoint> death = (Entry)i$.next();
         deaths.put(((UUID)death.getKey()).toString(), death.getValue());
      }

      NBTUtils.writeHashMapToNBT(var1.func_150295_c("deaths", 10), deaths);
      NBTTagList players = new NBTTagList();
      Iterator i$ = profiles.iterator();

      while(i$.hasNext()) {
         GameProfile profile = (GameProfile)i$.next();
         NBTTagCompound profTag = new NBTTagCompound();
         profTag.func_74772_a("least", profile.getId().getLeastSignificantBits());
         profTag.func_74772_a("most", profile.getId().getMostSignificantBits());
         profTag.func_74778_a("name", profile.getName());
      }

      var1.func_74782_a("players", players);
   }

   void updateClient(EntityPlayerMP player, ServerConnectionFromClientEvent e) {
      FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel("warpbook", Side.SERVER);
      channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.DISPATCHER);
      channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(e.manager));
      channel.writeAndFlush(new PacketSyncWaypoints(table)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
   }

   public boolean waypointExists(String name) {
      return table.containsKey(name);
   }

   public Waypoint getWaypoint(String name) {
      return (Waypoint)table.get(name);
   }

   public void addWaypoint(Waypoint point) {
      table.put(point.name, point);
      this.func_76185_a();
   }

   public String[] listWaypoints() {
      return (String[])((String[])table.keySet().toArray());
   }

   public boolean deleteWaypoint(String waypoint) {
      this.func_76185_a();
      return table.remove(waypoint) != null;
   }

   public void setLastDeath(UUID id, double posX, double posY, double posZ, int dim) {
      deaths.put(id, new Waypoint("Death Point", "death", MathUtils.round(posX, RoundingMode.DOWN), MathUtils.round(posY, RoundingMode.DOWN), MathUtils.round(posZ, RoundingMode.DOWN), dim));
      this.func_76185_a();
   }

   public void clearLastDeath(UUID id) {
      deaths.remove(id);
      this.func_76185_a();
   }

   public static Waypoint getLastDeath(UUID id) {
      return (Waypoint)deaths.get(id);
   }
}
