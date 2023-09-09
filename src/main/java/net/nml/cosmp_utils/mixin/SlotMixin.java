package net.nml.cosmp_utils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Mixin(net.minecraft.screen.slot.Slot.class)
public class SlotMixin {
	@Shadow
	public Inventory inventory;

	@Inject(method = "canInsert(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void modifyCanInsert(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		if (this.inventory instanceof EnderChestInventory && !stack.getItem().canBeNested())
        	info.setReturnValue(false);
    }
}
