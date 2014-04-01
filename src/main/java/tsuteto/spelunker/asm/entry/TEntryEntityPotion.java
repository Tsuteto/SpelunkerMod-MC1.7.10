package tsuteto.spelunker.asm.entry;

import java.util.EnumSet;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import tsuteto.spelunker.asm.AsmPetitUtil;
import tsuteto.spelunker.asm.ITransformerEntry;
import cpw.mods.fml.relauncher.Side;

public class TEntryEntityPotion implements ITransformerEntry, Opcodes
{
    @Override
    public EnumSet<Side> getSide()
    {
        return EnumSet.of(Side.CLIENT, Side.SERVER);
    }

    @Override
    public String getTargetClass()
    {
        return "net.minecraft.entity.projectile.EntityPotion";
    }

    @Override
    public String getTargetMethodDeobf()
    {
        return "onImpact";
    }

    @Override
    public String getTargetMethodObf()
    {
        return "func_70184_a";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "(Lnet/minecraft/util/MovingObjectPosition;)V";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        String entityLivingBase = AsmPetitUtil.getActualClass("net/minecraft/entity/EntityLivingBase");

        FieldNode fnode = (FieldNode)cnode.fields.get(0);

        InsnList overrideList = new InsnList();

        overrideList.add(new VarInsnNode(ALOAD, 0));
        overrideList.add(new VarInsnNode(ALOAD, 2)); // potionEffects
        overrideList.add(new VarInsnNode(ALOAD, 6)); // entity
        overrideList.add(new VarInsnNode(DLOAD, 9)); // factor
        overrideList.add(new MethodInsnNode(INVOKESTATIC,
                "tsuteto/spelunker/eventhandler/PotionEventHook",
                "onSplashPotionImpact",
                "(L" + cnode.name + ";Ljava/util/List;L" + entityLivingBase + ";D)V"));

        mnode.instructions.insert(mnode.instructions.get(91), overrideList);
    }
}
