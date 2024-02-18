package com.panicnot42.warpbook.util;

import com.panicnot42.warpbook.util.nbt.INBTSerializable;
import net.minecraft.nbt.NBTTagCompound;

public class Waypoint implements INBTSerializable {
   public String friendlyName;
   public String name;
   public int x;
   public int y;
   public int z;
   public int dim;

   public Waypoint(String friendlyName, String name, int x, int y, int z, int dim) {
      this.friendlyName = friendlyName;
      this.name = name;
      this.x = x;
      this.y = y;
      this.z = z;
      this.dim = dim;
   }

   public Waypoint(NBTTagCompound var1) {
      this.readFromNBT(var1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.friendlyName = var1.func_74779_i("friendlyName");
      this.x = var1.func_74762_e("x");
      this.y = var1.func_74762_e("y");
      this.z = var1.func_74762_e("z");
      this.dim = var1.func_74762_e("dim");
      this.name = var1.func_74779_i("name");
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.func_74778_a("friendlyName", this.friendlyName);
      var1.func_74768_a("x", this.x);
      var1.func_74768_a("y", this.y);
      var1.func_74768_a("z", this.z);
      var1.func_74768_a("dim", this.dim);
      var1.func_74778_a("name", this.name);
   }
}
