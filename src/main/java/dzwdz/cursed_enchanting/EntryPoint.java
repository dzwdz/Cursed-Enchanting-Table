package dzwdz.cursed_enchanting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class EntryPoint implements ModInitializer {
    public static final Tag<Block> CURSED_GROUND = TagRegistry.block(new Identifier("cursed_enchanting", "cursed_ground"));

    @Override
    public void onInitialize() {
    }
}
