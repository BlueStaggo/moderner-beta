package mod.bluestaggo.modernerbeta.command;

import com.mojang.brigadier.CommandDispatcher;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class DebugProviderSettingsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("printprovidersettings")
            .requires(
                //? if >=1.21.11 {
                /*Commands.hasPermission(Commands.LEVEL_ADMINS)
                *///? } else {
                source -> source.hasPermission(2)
                //? }
            )
            .executes(ctx -> execute(ctx.getSource())));
    }
    
    private static int execute(CommandSourceStack source) {
        boolean validWorld = false;
        
        if (source.getLevel().getChunkSource().getGenerator() instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            validWorld = true;
            
            source.sendSuccess(() -> Component.literal("Chunk Provider Settings:").withStyle(ChatFormatting.YELLOW), false);
            source.sendSuccess(() -> Component.literal(modernBetaChunkGenerator.getChunkSettings().toString()), false);
        }
        
        if (source.getLevel().getChunkSource().getGenerator().getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            validWorld = true;

            source.sendSuccess(() -> Component.literal("Biome Provider Settings:").withStyle(ChatFormatting.YELLOW), false);
            source.sendSuccess(() -> Component.literal(modernBetaBiomeSource.getBiomeSettings().toString()), false);
            
            source.sendSuccess(() -> Component.literal("Cave Biome Provider Settings:").withStyle(ChatFormatting.YELLOW), false);
            source.sendSuccess(() -> Component.literal(modernBetaBiomeSource.getCaveBiomeSettings().toString()), false);
        }

        if (validWorld) {
            return 0;
        } 

        source.sendSuccess(() -> Component.literal("Not a Modern Beta world!").withStyle(ChatFormatting.RED), false);
        
        return -1;
    }
}
