package net.nml.cosmp_utils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.nml.cosmp_utils.util.ColorData;
import net.nml.cosmp_utils.util.IEntityDataSaver;

@Mixin(value = net.minecraft.entity.player.PlayerEntity.class, priority = 99999)
public class PlayerEntityMixin {
    @Shadow
    private MutableText addTellClickEvent(MutableText component){return component;}
	@Shadow
    private GameProfile gameProfile;

    private MutableText coloredName;
    private Integer prev_color1 = -1;
    private Integer prev_color2 = -1;

    private MutableText generateColoredName(Text text){
        NbtCompound persistentData = ((IEntityDataSaver) this).getPersistentData();
        int color1 = persistentData.getInt("start_color");
        int color2 = persistentData.getInt("end_color");

        if (color1 == this.prev_color1 && color2 == this.prev_color2)
            return this.coloredName;
		this.prev_color1 = color1;
		this.prev_color2 = color2;

        String originalName = text.asTruncatedString(16); //this.gameProfile.getName();
        MutableText newName = Text.literal("");

        float[] hsv1 = ColorData.rgbToHSV(color1);
        float[] hsv2 = ColorData.rgbToHSV(color2);

		if (hsv1[1] == 0) hsv1[0] = hsv2[0];
		else if (hsv2[1] == 0) hsv2[0] = hsv1[0];

        float[] step = new float[3];
        for (int i = 0; i < 3; i++) {
            step[i] = (hsv2[i] - hsv1[i]) / (originalName.length() - 1);
        }

        for (int i = 0; i < originalName.length(); i++) {
            float[] currentHSV = new float[3];
            for (int j = 0; j < 3; j++) {
                currentHSV[j] = hsv1[j] + step[j] * i;
            }
            newName.append(Text.literal(String.valueOf(originalName.charAt(i)))
				.setStyle(Style.EMPTY.withColor(ColorData.hsvToRGB(currentHSV))));
        }
        return this.coloredName = newName;
    }

    @ModifyArg(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/Team;decorateName(Lnet/minecraft/scoreboard/AbstractTeam;Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;"))
    private Text replaceName(Text text) {
        return generateColoredName(text);
    }
}
