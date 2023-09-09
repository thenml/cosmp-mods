package net.nml.cosmp_utils.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;

public class ColorData {
	public static void setData(IEntityDataSaver player, String tag, int color) {
		NbtCompound nbt = player.getPersistentData();
		nbt.putInt(tag, color);
	}

	public static void resetColor(IEntityDataSaver player) {
		NbtCompound nbt = player.getPersistentData();
		nbt.putInt("start_color", Formatting.WHITE.getColorValue());
		nbt.putInt("end_color", Formatting.GRAY.getColorValue());
	}
	
	public static float[] rgbToHSV(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        float[] hsv = new float[3];
        java.awt.Color.RGBtoHSB(red, green, blue, hsv);
        return hsv;
    }

    public static int hsvToRGB(float[] hsv) {
        int rgb = java.awt.Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
        return rgb & 0x00FFFFFF;
    }
}
