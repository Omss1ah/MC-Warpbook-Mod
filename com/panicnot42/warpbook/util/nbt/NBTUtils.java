package com.panicnot42.warpbook.util.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtils {
   public static <T extends INBTSerializable> HashMap<String, T> readHashMapFromNBT(NBTTagList tag, Class<T> clazz) {
      HashMap<String, T> map = new HashMap();

      for(int i = 0; i < tag.func_74745_c(); ++i) {
         INBTSerializable obj;
         try {
            obj = (INBTSerializable)clazz.newInstance();
         } catch (InstantiationException var6) {
            throw new RuntimeException(var6);
         } catch (IllegalAccessException var7) {
            throw new RuntimeException(var7);
         }

         obj.readFromNBT(tag.func_150305_b(i).func_74775_l("value"));
         map.put(tag.func_150305_b(i).func_74779_i("name"), obj);
      }

      return map;
   }

   public static <T extends INBTSerializable> void writeHashMapToNBT(NBTTagList tag, HashMap<String, T> map) {
      Iterator i$ = map.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, T> e = (Entry)i$.next();
         NBTTagCompound comp = new NBTTagCompound();
         NBTTagCompound value = new NBTTagCompound();
         ((INBTSerializable)e.getValue()).writeToNBT(value);
         comp.func_74778_a("name", (String)e.getKey());
         comp.func_74782_a("value", value);
         tag.func_74742_a(comp);
      }

   }

   public static <T extends INBTSerializable> ArrayList<T> readArrayListFromNBT(NBTTagList tag, Class<T> clazz) {
      ArrayList<T> array = new ArrayList();

      for(int i = 0; i < tag.func_74745_c(); ++i) {
         INBTSerializable obj;
         try {
            obj = (INBTSerializable)clazz.newInstance();
         } catch (InstantiationException var6) {
            throw new RuntimeException(var6);
         } catch (IllegalAccessException var7) {
            throw new RuntimeException(var7);
         }

         obj.readFromNBT(tag.func_150305_b(i));
      }

      return array;
   }

   public static <T extends INBTSerializable> void writeArrayListToNBT(NBTTagList tag, ArrayList<T> array) {
      Iterator i$ = array.iterator();

      while(i$.hasNext()) {
         T e = (INBTSerializable)i$.next();
         NBTTagCompound compTag = new NBTTagCompound();
         e.writeToNBT(compTag);
         tag.func_74742_a(compTag);
      }

   }
}
