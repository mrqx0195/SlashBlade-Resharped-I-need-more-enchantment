package net.mrqx.sbr_inme.mixin;

import java.util.Map;
import mods.flammpfeil.slashblade.data.tag.SlashBladeItemTags;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.event.bladestand.BlandStandEventHandler;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrqx.sbr_inme.SlashBladeResharpedINeedMoreEnchantment;
import net.mrqx.sbr_inme.SlashBladeResharpedINeedMoreEnchantmentConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlandStandEventHandler.class)
public abstract class MixinBlandStandEventHandler {
    @SuppressWarnings("null")
    @Overwrite(remap = false)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventProudSoulEnchantment(SlashBladeEvent.BladeStandAttackEvent event) {
        if (event.getDamageSource().getEntity() instanceof Player) {
            Player player = (Player) event.getDamageSource().getEntity();
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack blade = event.getBlade();
            if (!blade.isEmpty()) {
                if (stack.is(SlashBladeItemTags.PROUD_SOULS)) {
                    if (stack.isEnchanted()) {
                        Level world = player.level();
                        RandomSource random = world.getRandom();
                        BladeStandEntity bladeStand = event.getBladeStand();
                        Map<Enchantment, Integer> currentBladeEnchantments = blade.getAllEnchantments();
                        stack.getAllEnchantments().forEach((enchantment, level) -> {
                            if (!event.isCanceled()) {
                                if (blade.canApplyAtEnchantingTable(enchantment)) {
                                    Double probability = 1.0;

                                    if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_CHANCE_MAP
                                            .containsKey(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
                                        probability = (Double) (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_CHANCE_MAP
                                                .get(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString()));
                                    }

                                    if (random.nextDouble() <= probability) {
                                        int maxLevel = enchantment.getMaxLevel();

                                        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_MULTIPLIER_MAP
                                                .containsKey(
                                                        ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
                                            maxLevel = (int) (maxLevel
                                                    * SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_MULTIPLIER_MAP
                                                            .get(ForgeRegistries.ITEMS.getKey(stack.getItem())
                                                                    .toString()));
                                        }

                                        int bonusLevel = 1;

                                        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_BONUS_MAP
                                                .containsKey(
                                                        ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
                                            bonusLevel = bonusLevel
                                                    + SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_BONUS_MAP
                                                            .get(ForgeRegistries.ITEMS.getKey(stack.getItem())
                                                                    .toString());
                                        }

                                        int enchantLevel = Math.min(
                                                Math.max(maxLevel,
                                                        EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade)),
                                                EnchantmentHelper.getTagEnchantmentLevel(enchantment, blade)
                                                        + bonusLevel);

                                        SlashBladeResharpedINeedMoreEnchantment.LOGGER
                                                .debug("probability:{}, maxLevel:{}, bonusLevel:{}, enchantLevel:{}",
                                                        probability,
                                                        maxLevel,
                                                        bonusLevel,
                                                        enchantLevel);

                                        currentBladeEnchantments.put(enchantment, enchantLevel);
                                        EnchantmentHelper.setEnchantments(currentBladeEnchantments, blade);
                                        world.playSound(bladeStand, bladeStand.getPos(), SoundEvents.WITHER_SPAWN,
                                                SoundSource.BLOCKS, 1.0F, 1.0F);

                                        for (int i = 0; i < 32 && !player.level().isClientSide(); ++i) {
                                            double xDist = (double) (random.nextFloat() * 2.0F - 1.0F);
                                            double yDist = (double) (random.nextFloat() * 2.0F - 1.0F);
                                            double zDist = (double) (random.nextFloat() * 2.0F - 1.0F);
                                            if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0)) {
                                                double x = bladeStand.getX(xDist / 4.0);
                                                double y = bladeStand.getY(0.5 + yDist / 4.0);
                                                double z = bladeStand.getZ(zDist / 4.0);
                                                ((ServerLevel) world).sendParticles(ParticleTypes.PORTAL, x, y, z, 0,
                                                        xDist, yDist + 0.2, zDist, 1.0);
                                            }
                                        }
                                    }

                                    if (!player.isCreative()) {
                                        stack.shrink(1);
                                    }

                                    event.setCanceled(true);
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
