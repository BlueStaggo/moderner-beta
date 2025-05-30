package mod.bluestaggo.modernerbeta.client.color;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureContents;

@Environment(EnvType.CLIENT)
public class BlockColormap {
    private TextureContents colormap;
    
    public void setColormap(TextureContents map) {
        this.colormap = map;
    }
    
    public int getColor(double temp, double rain) {
        NativeImage image = this.colormap.image();

        final int width = image.getWidth();
        final int height = image.getHeight();

        int x = (int)((1.0 - temp) * (width - 1));
        int z = (int)((1.0 - (rain * temp)) * (height - 1));
        
        if (x < 0 || x >= width || z < 0 || z >= height) {
            return 0xFFFF00FF;
        }
        
        return image.getColorArgb(x, z);
    }
}
