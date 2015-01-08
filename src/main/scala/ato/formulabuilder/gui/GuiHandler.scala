package ato.formulabuilder.gui

import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class GuiHandler extends IGuiHandler {
  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
    world.getTileEntity(x, y, z) match {
      case tile: TileEntityFormulaBuilder => new GuiContainerFormulaBuilder(new ContainerFormulaBuilder(tile))
      case _ => null
    }

  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
    world.getTileEntity(x, y, z) match {
      case tile: TileEntityFormulaBuilder => new ContainerFormulaBuilder(tile)
      case _ => null
    }
}
