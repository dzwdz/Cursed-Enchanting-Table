package dzwdz.cursed_enchanting_table.mixin;

import dzwdz.cursed_enchanting_table.EntryPoint;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(EnchantmentScreenHandler.class)
public class ScreenHandlerMixin {
    @Shadow
    private ScreenHandlerContext context;
    @Shadow
    private Random random;
    @Shadow
    private Property seed;

    private boolean curseFilter(EnchantmentLevelEntry e) {
        return e.enchantment.isCursed();
    }

    @Inject(method = "Lnet/minecraft/screen/EnchantmentScreenHandler;generateEnchantments(Lnet/minecraft/item/ItemStack;II)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    public void generateEnchantments(ItemStack stack, int slot, int level, CallbackInfoReturnable callbackInfo) {
        this.context.run((world, blockPos) -> {
            if (world.getBlockState(blockPos.down(1)).getBlock().isIn(EntryPoint.CURSED_GROUND)) {

                this.random.setSeed((long)(this.seed.get() + slot));
                List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(this.random, stack, level + 5, true);
                if (stack.getItem() == Items.BOOK && list.size() > 1) {
                    list.remove(this.random.nextInt(list.size()));
                }

                boolean hasCurses = list.stream().filter(this::curseFilter).count() > 0;
                if (!hasCurses) {
                    List<EnchantmentLevelEntry> curseStream = EnchantmentHelper.getPossibleEntries(level + 5, stack, true).stream().filter(this::curseFilter).collect(Collectors.toList());
                    if (curseStream.size() > 0) {
                        list.add(curseStream.get(this.random.nextInt(curseStream.size())));
                    }
                }

                callbackInfo.setReturnValue(list);

            }
        });
    }
}
