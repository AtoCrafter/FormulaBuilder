package ato.formulabuilder.block

import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockFormulaBuilder extends BlockContainer(Material.iron) {

  setBlockName("FormulaBuilder")
  setCreativeTab(CreativeTabs.tabRedstone)

  override def createNewTileEntity(world: World, meta: Int): TileEntity =
    new TileEntityFormulaBuilder()
}
