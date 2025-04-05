package net.mrqx.sbr_inme.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrqx.sbr_core.events.MrqxSlashBladeEvents;
import net.mrqx.sbr_inme.SlashBladeResharpedINeedMoreEnchantmentConfig;

@EventBusSubscriber
public class SlashBladeEnchantHandler {
    @SuppressWarnings("null")
    @SubscribeEvent
    public static void onProudSoulEnchantmentEvent(MrqxSlashBladeEvents.ProudSoulEnchantmentEvent event) {
        Enchantment enchantment = event.getEnchantment();
        int maxLevel = enchantment.getMaxLevel();
        Player player = (Player) event.getOriginalEvent().getDamageSource().getEntity();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack blade = event.getBlade();

        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_MULTIPLIER_MAP.containsKey(
                ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
            maxLevel = (int) (maxLevel * SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_MULTIPLIER_MAP
                    .get(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString()));
        }

        int bonusLevel = 1;

        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_BONUS_MAP.containsKey(
                ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
            bonusLevel = bonusLevel + SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_BONUS_MAP
                    .get(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        }

        int enchantLevel = EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade) <= 1 ? 1
                : Math.min(Math.max(maxLevel, EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade)),
                        EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade) + bonusLevel);

        event.setEnchantLevel(enchantLevel);
    }
}
