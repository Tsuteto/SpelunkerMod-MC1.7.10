package tsuteto.spelunker.asm.entry;

import java.util.EnumSet;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import tsuteto.spelunker.asm.ITransformerEntry;
import cpw.mods.fml.relauncher.Side;

public class TEntryAddingHcModeButton implements ITransformerEntry, Opcodes
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
        return "initGui";
    }

    // Can't FMLDeobfuscatingRemapper be used for conversion between obf one and deobf one?!?!

    @Override
    public String getTargetMethodObf()
    {
        return "func_73866_w_";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "()V";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        InsnList overrideList = new InsnList();

        overrideList.add(new VarInsnNode(ALOAD, 0));

        overrideList.add(new MethodInsnNode(INVOKESTATIC,
                "tsuteto/spelunker/eventhandler/GuiCreateWorldHook",
                "onInitGui",
                "(L" + cnode.name + ";)V"));

        mnode.instructions.insert(mnode.instructions.get(337), overrideList);
    }
}
