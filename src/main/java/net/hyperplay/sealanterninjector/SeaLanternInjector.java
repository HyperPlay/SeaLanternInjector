package net.hyperplay.sealanterninjector;

import com.google.common.collect.Maps;
import net.hyperplay.sealanterninjector.block.BlockSeaLantern;
import net.hyperplay.sealanterninjector.util.ReflectionUtil;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.ItemBlock;
import net.minecraft.server.v1_7_R4.StepSound;
import org.bukkit.Material;
import org.bukkit.util.Java15Compat;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class SeaLanternInjector {

    public static Block inject() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        //Make NMS Block & Item.
        Block sea_lantern = new BlockSeaLantern();
        ReflectionUtil.invokeMethod(Block.class, sea_lantern, "c", new Class[]{float.class}, new Object[]{0.3F});
        ReflectionUtil.invokeMethod(Block.class, sea_lantern, "a", new Class[]{StepSound.class}, new Object[]{Block.k});
        ReflectionUtil.invokeMethod(Block.class, sea_lantern, "a", new Class[]{float.class}, new Object[]{1.0F});
        ReflectionUtil.invokeMethod(Block.class, sea_lantern, "c", new Class[]{String.class}, new Object[]{"seaLantern"});
        Block.REGISTRY.a(169, "sea_lantern", sea_lantern);
        Item.REGISTRY.a(169, "sea_lantern", new ItemBlock(sea_lantern));

        //Inject into org.bukkit.Material
        Material[] oldValues = (Material[]) ReflectionUtil.getFieldValue(Material.class, Material.class, "$VALUES");
        Material[] newValues = new Material[oldValues.length + 1];
        System.arraycopy(oldValues, 0, newValues, 0, oldValues.length); //copy over
        Material sl = (Material) ReflectionUtil.invokeEnumConstructor(Material.class, new Class[]{
                String.class, Integer.TYPE, Integer.TYPE
        }, new Object[]{"SEA_LANTERN", 169, 169});
        newValues[newValues.length - 1] = sl;
        ReflectionUtil.setStaticFieldValue(Material.class, "$VALUES", newValues);

        //Fix lookup methods.
        Material[] byId = new Material[383];
        Map<String, Material> BY_NAME = Maps.newHashMap();
        for (Material material : Material.values()) {
            if (byId.length > material.getId()) {
                byId[material.getId()] = material;
            } else {
                byId = Java15Compat.Arrays_copyOfRange(byId, 0, material.getId() + 2);
                byId[material.getId()] = material;
            }
            BY_NAME.put(material.name(), material);
        }
        ReflectionUtil.setStaticFieldValue(Material.class, "byId", byId);
        ReflectionUtil.setStaticFieldValue(Material.class, "BY_NAME", BY_NAME);

        return sea_lantern;
    }

}
