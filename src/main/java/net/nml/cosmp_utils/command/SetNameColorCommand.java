package net.nml.cosmp_utils.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.nml.cosmp_utils.util.ColorData;
import net.nml.cosmp_utils.util.IEntityDataSaver;

public final class SetNameColorCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment){
		dispatcher.register(
			literal("color").then(
				literal("from").then(
					argument("color", ColorArgumentType.color()).executes(
						ctx -> setColor(
							ctx.getSource(),
							"start_color",
							ColorArgumentType.getColor(ctx, "color")
						))
					.then(
						argument("target", EntityArgumentType.player()).executes(
							ctx -> setColor(
								ctx.getSource(),
								"start_color",
								ColorArgumentType.getColor(ctx, "color"),
								EntityArgumentType.getPlayer(ctx, "target")
							))
						)
					))
			.then(
				literal("int_from").then(
					argument("color", IntegerArgumentType.integer(0, 16777215)).executes(
						ctx -> setColor(
							ctx.getSource(),
							"start_color",
							IntegerArgumentType.getInteger(ctx, "color")
						))
					.then(
						argument("target", EntityArgumentType.player()).executes(
							ctx -> setColor(
								ctx.getSource(),
								"start_color",
								IntegerArgumentType.getInteger(ctx, "color"),
								EntityArgumentType.getPlayer(ctx, "target")
							))
						)
					))
			.then(
				literal("to").then(
					argument("color", ColorArgumentType.color()).executes(
						ctx -> setColor(
							ctx.getSource(),
							"end_color",
							ColorArgumentType.getColor(ctx, "color")
						))
					.then(
						argument("target", EntityArgumentType.player()).executes(
							ctx -> setColor(
								ctx.getSource(),
								"end_color",
								ColorArgumentType.getColor(ctx, "color"),
								EntityArgumentType.getPlayer(ctx, "target")
							))
						)
					))
			.then(
				literal("int_to").then(
					argument("color", IntegerArgumentType.integer(0, 16777215)).executes(
						ctx -> setColor(
							ctx.getSource(),
							"end_color",
							IntegerArgumentType.getInteger(ctx, "color")
						))
					.then(
						argument("target", EntityArgumentType.player()).executes(
							ctx -> setColor(
								ctx.getSource(),
								"end_color",
								IntegerArgumentType.getInteger(ctx, "color"),
								EntityArgumentType.getPlayer(ctx, "target")
							))
						)
					))
			.then(
				literal("reset").executes(
					ctx -> resetColor(
						ctx.getSource()
					))
				.then(
					argument("target", EntityArgumentType.player()).executes(
						ctx -> resetColor(
							ctx.getSource(),
							EntityArgumentType.getPlayer(ctx, "target")
						))
					)
			));
	}
	private static int resetColor(ServerCommandSource source) throws CommandSyntaxException {
		return resetColor(source, source.getPlayerOrThrow());
	}
	private static int resetColor(ServerCommandSource source, ServerPlayerEntity player) {
		ColorData.resetColor((IEntityDataSaver) player);
		player.sendMessage(Text.translatable("Color reset to: ").append(player.getDisplayName()));
		return 1;
	}

    private static int setColor(ServerCommandSource source, String tag, Formatting color) throws CommandSyntaxException {
		return setColor(source, tag, color.getColorValue(), source.getPlayerOrThrow());
	}
    private static int setColor(ServerCommandSource source, String tag, Formatting color, ServerPlayerEntity player) {
		return setColor(source, tag, color.getColorValue(), player);
	}
    private static int setColor(ServerCommandSource source, String tag, int color) throws CommandSyntaxException {
		return setColor(source, tag, color, source.getPlayerOrThrow());
	}
    private static int setColor(ServerCommandSource source, String tag, int color, ServerPlayerEntity player) {
		// MutableText playerName = MutableText.of(player.getName().getContent());
		// MutableText coloredName = playerName.formatted(color);
		ColorData.setData((IEntityDataSaver) player, tag, color);
		player.sendMessage(Text.translatable("Color changed to: ").append(player.getDisplayName()));
        return 1;
	}
}
