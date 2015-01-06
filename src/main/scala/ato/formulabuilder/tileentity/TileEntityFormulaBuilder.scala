package ato.formulabuilder.tileentity

import ato.formulabuilder.FormulaBuilder
import net.minecraft.tileentity.TileEntity

class TileEntityFormulaBuilder extends TileEntity {

  var activated = true
  var progress = 0
  var range = ((-5, -5, -5), (5, 5, 5))

  override def updateEntity(): Unit = if (activated) {
    val xlist = range._1._1 to range._2._1
    val ylist = range._1._2 to range._2._2
    val zlist = range._1._3 to range._2._3
    if (progress < xlist.length * ylist.length * zlist.length) {
      val (dx, dy, dz) = (for {
        dx <- xlist
        dy <- ylist
        dz <- zlist
      } yield (dx, dy, dz)).apply(progress)
      val (x, y, z) = (xCoord + dx, yCoord + dy, zCoord + dz)
      if (worldObj.isAirBlock(x, y, z) && evaluate(dx, dy, dz)) {
        worldObj.setBlock(x, y, z, FormulaBuilder.blockFrame)
      }
      progress += 1
    } else {
      activated = false
    }
  }

  def evaluate(dx: Int, dy: Int, dz: Int): Boolean = true
}
