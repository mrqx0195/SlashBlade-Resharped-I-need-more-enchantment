package net.mrqx.sbr_inme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SlashBladeResharpedINeedMoreEnchantment.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlashBladeResharpedINeedMoreEnchantmentConfig {
        public static ForgeConfigSpec COMMON_CONFIG;
        public static ForgeConfigSpec.ConfigValue<List<? extends List<?>>> PROUDSOUL_ITEM_CHANCE_LIST;
        public static ForgeConfigSpec.ConfigValue<List<? extends List<?>>> PROUDSOUL_ITEM_MULTIPLIER_LIST;
        public static ForgeConfigSpec.ConfigValue<List<? extends List<?>>> PROUDSOUL_ITEM_BONUS_LIST;

        public static Map<String, Double> PROUDSOUL_ITEM_CHANCE_MAP = new HashMap<String, Double>();
        public static Map<String, Double> PROUDSOUL_ITEM_MULTIPLIER_MAP = new HashMap<String, Double>();
        public static Map<String, Integer> PROUDSOUL_ITEM_BONUS_MAP = new HashMap<String, Integer>();

        static {
                ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
                COMMON_BUILDER.comment("Enchantment chance settings").push("enchantment");

                PROUDSOUL_ITEM_CHANCE_LIST = COMMON_BUILDER
                                .comment("Set the chance for each item to be successfully enchanted.")
                                .comment("Any item with the \"slashblade:proudsouls \" tag that isn't listed here will always succeed (100% chance).")
                                .comment("Items you’ve set up without this tag can’t be used for enchanting.")
                                .comment("Range: [0.0,1.0]")
                                .defineListAllowEmpty(List.of("Proudsoul_Item_Chance_List"),
                                                List.of(
                                                                List.of("slashblade:proudsoul_tiny", 0.25),
                                                                List.of("slashblade:proudsoul", 0.50),
                                                                List.of("slashblade:proudsoul_ingot", 0.75),
                                                                List.of("slashblade:proudsoul_sphere", 1.0),
                                                                List.of("slashblade:proudsoul_crystal", 1.0),
                                                                List.of("slashblade:proudsoul_trapezohedron", 1.0)),
                                                it -> it instanceof List<?> list && list.size() == 2
                                                                && list.get(0) instanceof String
                                                                && list.get(1) instanceof Double
                                                                && (Double) (list.get(1)) >= 0.0
                                                                && (Double) (list.get(1)) <= 1.0);

                PROUDSOUL_ITEM_MULTIPLIER_LIST = COMMON_BUILDER
                                .comment("Set how many times an item can exceed the enchantment level cap during enchanting.(doesn't affect enchantments with max level 1)")
                                .comment("Range: [0.0,)")
                                .defineListAllowEmpty(List.of("Proudsoul_Item_Multiplier_List"),
                                                List.of(
                                                                List.of("slashblade:proudsoul_tiny", 1.0),
                                                                List.of("slashblade:proudsoul", 1.0),
                                                                List.of("slashblade:proudsoul_ingot", 2.0),
                                                                List.of("slashblade:proudsoul_sphere", 2.0),
                                                                List.of("slashblade:proudsoul_crystal", 2.0),
                                                                List.of("slashblade:proudsoul_trapezohedron", 2.0)),
                                                it -> it instanceof List<?> list && list.size() == 2
                                                                && list.get(0) instanceof String
                                                                && list.get(1) instanceof Double
                                                                && (Double) (list.get(1)) >= 0.0
                                                                && (Double) (list.get(1)) <= Double.MAX_VALUE);

                PROUDSOUL_ITEM_BONUS_LIST = COMMON_BUILDER
                                .comment("Set a extra bonus enchantment level boost for each item during enchanting.")
                                .comment("Range: [0,)")
                                .defineListAllowEmpty(List.of("Proudsoul_Item_Bonus_List"),
                                                List.of(
                                                                List.of("slashblade:proudsoul_tiny", 0),
                                                                List.of("slashblade:proudsoul", 0),
                                                                List.of("slashblade:proudsoul_ingot", 0),
                                                                List.of("slashblade:proudsoul_sphere", 1),
                                                                List.of("slashblade:proudsoul_crystal", 1),
                                                                List.of("slashblade:proudsoul_trapezohedron", 1)),
                                                it -> it instanceof List<?> list && list.size() == 2
                                                                && list.get(0) instanceof String
                                                                && list.get(1) instanceof Integer
                                                                && (Integer) (list.get(1)) >= 0
                                                                && (Integer) (list.get(1)) <= Integer.MAX_VALUE);

                COMMON_BUILDER.pop();
                COMMON_CONFIG = COMMON_BUILDER.build();
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
                PROUDSOUL_ITEM_CHANCE_LIST.get().forEach(chance -> {
                        PROUDSOUL_ITEM_CHANCE_MAP.put((String) (chance.get(0)), (Double) (chance.get(1)));
                });
                PROUDSOUL_ITEM_MULTIPLIER_LIST.get().forEach(chance -> {
                        PROUDSOUL_ITEM_MULTIPLIER_MAP.put((String) (chance.get(0)), (Double) (chance.get(1)));
                });
                PROUDSOUL_ITEM_BONUS_LIST.get().forEach(chance -> {
                        PROUDSOUL_ITEM_BONUS_MAP.put((String) (chance.get(0)), (Integer) (chance.get(1)));
                });
        }
}
