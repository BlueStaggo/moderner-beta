package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.legacy;

public class LayerZoom extends LayerZoomBase {
	public LayerZoom(long seed, Layer parent) {
		super(seed, parent);
	}

	@Override
	protected BiomeInfo interpolate(BiomeInfo a, BiomeInfo b) {
		return nextInt(2) == 0 ? a : b;
	}

	@Override
	protected BiomeInfo interpolate(BiomeInfo a, BiomeInfo b, BiomeInfo c, BiomeInfo d) {
		boolean ab = a.equals(b);
		boolean ac = a.equals(c);
		boolean ad = a.equals(d);
		boolean bc = b.equals(c);
		boolean bd = b.equals(d);
		boolean cd = c.equals(d);
		if(bc && cd) {
			return b;
		} else if(ab && ac) {
			return a;
		} else if(ab && ad) {
			return a;
		} else if(ac && ad) {
			return a;
		} else if(ab && !cd) {
			return a;
		} else if(ac && !bd) {
			return a;
		} else if(ad && !bc) {
			return a;
		} else if(bc && !ad) {
			return b;
		} else if(bd && !ac) {
			return b;
		} else if(cd && !ab) {
			return c;
		} else {
			int choice = this.nextInt(4);
			return switch (choice) {
				case 0 -> a;
				case 1 -> b;
				case 2 -> c;
				case 3 -> d;
				default -> throw new IllegalStateException("Unexpected value: " + choice);
			};
		}
	}

	public static Layer multi(long seed, Layer parent, int count) {
		for (int i = 0; i < count; i++) {
			parent = new LayerZoom(seed, parent);
			seed++;
		}

		return parent;
	}
}
