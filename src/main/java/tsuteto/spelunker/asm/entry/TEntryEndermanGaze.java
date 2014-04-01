package tsuteto.spelunker.asm.entry;

import java.util.EnumSet;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import tsuteto.spelunker.asm.AsmPetitUtil;
import tsuteto.spelunker.asm.ITransformerEntry;
import cpw.mods.fml.relauncher.Side;

public class TEntryEndermanGaze implements ITransformerEntry, Opcodes
{
    @Override
    public EnumSet<Side> getSide()
    {
        return EnumSet.of(Side.CLIENT, Side.SERVER);
    }

    @Override
    public String getTargetClass()
    {
        return "net.minecraft.entity.monster.EntityEnderman";
    }

    @Override
    public String getTargetMethodDeobf()
    {
        return "findPlayerToAttack";
    }

    @Override
    public String getTargetMethodObf()
    {
        return "func_70782_k";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "()Lnet/minecraft/entity/Entity;";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        String entityPlayer = AsmPetitUtil.getActualClass("net/minecraft/entity/player/EntityPlayer");

        InsnList overrideList = new InsnList();

        overrideList.add(new VarInsnNode(ALOAD, 0));
        overrideList.add(new VarInsnNode(ALOAD, 1));
        overrideList.add(new MethodInsnNode(INVOKESTATIC,
                "tsuteto/spelunker/eventhandler/EndermanGazeHook",
                "onEndermanGazing",
                "(L" + cnode.name + ";L" + entityPlayer + ";)V"));

        mnode.instructions.insert(mnode.instructions.get(61), overrideList);
    }

}
