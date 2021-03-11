package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class IndevCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    private IndevType levelType;
    private IndevTheme levelTheme;

    private int levelWidth;
    private int levelLength;
    private int levelHeight;
    
    private float caveRadius;
    
    public IndevCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag settings, Consumer<CompoundTag> consumer) {
        super(parent, settings, consumer);
        
        this.levelType = IndevType.fromName(ModernBeta.BETA_CONFIG.indevLevelType);
        this.levelTheme = IndevTheme.fromName(ModernBeta.BETA_CONFIG.indevLevelTheme);
        this.levelWidth = ModernBeta.BETA_CONFIG.indevLevelWidth;
        this.levelLength = ModernBeta.BETA_CONFIG.indevLevelLength;
        this.levelHeight = ModernBeta.BETA_CONFIG.indevLevelHeight;
        this.caveRadius = ModernBeta.BETA_CONFIG.indevCaveRadius;
        
        this.settings.putString("levelType", this.levelType.getName());
        this.settings.putString("levelTheme", this.levelTheme.getName());
        this.settings.putInt("levelWidth", this.levelWidth);
        this.settings.putInt("levelLength", this.levelLength);
        this.settings.putInt("levelHeight", this.levelHeight);
        this.settings.putFloat("caveRadius", this.caveRadius);
        
        this.consumer.accept(this.settings);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.indev.levelType", 
                IndevType.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.type." + value.getName()), 
                (gameOptions) -> { return this.levelType; }, 
                (gameOptions, option, value) -> {
                    this.levelType = value;
                    this.settings.putString("levelType", this.levelType.getName());
                    
                    this.consumer.accept(this.settings);
                })
        );
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.indev.levelTheme", 
                IndevTheme.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.theme." + value.getName()), 
                (gameOptions) -> { return this.levelTheme; }, 
                (gameOptions, option, value) -> {
                    this.levelTheme = value;
                    this.settings.putString("levelTheme", this.levelTheme.getName());
                    
                    this.consumer.accept(this.settings);
                })
        );
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.widthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> { return (double) this.levelWidth; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelWidth = value.intValue();
                    this.settings.putInt("levelWidth", this.levelWidth);
                    
                    this.consumer.accept(this.settings);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelWidth"), 
                            Text.of(String.valueOf(this.levelWidth)) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.lengthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> { return (double) this.levelLength; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelLength = value.intValue();
                    this.settings.putInt("levelLength", this.levelLength);
                    
                    this.consumer.accept(this.settings);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelLength"), 
                            Text.of(String.valueOf(this.levelLength)) 
                    });
                }
        ));
      
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.heightSlider", 
                64D, 320D, 64F,
                (gameOptions) -> { return (double) this.levelHeight; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelHeight = value.intValue();
                    this.settings.putInt("levelHeight", this.levelHeight);
                    
                    this.consumer.accept(this.settings);
                },
                (gameOptions, doubleOptions) -> {
                    int seaLevel = this.levelHeight / 2;
                    String heightString = String.valueOf(this.levelHeight) + " (Sea Level: " + String.valueOf(seaLevel) + ")";
                    
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelHeight"), 
                            Text.of(heightString) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.caveRadiusSlider", 
                1D, 3D, 0.1f,
                (gameOptions) -> { return (double) this.caveRadius; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.caveRadius = value.floatValue();
                    this.settings.putFloat("caveRadius", this.caveRadius);
                    
                    this.consumer.accept(this.settings);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value",  
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.caveRadius"), 
                            Text.of(String.format("%.01f", this.caveRadius)) 
                    });
                }
        ));
    }
}
