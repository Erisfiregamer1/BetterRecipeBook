package net.marshmallow.BetterRecipeBook;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.marshmallow.BetterRecipeBook.Mixins.Accessors.BrewingRecipeRegistryRecipeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PinnedRecipeManager {
    public List<Identifier> pinned;

    public void read() {
        Gson gson = new Gson();
        JsonReader reader = null;

        try {
            File pinsFile = new File(MinecraftClient.getInstance().runDirectory, "brb.pins");

            if (pinsFile.exists()) {
                reader = new JsonReader(new FileReader(pinsFile.getAbsolutePath()));
                Type type = new TypeToken<List<Identifier>>() {}.getType();
                pinned = gson.fromJson(reader, type);
            }
        } catch (Throwable var8) {
            BetterRecipeBook.LOGGER.error("brb.pins could not be read.");
        } finally {
            if (pinned == null) {
                pinned = new ArrayList<>();
            }
            IOUtils.closeQuietly(reader);
        }
    }

    private void store() {
        Gson gson = new Gson();
        OutputStreamWriter writer = null;

        try {
            File pinsFile = new File(MinecraftClient.getInstance().runDirectory, "brb.pins");
            writer = new OutputStreamWriter(new FileOutputStream(pinsFile), StandardCharsets.UTF_8);
            writer.write(gson.toJson(this.pinned));
        } catch (Throwable var8) {
            BetterRecipeBook.LOGGER.error("brb.pins could not be saved.");
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public void addOrRemoveFavourite(RecipeResultCollection target) {
        for (Identifier identifier : this.pinned) {
            for (Recipe<?> recipe : target.getAllRecipes()) {
                if (recipe.getId().equals(identifier)) {
                    this.pinned.remove(identifier);
                    this.store();
                    return;
                }
            }
        }

        this.pinned.add(target.getAllRecipes().get(0).getId());
        this.store();
    }

    public void addOrRemoveFavouritePotion(BrewingRecipeRegistry.Recipe<?> target) {
        Identifier targetIdentifier = Registry.POTION.getId((Potion) ((BrewingRecipeRegistryRecipeAccessor<?>) target).getOutput());

        for (Identifier identifier : this.pinned) {
            if (identifier.equals(targetIdentifier)) {
                this.pinned.remove(targetIdentifier);
                this.store();
                return;
            }
        }

        this.pinned.add(targetIdentifier);
        this.store();
    }

    public boolean has(Object target) {
        for (Identifier identifier : this.pinned) {
            for (Recipe<?> recipe : ((RecipeResultCollection) target).getAllRecipes()) {
                if (recipe.getId().equals(identifier)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPotion(BrewingRecipeRegistry.Recipe<?> target) {
        Identifier targetIdentifier = Registry.POTION.getId((Potion) ((BrewingRecipeRegistryRecipeAccessor<?>) target).getOutput());

        for (Identifier identifier : this.pinned) {
            if (targetIdentifier.equals(identifier)) {
                return true;
            }
        }
        return false;
    }
}
