package ato.formulabuilder.gui

import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container

class ContainerFormulaBuilder(tileentity: TileEntityFormulaBuilder) extends Container {
  override def canInteractWith(player: EntityPlayer): Boolean = true
}
