package net.marshmallow.BetterRecipeBook.Mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipebook.AnimatedResultButton;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AnimatedResultButton.class)
public class AnimatedResultButtonMixin {
    @Inject(at = @At("RETURN"), method = "getTooltip", locals = LocalCapture.CAPTURE_FAILSOFT)
    public void getTooltip(Screen screen, CallbackInfoReturnable<List<Text>> cir, ItemStack itemStack, List<Text> list) {
        list.add(new TranslatableText("add to your favourites"));
    }
}
