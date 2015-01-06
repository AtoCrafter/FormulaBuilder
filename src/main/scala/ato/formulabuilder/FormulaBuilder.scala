package ato.formulabuilder

import ato.formulabuilder.block.{BlockFormulaBuilder, BlockFrame}
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.registry.GameRegistry

@Mod(modid = "FormulaBuilder", modLanguage = "scala")
object FormulaBuilder {

  val blockBuilder = new BlockFormulaBuilder()
  val blockFrame = new BlockFrame()

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    GameRegistry.registerBlock(blockBuilder, "FormulaBuilder")
    GameRegistry.registerBlock(blockFrame, "Frame")
  }
}
