package com.panicnot42.warpbook.util.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSerializable {
   void readFromNBT(NBTTagCompound var1);

   void writeToNBT(NBTTagCompound var1);
}
