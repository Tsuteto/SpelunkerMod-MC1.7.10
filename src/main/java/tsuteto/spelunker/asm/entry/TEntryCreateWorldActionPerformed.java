package tsuteto.spelunker.asm.entry;

import cpw.mods.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import tsuteto.spelunker.asm.AsmPetitUtil;
import tsuteto.spelunker.asm.ITransformerEntry;
import tsuteto.spelunker.asm.SpelunkerModCorePlugin;

import java.util.EnumSet;

public class TEntryCreateWorldActionPerformed implements ITransformerEntry, Opcodes
{
    @Override
    public EnumSet<Side> getSide()
    {
        return EnumSet.of(Side.CLIENT);
    }

    @Override
    public String getTargetClass()
    {
        return "net.minecraft.client.gui.GuiCreateWorld";
    }

    @Override
    public String getTargetMethodDeobf()
    {
        return "actionPerformed";
    }

    @Override
    public String getTargetMethodObf()
    {
        return "func_146284_a";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "(Lnet/minecraft/client/gui/GuiButton;)V";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        boolean obfed = SpelunkerModCorePlugin.isObfuscated;

        // Adding game mode
        {
            String mtdUpdateButtonText = obfed ? "func_146319_h" : "func_146319_h"; // updateButtonText

            InsnList overrideList = new InsnList();

            overrideList.add(new VarInsnNode(ALOAD, 0));

            overrideList.add(new MethodInsnNode(INVOKESTATIC,
                    "tsuteto/spelunker/eventhandler/GuiCreateWorldHook",
                    "onGameModeButtonClicked",
                    "(L" + cnode.name + ";)V",
                    false));

            overrideList.add(new VarInsnNode(ALOAD, 0));

            overrideList.add(new MethodInsnNode(INVOKESPECIAL,
                    cnode.name,
                    mtdUpdateButtonText,
                    "()V",
                    false));

            overrideList.add(new InsnNode(RETURN));

            int insertPos = obfed ? 195 : 194;

            mnode.instructions.insert(mnode.instructions.get(insertPos), overrideList);
        }

        // Adding HC button action
        {
            String clsGuiButton = AsmPetitUtil.getActualClass("net/minecraft/client/gui/GuiButton");

            InsnList overrideList = new InsnList();

            overrideList.add(new VarInsnNode(ALOAD, 0));
            overrideList.add(new VarInsnNode(ALOAD, 1));

            overrideList.add(new MethodInsnNode(INVOKESTATIC,
                    "tsuteto/spelunker/eventhandler/GuiCreateWorldHook",
                    "onButtonClicked",
                    "(L" + cnode.name + ";L" + clsGuiButton + ";)V",
                    false));

            mnode.instructions.insert(mnode.instructions.get(7), overrideList);
        }
    }
}
