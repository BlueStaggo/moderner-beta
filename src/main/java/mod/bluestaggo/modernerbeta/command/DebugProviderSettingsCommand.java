package mod.bluestaggo.modernerbeta.command;

import com.mojang.brigadier.CommandDispatcher;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DebugProviderSettingsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("printprovidersettings")
            .requires(source -> source.hasPermissionLevel(2))
                .executes(ctx -> execute(ctx.getSource())));
    }
    
    private static int execute(ServerCommandSource source) {
        boolean validWorld = false;
        
        if (source.getWorld().getChunkManager().getChunkGenerator() instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            validWorld = true;
            
            source.sendFeedback(() -> Text.literal("Chunk Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(() -> Text.literal(modernBetaChunkGenerator.getChunkSettings().toString()), false);
        }
        
        if (source.getWorld().getChunkManager().getChunkGenerator().getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            validWorld = true;

            source.sendFeedback(() -> Text.literal("Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(() -> Text.literal(modernBetaBiomeSource.getBiomeSettings().toString()), false);
            
            source.sendFeedback(() -> Text.literal("Cave Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(() -> Text.literal(modernBetaBiomeSource.getCaveBiomeSettings().toString()), false);
        }

        if (validWorld) {
            return 0;
        } 

        source.sendFeedback(() -> Text.literal("Not a Modern Beta world!").formatted(Formatting.RED), false);
        
        return -1;
    }
}
