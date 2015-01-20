package tsuteto.spelunker.asm.entry;

import cpw.mods.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import tsuteto.spelunker.asm.ITransformerEntry;

import java.util.EnumSet;

public class TEntryToggleMoreOptions implements ITransformerEntry, Opcodes
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
        return "func_146316_a";
    }

    @Override
    public String getTargetMethodObf()
    {
        return "func_146316_a";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "(Z)V";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        InsnList overrideList = new InsnList();

        overrideList.add(new VarInsnNode(ALOAD, 0));
        overrideList.add(new VarInsnNode(ILOAD, 1));

        overrideList.add(new MethodInsnNode(INVOKESTATIC,
                "tsuteto/spelunker/eventhandler/GuiCreateWorldHook",
                "onToggleMoreOptions",
                "(L" + cnode.name + ";Z)V",
                false));

        mnode.instructions.insert(mnode.instructions.get(6), overrideList);
    }

}
