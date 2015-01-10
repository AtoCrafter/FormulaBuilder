package ato.formulabuilder

import ato.formulabuilder.block.BlockFormulaBuilder
import ato.formulabuilder.gui.{GuiHandler, Message, MessageHandler}
import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack

@Mod(modid = "FormulaBuilder", modLanguage = "scala")
object FormulaBuilder {

  val packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("FormulaBuilder")

  val blockBuilder = new BlockFormulaBuilder()

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    GameRegistry.registerBlock(blockBuilder, "FormulaBuilder")
    GameRegistry.registerTileEntity(classOf[TileEntityFormulaBuilder], "FormulaBuilder")
    GameRegistry.addRecipe(new ItemStack(blockBuilder),
      " B ",
      "DAD",
      "OOO",
      new Character('B'), Items.book,
      new Character('D'), Items.diamond,
      new Character('A'), Blocks.beacon,
      new Character('O'), Blocks.obsidian)

    packetHandler.registerMessage(
      classOf[MessageHandler], classOf[Message], 0, Side.SERVER)
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler())
  }
}
