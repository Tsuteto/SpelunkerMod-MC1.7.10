package tsuteto.spelunker.asm.entry;

import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import cpw.mods.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import tsuteto.spelunker.asm.AsmPetitUtil;
import tsuteto.spelunker.asm.ITransformerEntry;
import tsuteto.spelunker.asm.SpelunkerModCorePlugin;

import java.util.EnumSet;

public class TEntryDeathCause implements ITransformerEntry, Opcodes
{
    @Override
    public EnumSet<Side> getSide()
    {
        return EnumSet.of(Side.CLIENT, Side.SERVER);
    }

    @Override
    public String getTargetClass()
    {
        return "net.minecraft.util.ChatComponentTranslation";
    }

    @Override
    public String getTargetMethodDeobf()
    {
        return "<init>";
    }

    @Override
    public String getTargetMethodObf()
    {
        return "<init>";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "(Ljava/lang/String;[Ljava/lang/Object;)V";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        boolean obfed = SpelunkerModCorePlugin.isObfuscated;
        FieldNode fnode = cnode.fields.get(0);

        int i = 0;
        while (i < mnode.instructions.size())
        {
            AbstractInsnNode node = mnode.instructions.get(i);
            if (node instanceof LineNumberNode)
            {
                LineNumberNode linenumber = (LineNumberNode) node;
                System.out.printf("[%d] LINENUMBER %d%n", i, linenumber.line);
            }
            else
            {
                System.out.printf("[%d] %s%n", i, node.toString());
            }
            i++;
        }

        InsnList overrideList = new InsnList();

        String fldKey = obfed ? "field_150276_d" : "key";

        overrideList.add(new VarInsnNode(ALOAD, 0));
        overrideList.add(new VarInsnNode(ALOAD, 1));
        overrideList.add(new MethodInsnNode(INVOKESTATIC,
                "tsuteto/spelunker/eventhandler/DeathCauseHook",
                "onDeathCauseReplace",
                "(Ljava/lang/String;)Ljava/lang/String;"));
        overrideList.add(new FieldInsnNode(PUTFIELD,
                cnode.name,
                fldKey,
                "Ljava/lang/String;"));

        mnode.instructions.insert(mnode.instructions.get(27), overrideList); // insert before return statement

    }
}
