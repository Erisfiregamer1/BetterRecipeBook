package net.marshmallow.BetterRecipeBook.Mixins;

import net.marshmallow.BetterRecipeBook.BetterRecipeBook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(RecipeBookWidget.class)
public abstract class RecipeBookWidgetMixin {
    @Shadow
    protected MinecraftClient client;
    @Final @Shadow
    private RecipeBookResults recipesArea;
    @Shadow
    protected abstract void refreshResults(boolean resetCurrentPage);

    @Inject(at = @At("HEAD"), method = "isOpen", cancellable = true)
    public void isOpen(CallbackInfoReturnable<Boolean> cir) {
        if (!BetterRecipeBook.config.enableBook) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("RETURN"), method = "refreshResults", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void refreshResultsInject(boolean resetCurrentPage, CallbackInfo ci, List<RecipeResultCollection> list, List<RecipeResultCollection> list2) {
        list2.removeIf((recipeResultCollection -> {
            boolean isIn = false;

            for (int j = 0; j < recipeResultCollection.getAllRecipes().size(); j++) {
                if (BetterRecipeBook.favourites.contains(recipeResultCollection.getAllRecipes().get(j).getId())) {
                    isIn = true;
                }
            }

            return isIn;
        }));
    }

    @Inject(at = @At("RETURN"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        RecipeBookResultsAccessor recipesArea = (RecipeBookResultsAccessor) this.recipesArea;

        for (int i = 0; i < recipesArea.getResultButtons().size(); i++) {
            AnimatedResultButton recipeBookResults = recipesArea.getResultButtons().get(i);

            if (recipeBookResults.isHovered()) {
                RecipeResultCollection recipeResultCollection = ((AnimatedResultButtonAccessor) recipeBookResults).getResults();
                for (int j = 0; j < recipeResultCollection.getAllRecipes().size(); j++) {
                    BetterRecipeBook.favourites.add(recipeResultCollection.getAllRecipes().get(j).getId());
                    refreshResults(true);
                }
            }
        }
    }
}
