package net.mrqx.sbr_inme.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrqx.sbr_core.events.handler.BladeStandEventHandler;
import net.mrqx.sbr_core.events.MrqxSlashBladeEvents;
import net.mrqx.sbr_inme.SlashBladeResharpedINeedMoreEnchantmentConfig;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BladeStandEventHandler.class)
public abstract class MixinBladeStandEventHandler {
    @SuppressWarnings("null")
    @Redirect(method = "proudSoulEnchantmentProbabilityCheck(Lnet/mrqx/sbr_core/events/MrqxSlashBladeEvents$ProudSoulEnchantmentEvent;)V", at = @At(value = "INVOKE", target = "Lnet/mrqx/sbr_core/events/MrqxSlashBladeEvents$ProudSoulEnchantmentEvent;getProbability()F"), remap = false)
    private static float injectProudSoulEnchantmentProbabilityCheck(
            MrqxSlashBladeEvents.ProudSoulEnchantmentEvent event) {

        Player player = (Player) event.getOriginalEvent().getDamageSource().getEntity();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Double probability = 1.0;

        if (SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_CHANCE_MAP
                .containsKey(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
            probability = SlashBladeResharpedINeedMoreEnchantmentConfig.PROUDSOUL_ITEM_CHANCE_MAP
                    .get(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        }
        return probability.floatValue();
    }
}
