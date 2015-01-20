package tsuteto.spelunker.asm.entry;

import cpw.mods.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import tsuteto.spelunker.asm.ITransformerEntry;

import java.util.EnumSet;

public class TEntryItemPotion implements ITransformerEntry, Opcodes
{
    @Override
    public EnumSet<Side> getSide()
    {
        return EnumSet.of(Side.CLIENT, Side.SERVER);
    }

    @Override
    public String getTargetClass()
    {
        return "net.minecraft.item.ItemPotion";
    }

    @Override
    public String getTargetMethodDeobf()
    {
        return "onEaten";
    }

    @Override
    public String getTargetMethodObf()
    {
        return "func_77654_b";
    }

    @Override
    public String getTargetMethodDesc()
    {
        return "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;";
    }

    @Override
    public void transform(MethodNode mnode, ClassNode cnode)
    {
        FieldNode fnode = cnode.fields.get(0);

        InsnList overrideList = new InsnList();

        overrideList.add(new VarInsnNode(ALOAD, 1));
        overrideList.add(new VarInsnNode(ALOAD, 2));
        overrideList.add(new VarInsnNode(ALOAD, 3));
        overrideList.add(new MethodInsnNode(INVOKESTATIC,
                "tsuteto/spelunker/eventhandler/PotionEventHook",
                "onEaten",
                "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)V",
                false));

        mnode.instructions.insert(mnode.instructions.get(1), overrideList);
    }
}
