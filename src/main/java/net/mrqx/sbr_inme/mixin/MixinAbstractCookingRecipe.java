package net.mrqx.sbr_inme.mixin;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractCookingRecipe.class)
public abstract class MixinAbstractCookingRecipe implements Recipe<Container> {
    @Inject(method = "assemble", at = @At(value = "RETURN"), cancellable = true)
    private void injectAssemble(Container p_43746_, RegistryAccess p_267063_, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        Map<Enchantment, Integer> all = Maps.newHashMap();

        for (int idx = 0; idx < p_43746_.getContainerSize(); ++idx) {
            ItemStack stack = p_43746_.getItem(idx);
            if (!stack.isEmpty() && stack.isEnchanted()) {
                Map<Enchantment, Integer> emap = EnchantmentHelper.getEnchantments(stack);
                all.putAll(emap);
            }
        }

        EnchantmentHelper.setEnchantments(all, result);
        cir.setReturnValue(result);
    }
}
