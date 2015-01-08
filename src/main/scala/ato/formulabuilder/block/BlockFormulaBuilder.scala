package ato.formulabuilder.block

import ato.formulabuilder.FormulaBuilder
import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockFormulaBuilder extends BlockContainer(Material.iron) {

  setBlockName("FormulaBuilder")
  setCreativeTab(CreativeTabs.tabRedstone)

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, i1: Int, f1: Float, f2: Float, f3: Float): Boolean = {
    player.openGui(FormulaBuilder, 0, world, x, y, z)
    true
  }

  override def createNewTileEntity(world: World, meta: Int): TileEntity =
    new TileEntityFormulaBuilder()
}
