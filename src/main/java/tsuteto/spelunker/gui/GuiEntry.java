package tsuteto.spelunker.gui;

abstract public class GuiEntry
{
    public final int id;

    protected Class<?> guiClass;
    protected Class<?> tileEntityClass;
    protected Class<?> containerClass;
    private boolean hasCoord;

    public GuiEntry(int id)
    {
        this.id = id;
    }

    public GuiEntry withName(String name)
    {
        this.withName(name, name, name);
        return this;
    }

    public GuiEntry withName(String guiClass, String tileEntityClass, String containerClass)
    {
        try
        {
            if (tileEntityClass != null)
            {
                this.tileEntityClass = Class.forName("tsuteto.spelunker.block.tileentity.TileEntity" + tileEntityClass);
            }
            this.containerClass = Class.forName("tsuteto.spelunker.block.tileentity.Container" + containerClass);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    public GuiEntry setCoord()
    {
        this.hasCoord = true;
        return this;
    }

    public boolean hasCoord()
    {
        return this.hasCoord;
    }

    public Class<?> getGuiClass()
    {
        return this.guiClass;
    }

    public Class<?> getTileEntityClass()
    {
        return this.tileEntityClass;
    }

    public Class<?> getContainerClass()
    {
        return this.containerClass;
    }
}
