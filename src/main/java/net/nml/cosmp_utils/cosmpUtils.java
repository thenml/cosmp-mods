package net.nml.cosmp_utils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.nml.cosmp_utils.command.SetNameColorCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class cosmpUtils implements ModInitializer {
	public static final String MODID = "cosmp_utils";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("starting cosmp_utils v2");
		CommandRegistrationCallback.EVENT.register(SetNameColorCommand::register);
	}
}