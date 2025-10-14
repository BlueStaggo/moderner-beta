//~registryOr
package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.ibm.icu.text.Collator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
//? if >=1.20.2
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaSelectBiomeScreen extends Screen {
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Screen parent;
    private final Consumer<Holder<Biome>> onDone;
    private final boolean allowNone;
    final Registry<Biome> biomeRegistry;
    private BiomeListWidget biomeSelectionList;
    Holder<Biome> biome;
    private Button confirmButton;

    public ModernBetaSelectBiomeScreen(Screen parent, WorldCreationContext generatorOptionsHolder, Consumer<Holder<Biome>> onDone, boolean allowNone) {
        super(Component.translatable("createWorld.customize.modern_beta.title.biome_picker"));
        this.parent = parent;
        this.onDone = onDone;
        this.allowNone = allowNone;
        this.biomeRegistry = generatorOptionsHolder.worldgenLoadContext().lookupOrThrow(Registries.BIOME);
        Holder<Biome> registryEntry = this.biomeRegistry
            .get(Biomes.PLAINS)
            .or(() -> this.biomeRegistry.listElements().findAny())
            .orElseThrow();
        this.biome = generatorOptionsHolder.selectedDimensions()
            .overworld()
            .getBiomeSource()
            .possibleBiomes()
            .stream()
            .findFirst()
            .orElse(registryEntry);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    protected void init() {
        //? if >=1.20.2 {
        LinearLayout header = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
        //?} else {
        /*GridLayout.RowHelper header = this.layout.addToHeader(new GridLayout().columnSpacing(8)).createRowHelper(1);
        *///?}

        header.defaultCellSetting().alignHorizontallyCenter();
        header.addChild(new StringWidget(this.getTitle(), this.font));

        //? if >=1.20.2 {
        this.biomeSelectionList = this.layout.addToContents(new BiomeListWidget());
        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        //?} else {
        /*this.biomeSelectionList = new ModernBetaSelectBiomeScreen.BiomeListWidget();
        this.addRenderableWidget(this.biomeSelectionList);
        GridLayout.RowHelper footer = this.layout.addToFooter(new GridLayout().columnSpacing(8)).createRowHelper(2);
        *///?}

        this.confirmButton = footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.onDone.accept(this.biome);
            this.onClose();
        }).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
        this.biomeSelectionList.setSelected(this.biomeSelectionList
                .children()
                .stream()
                .filter(entry -> Objects.equals(entry.biome, this.biome))
                .findFirst()
                .orElse(null));
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void repositionElements() {
        this.layout.arrangeElements();
        //? if >=1.20.2
        this.biomeSelectionList.updateSize(this.width, this.layout);
    }

    void refreshConfirmButton() {
        this.confirmButton.active = this.biomeSelectionList.getSelected() != null;
    }

    class BiomeListWidget extends ObjectSelectionList<BiomeListWidget.BiomeItem> {
        BiomeListWidget() {
            //? if >=1.20.2 {
            super(ModernBetaSelectBiomeScreen.this.minecraft, ModernBetaSelectBiomeScreen.this.width, ModernBetaSelectBiomeScreen.this.height - 77, 40, 16);
            //?} else {
            /*super(ModernBetaSelectBiomeScreen.this.minecraft, ModernBetaSelectBiomeScreen.this.width, ModernBetaSelectBiomeScreen.this.height, 37, ModernBetaSelectBiomeScreen.this.height - 37, 16);
            *///?}
            Collator collator = Collator.getInstance(Locale.getDefault());
            if (ModernBetaSelectBiomeScreen.this.allowNone)
                this.addEntry(new BiomeItem());
            ModernBetaSelectBiomeScreen.this.biomeRegistry
                    .listElements()
                    .map(BiomeItem::new)
                    .sorted(Comparator.comparing(biome -> biome.text.getString(), collator))
                    .forEach(this::addEntry);
        }

        public void setSelected(@Nullable BiomeItem buffetBiomeItem) {
            super.setSelected(buffetBiomeItem);
            if (buffetBiomeItem != null) {
                ModernBetaSelectBiomeScreen.this.biome = buffetBiomeItem.biome;
            }

            ModernBetaSelectBiomeScreen.this.refreshConfirmButton();
        }

        class BiomeItem extends ObjectSelectionList.Entry<BiomeItem> {
            final Holder.Reference<Biome> biome;
            final Component text;

            public BiomeItem() {
                this.biome = null;
                this.text = Component.translatable("gui.none").withStyle(ChatFormatting.ITALIC);
            }

            public BiomeItem(final Holder.Reference<Biome> biome) {
                this.biome = biome;
                ResourceLocation id = biome.key().location();
                String name = id.toLanguageKey("biome");
                if (Language.getInstance().has(name)) {
                    this.text = Component.translatable(name);
                } else {
                    this.text = Component.literal(id.toString());
                }
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.text);
            }

            @Override
            public void
            //? if >=1.21.9 {
            /*renderContent
            *///? } else {
            render
            //? }
            (GuiGraphics context,
                //? if <1.21.9
                int index, int y, int x, int entryWidth, int entryHeight,
                int mouseX, int mouseY, boolean hovered, float tickDelta) {
                //? if >=1.21.9 {
                /*int x = this.getContentX();
                int y = this.getContentY();
                *///?}

                context.drawString(ModernBetaSelectBiomeScreen.this.font, this.text, x + 5, y + 2, 0xFFFFFFFF);
            }

            @Override
            public boolean mouseClicked(/*? if <1.21.9 {*/ double mouseX, double mouseY, int button /*?} else {*/ /*net.minecraft.client.input.MouseButtonEvent click, boolean doubleClick *//*?}*/) {
                BiomeListWidget.this.setSelected(this);
                return super.mouseClicked(/*? if <1.21.9 {*/ mouseX, mouseY, button /*?} else {*/ /*click, doubleClick *//*?}*/);
            }
        }
    }
}
