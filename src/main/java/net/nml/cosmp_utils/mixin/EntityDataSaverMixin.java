package net.nml.cosmp_utils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import net.nml.cosmp_utils.util.IEntityDataSaver;

@Mixin(net.minecraft.entity.Entity.class)
public abstract class EntityDataSaverMixin implements IEntityDataSaver {
	private NbtCompound persistentData;

	@Override
	public NbtCompound getPersistentData() {
		if (this.persistentData == null) {
			this.persistentData = new NbtCompound();
			this.persistentData.putInt("start_color", Formatting.WHITE.getColorValue());
			this.persistentData.putInt("end_color", Formatting.GRAY.getColorValue());
		}

		return persistentData;
	}

	// https://github.com/Tutorials-By-Kaupenjoe/Fabric-Tutorial-1.19/blob/16-DataOnThePlayer/src/main/java/net/kaupenjoe/tutorialmod/mixin/ModEntityDataSaverMixin.java
    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(persistentData != null) {
            nbt.put("cosmp_utils.data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("cosmp_utils.data", 10)) {
            persistentData = nbt.getCompound("cosmp_utils.data");
        }
    }
}
