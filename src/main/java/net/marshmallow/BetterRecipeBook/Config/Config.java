package net.marshmallow.BetterRecipeBook.Config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@me.shedaniel.autoconfig.annotation.Config(name = "betterrecipebook")
public class Config implements ConfigData {
    public boolean enableBook = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean darkMode = false;

    public boolean unlockAll = true;

    public boolean keepCentered = true;
    public boolean statusEffects = true;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public InstantCraft instantCraftModule = new InstantCraft();

    @ConfigEntry.Gui.Tooltip()
    public boolean enableBounce = false;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Scrolling scrollingModule = new Scrolling();

    @ConfigEntry.Gui.Tooltip()
    public boolean showAlternativesOnHover = true;
}
