package net.mrqx.sbr_inme;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import org.slf4j.Logger;

@Mod(SlashBladeResharpedINeedMoreEnchantment.MODID)
public class SlashBladeResharpedINeedMoreEnchantment {
    public static final String MODID = "sbr_inme";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SlashBladeResharpedINeedMoreEnchantment() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,
                SlashBladeResharpedINeedMoreEnchantmentConfig.COMMON_CONFIG);
    }
}
