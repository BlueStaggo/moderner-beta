//~registryOr
//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical;

import com.ibm.icu.text.Collator;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.GridLayout;
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

public class ModernBetaSelectBiomeScreen extends ModernBetaScreen {
    private static final Component SEARCH_HINT = Component.translatable("createWorld.customize.modern_beta.search").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    private final Consumer<Holder<Biome>> onDone;
    private final boolean allowNone;
    final Registry<Biome> biomeRegistry;
    private BiomeList biomeSelectionList;
    Holder<Biome> biome;
    private Button confirmButton;

    public ModernBetaSelectBiomeScreen(Screen parent, WorldCreationContext context, Consumer<Holder<Biome>> onDone, boolean allowNone) {
        super(Component.translatable("createWorld.customize.modern_beta.title.biome_picker"), parent, 13 + 9 + 3 + 15, 33);
        this.onDone = onDone;
        this.allowNone = allowNone;
        this.biomeRegistry = context.worldgenLoadContext().lookupOrThrow(Registries.BIOME);
        Holder<Biome> registryEntry = this.biomeRegistry
            //? if >=1.21.2 {
            .get
            //? } else {
            /*.getHolder
            *///? }
                (Biomes.PLAINS)
            .or(() -> this.biomeRegistry.listElements().findAny())
            .orElseThrow();
        this.biome = context.selectedDimensions()
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
        this.biomeSelectionList = new BiomeList();

        super.init();
        this.biomeSelectionList.setSelected(this.biomeSelectionList
                .children()
                .stream()
                .filter(entry -> Objects.equals(entry.biome, this.biome))
                .findFirst()
                .orElse(null));
    }

    @Override
    protected void initHeader(GridLayout headerLayout) {
        headerLayout.columnSpacing(3);
        headerLayout.defaultCellSetting().paddingVertical(3);

        super.initHeader(headerLayout);

        EditBox editBox = headerLayout.addChild(new EditBox(this.font, 0, 0, 200, 15, Component.empty()), 1, 0);
        editBox.setHint(SEARCH_HINT);
        editBox.setResponder(biomeSelectionList::filterEntries);
    }

    @Override
    protected void initContent(GridLayout contentLayout) {
        //? if >=1.20.2 {
        this.layout.addToContents(this.biomeSelectionList);
        //? } else {
        /*this.addRenderableWidget(this.biomeSelectionList);
        *///? }
    }

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout.RowHelper row = footerLayout.createRowHelper(2);

        this.confirmButton = row.addChild(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.onDone.accept(this.biome);
            this.onClose();
        }).build());
        row.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        //? if >=1.20.3 {
        this.biomeSelectionList.setSize(this.width, this.layout.getContentHeight());
        this.biomeSelectionList.setPosition(0, this.layout.getHeaderHeight());
        //? } else {
        /*int y0 = this.layout.getY() + this.layout.getHeaderHeight();
        int y1 = y0 + this.layout.getContentHeight();

        this.biomeSelectionList.updateSize(this.width, this.height, y0, y1);
        *///? }
        //? if >=1.21.4 {
        this.biomeSelectionList.refreshScrollAmount();
        //? } else {
        /*this.biomeSelectionList.setScrollAmount(this.biomeSelectionList.getScrollAmount());
        *///? }
    }

    void refreshConfirmButton() {
        this.confirmButton.active = this.biomeSelectionList.getSelected() != null;
    }

    class BiomeList extends ObjectSelectionList<BiomeList.Entry> {
        BiomeList() {
            //? if >=1.20.2 {
            super(
                ModernBetaSelectBiomeScreen.this.minecraft,
                ModernBetaSelectBiomeScreen.this.width,
                ModernBetaSelectBiomeScreen.this.layout.getContentHeight(),
                ModernBetaSelectBiomeScreen.this.layout.getHeaderHeight(),
                15
            );
            //?} else {
            /*super(
                ModernBetaSelectBiomeScreen.this.minecraft,
                ModernBetaSelectBiomeScreen.this.width,
                ModernBetaSelectBiomeScreen.this.height,
                ModernBetaSelectBiomeScreen.this.layout.getHeaderHeight(),
                ModernBetaSelectBiomeScreen.this.height - ModernBetaSelectBiomeScreen.this.layout.getHeaderHeight(),
                15
            );
            *///?}
            this.filterEntries("");
        }

        private void filterEntries(String filter) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            String query = filter.toLowerCase(Locale.ROOT);
            this.clearEntries();
            if (ModernBetaSelectBiomeScreen.this.allowNone)
                this.addEntry(new Entry());

            ModernBetaSelectBiomeScreen.this.biomeRegistry
                .listElements()
                .map(Entry::new)
                .sorted(Comparator.comparing(entry -> entry.text.getString(), collator))
                .filter(entry -> filter.isEmpty() || entry.text.getString().toLowerCase(Locale.ROOT).contains(query))
                .forEach(this::addEntry);
            //? if >=1.21.4 {
            this.refreshScrollAmount();
            //? } else {
            /*this.setScrollAmount(this.getScrollAmount());
            *///? }
        }

        public void setSelected(@Nullable Entry entry) {
            super.setSelected(entry);
            if (entry != null) {
                ModernBetaSelectBiomeScreen.this.biome = entry.biome;
            }

            ModernBetaSelectBiomeScreen.this.refreshConfirmButton();
        }

        class Entry extends ObjectSelectionList.Entry<Entry> {
            final Holder.Reference<Biome> biome;
            final Component text;

            public Entry() {
                this.biome = null;
                this.text = Component.translatable("gui.none").withStyle(ChatFormatting.ITALIC);
            }

            public Entry(final Holder.Reference<Biome> biome) {
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
            //? if >=26.1 {
            /*extractContent
            *///? } else if >=1.21.9 {
            /*renderContent
            *///? } else {
            render
            //? }
            (GuiGraphics graphics,
                //? if <1.21.9
                int index, int y, int x, int entryWidth, int entryHeight,
                int mouseX, int mouseY, boolean hovered, float tickDelta) {
                //? if >=1.21.9 {
                /*int x = this.getContentX();
                int y = this.getContentY();
                *///?}

                graphics.drawString(ModernBetaSelectBiomeScreen.this.font, this.text, x + 5, y + 2, 0xFFFFFFFF);
            }

            @Override
            public boolean mouseClicked(/*? if <1.21.9 {*/ double mouseX, double mouseY, int button /*?} else {*/ /*net.minecraft.client.input.MouseButtonEvent click, boolean doubleClick *//*?}*/) {
                BiomeList.this.setSelected(this);
                return super.mouseClicked(/*? if <1.21.9 {*/ mouseX, mouseY, button /*?} else {*/ /*click, doubleClick *//*?}*/);
            }
        }
    }
}
