package ato.formulabuilder

import ato.formulabuilder.block.BlockFormulaBuilder
import ato.formulabuilder.gui.{GuiHandler, Message, MessageHandler}
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side

@Mod(modid = "FormulaBuilder", modLanguage = "scala")
object FormulaBuilder {

  val packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("FormulaBuilder")

  val blockBuilder = new BlockFormulaBuilder()

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    GameRegistry.registerBlock(blockBuilder, "FormulaBuilder")

    packetHandler.registerMessage(
      classOf[MessageHandler], classOf[Message], 0, Side.SERVER)
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler())
  }
}
