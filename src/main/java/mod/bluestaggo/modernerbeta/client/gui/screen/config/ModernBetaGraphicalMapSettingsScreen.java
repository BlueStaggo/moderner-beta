package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public abstract class ModernBetaGraphicalMapSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    protected static final String KEY = "$MB MAP KEY$.";
    protected static final String VALUE = "$MB MAP VALUE$.";

    private final CompoundTag keys = new CompoundTag();
    private final CompoundTag values = new CompoundTag();

    public ModernBetaGraphicalMapSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext context,
        CompoundTag settings,
        Consumer<CompoundTag> onDone
    ) {
        super(title, parent, context, "list", settings, onDone);

        int i = 0;
        for (String key : this.settings./*? >=1.21.5 {*/keySet/*?} else {*//*getAllKeys*//*?}*/()) {
            String si = String.valueOf(i);
            this.keys.putString(si, key);
            this.values.put(si, this.settings.get(key));
            i++;
        }
    }

    protected abstract List<OptionInstance<?>> getOptions(int i);

    protected abstract String getDefaultKey();

    protected abstract Tag getDefaultValue();

    @Override
    protected CompoundTag getResult() {
        CompoundTag compound = new CompoundTag();
        for (int i = 0; i < this.keys.size(); i++) {
            String si = String.valueOf(i);
            compound.put(VersionCompat.unwrap(this.keys.getString(si)), this.values.get(si));
        }
        return compound;
    }

    @Override
    protected void addOptions(OptionsList list) {
        for (int i = 0; i < this.keys.size(); i++) {
            final int finalI = i;
            String si = String.valueOf(i);

            List<OptionInstance<?>> options = new ArrayList<>(this.getOptions(i));
            if (options.isEmpty()) {
                continue;
            }

            OptionInstance<?> removeButton = this.customButton(
                this.getText("remove"),
                () -> {
                    int size = this.keys.size();
                    this.keys.remove(si);
                    this.values.remove(si);

                    for (int j = finalI; j < size; j++) {
                        String sj = String.valueOf(j);
                        String sjm1 = String.valueOf(j - 1);

                        Tag keyV = this.keys.get(sj);
                        Tag valueV = this.values.get(sj);
                        this.keys.remove(sj);
                        this.values.remove(sj);
                        if (keyV != null && valueV != null) {
                            this.keys.put(sjm1, keyV);
                            this.values.put(sjm1, valueV);
                        }
                    }

                    this.rebuildWidgets();
                }
            );

            options.add(removeButton);
            if (options.size() % 2 == 1) {
                options.add(null);
            }

            for (int j = 0; j < options.size(); j += 2) {
                OptionInstance<?> left = options.get(j);
                OptionInstance<?> right = options.get(j + 1);

                if (right != null) {
                    list.addSmall(new OptionInstance[] {left, right});
                } else {
                    list.addBig(left);
                }
            }
        }

        list.addBig(this.headerOption(Component.empty()));
        list.addBig(this.customButton(
            this.getText("add"),
            () -> {
                String si = String.valueOf(this.keys.size());
                this.keys.putString(si, this.getDefaultKey());
                this.values.put(si, this.getDefaultValue());
                this.rebuildWidgets();
            }
        ));
    }

    @Override
    protected Tuple<CompoundTag, String> resolveSettings(String key) {
        if (key.startsWith(KEY)) {
            return new Tuple<>(keys, key.substring(KEY.length()));
        } else if (key.startsWith(VALUE)) {
            return new Tuple<>(values, key.substring(VALUE.length()));
        }
        return super.resolveSettings(key);
    }

    @FunctionalInterface
    public interface Constructor {
        ModernBetaGraphicalMapSettingsScreen create(
            String title,
            Screen parent,
            WorldCreationContext generatorOptionsHolder,
            CompoundTag settings,
            Consumer<CompoundTag> onDone
        );
    }
}
