package net.mrqx.sbr_inme.event;

import mods.flammpfeil.slashblade.event.bladestand.ProudSoulEnchantmentEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrqx.sbr_inme.SlashBladeResharpedINeedMoreEnchantmentConfig;

import java.util.Objects;

@EventBusSubscriber
public class SlashBladeEnchantHandler {
    @SuppressWarnings("null")
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onProudSoulEnchantmentEvent(ProudSoulEnchantmentEvent event) {
        Enchantment enchantment = event.getEnchantment();
        int maxLevel = enchantment.getMaxLevel();
        Player player = null;
        if (event.getOriginalEvent() != null) {
            player = (Player) event.getOriginalEvent().getDamageSource().getEntity();
        }
        if (player == null) {
            return;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack blade = event.getBlade();
        Double probability = 1.0;

        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_CHANCE_MAP
                .containsKey(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString())) {
            probability = SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_CHANCE_MAP
                    .get(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString());
        }
        event.setProbability(probability.floatValue());

        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_MULTIPLIER_MAP.containsKey(
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString())) {
            maxLevel = (int) (maxLevel * SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_MULTIPLIER_MAP
                    .get(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString()));
        }

        int bonusLevel = 1;

        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_BONUS_MAP.containsKey(
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString())) {
            bonusLevel = bonusLevel + SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_BONUS_MAP
                    .get(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString());
        }

        int enchantLevel = EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade) <= 1 ? 1
                : Math.min(Math.max(maxLevel, EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade)),
                EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade) + bonusLevel);

        event.setEnchantLevel(enchantLevel);
    }
}
