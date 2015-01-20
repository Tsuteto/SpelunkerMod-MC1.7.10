package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ChestGenHooks;
import org.apache.logging.log4j.Level;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.gui.GuiHardcoreButton;
import tsuteto.spelunker.util.FieldAccessor;
import tsuteto.spelunker.util.ModLog;

import java.util.List;
import java.util.Map;

public class GuiCreateWorldHook
{
    private static FieldAccessor<List> buttonList;
    private static FieldAccessor<String> gameMode;
    private static FieldAccessor<Boolean> commandsToggled;
    private static FieldAccessor<Boolean> isHardcore;
    private static FieldAccessor<Boolean> commandsAllowed;
    private static FieldAccessor<GuiButton> buttonAllowCommands;
    private static FieldAccessor<GuiButton> buttonBonusItems;

    private static FieldAccessor<Map<String, ChestGenHooks>> chestGenHooksChestInfo;
    private static ChestGenHooks originalBonusChest;

    static
    {
        buttonList = new FieldAccessor<List>(ReflectionHelper.findField(GuiScreen.class, "field_146292_n", "buttonList"));
        gameMode = new FieldAccessor<String>(ReflectionHelper.findField(GuiCreateWorld.class, "field_146342_r", "gameMode"));
        commandsAllowed = new FieldAccessor<Boolean>(ReflectionHelper.findField(GuiCreateWorld.class, "field_146340_t", "commandsAllowed"));
        commandsToggled = new FieldAccessor<Boolean>(ReflectionHelper.findField(GuiCreateWorld.class, "field_146339_u", "commandsToggled"));
        isHardcore = new FieldAccessor<Boolean>(ReflectionHelper.findField(GuiCreateWorld.class, "field_146337_w", "isHardcore"));
        buttonBonusItems = new FieldAccessor<GuiButton>(ReflectionHelper.findField(GuiCreateWorld.class, "field_146326_C", "buttonBonusItems"));
        buttonAllowCommands = new FieldAccessor<GuiButton>(ReflectionHelper.findField(GuiCreateWorld.class, "field_146321_E", "buttonAllowCommands"));

        chestGenHooksChestInfo = new FieldAccessor<Map<String, ChestGenHooks>>(ReflectionHelper.findField(ChestGenHooks.class, "chestInfo"));
    }

    public static void onGameModeButtonClicked(GuiCreateWorld gui)
    {
        try
        {
            if (gameMode.get(gui).equals("survival"))
            {
                if (!commandsToggled.get(gui))
                {
                    commandsAllowed.set(gui, false);
                }

                gameMode.set(gui, "scoreTrial");
                isHardcore.set(gui, false);
                buttonAllowCommands.get(gui).enabled = false;
                buttonBonusItems.get(gui).enabled = true;
                SpelunkerMod.settings().gameMode = SpelunkerGameMode.Arcade;
            }
            else if (gameMode.get(gui).equals("scoreTrial"))
            {
                if (!commandsToggled.get(gui))
                {
                    commandsAllowed.set(gui, false);
                }

                gameMode.set(gui, "hardcore");
                isHardcore.set(gui, true);
                buttonAllowCommands.get(gui).enabled = false;
                buttonBonusItems.get(gui).enabled = false;
                SpelunkerMod.settings().gameMode = SpelunkerGameMode.Adventure;
            }
            else if (gameMode.get(gui).equals("hardcore"))
            {
                if (!commandsToggled.get(gui))
                {
                    commandsAllowed.set(gui, true);
                }

                gameMode.set(gui, "creative");
                isHardcore.set(gui, false);
                buttonAllowCommands.get(gui).enabled = true;
                buttonBonusItems.get(gui).enabled = true;
                SpelunkerMod.settings().gameMode = SpelunkerGameMode.Adventure;
            }
            else
            {
                if (!commandsToggled.get(gui))
                {
                    commandsAllowed.set(gui, false);
                }

                gameMode.set(gui, "survival");
                isHardcore.set(gui, false);
                buttonAllowCommands.get(gui).enabled = true;
                buttonBonusItems.get(gui).enabled = true;
                SpelunkerMod.settings().gameMode = SpelunkerGameMode.Adventure;
            }
        }
        catch (Exception e)
        {
            ModLog.log(Level.FATAL, e, "Failed to change the gamemode.");
        }
    }

    public static void onInitGui(GuiCreateWorld gui)
    {
        try
        {
            buttonList.get(gui).add(new GuiHardcoreButton(9, gui.width / 2 + 100, 115, 15, 30));
        }
        catch (Exception e)
        {
            ModLog.log(Level.FATAL, e, "Failed to add gamemode button!");
        }

        SpelunkerMod.settings().hardcore = false;

        try
        {
            if (originalBonusChest == null)
            {
                ChestGenHooks oldBonuschest = chestGenHooksChestInfo.get(null).get(ChestGenHooks.BONUS_CHEST);
                originalBonusChest = oldBonuschest;
            }
            else
            {
                chestGenHooksChestInfo.get(null).put(ChestGenHooks.BONUS_CHEST, originalBonusChest);
            }
        }
        catch (Exception e)
        {
            ModLog.log(Level.FATAL, e, "Failed to get bonuschest contents!");
        }
    }

    public static void onButtonClicked(GuiCreateWorld gui, GuiButton button)
    {
        if (button.id == 9)
        {
            GuiHardcoreButton iconbtn = (GuiHardcoreButton)button;
            iconbtn.toggle();
            SpelunkerMod.settings().hardcore = iconbtn.isTurnedOn();
            if (iconbtn.isTurnedOn())
            {
                // Replace bonus chest
                try
                {
                    chestGenHooksChestInfo.get(null).put(ChestGenHooks.BONUS_CHEST, SpelunkerMod.hardcoreBonusChest);
                }
                catch (Exception e)
                {
                    ModLog.log(Level.FATAL, e, "Failed to replace bonuschest contents!");
                }
            }
            else
            {
                // Restore bonus chest
                try
                {
                    chestGenHooksChestInfo.get(null).put(ChestGenHooks.BONUS_CHEST, originalBonusChest);
                }
                catch (Exception e)
                {
                    ModLog.log(Level.FATAL, e, "Failed to restore bonuschest contents!");
                }
            }
        }
    }

    public static void onToggleMoreOptions(GuiCreateWorld gui, boolean par1)
    {
        try
        {
            for (Object obj : buttonList.get(gui))
            {
                GuiButton btn = (GuiButton)obj;
                if (btn.id == 9)
                {
                    btn.visible = !par1;
                }
            }
        } catch (IllegalAccessException e)
        {
            ModLog.log(Level.FATAL, e, "Failed to switch button visibility!");
        }
    }
}
