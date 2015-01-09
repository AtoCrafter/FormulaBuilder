package ato.formulabuilder.tileentity

import ato.formulabuilder.util.ParserFormula
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity

class TileEntityFormulaBuilder extends TileEntity {

  val parser = new ParserFormula()

  var activated = false
  var progress = 0
  var xlist = -50 to 50
  var ylist = -50 to 50
  var zlist = -50 to 50
  var list: IndexedSeq[(Int, Int, Int)] = _
  var formula = ""
  var func: (Int, Int, Int) => Boolean = _

  override def updateEntity(): Unit = work

  def setup: Unit = {
    parser.parse(formula) match {
      case Some(f) => {
        func = f
        calculate
        activated = true
        progress = 0
      }
      case None =>
    }
  }

  private def calculate: Unit = {
    list = for {
      dx <- xlist
      dy <- ylist
      dz <- zlist
      if isValidCoord(xCoord + dx, yCoord + dy, zCoord + dz) && evaluate(dx, dy, dz)
    } yield (dx, dy, dz)
  }

  def evaluate(dx: Int, dy: Int, dz: Int): Boolean = func(dx, dy, dz)

  private def work: Unit = if (activated) {
    if (progress < list.length) {
      val (dx, dy, dz) = list.apply(progress)
      progress += 1
      val (x, y, z) = (xCoord + dx, yCoord + dy, zCoord + dz)
      if (worldObj.isAirBlock(x, y, z)) {
        worldObj.setBlock(x, y, z, Blocks.glass)
      } else {
        work
      }
    } else {
      activated = false
    }
  }

  private def isValidCoord(x: Int, y: Int, z: Int): Boolean =
    0 <= y && y < 256 &&
      -30000000 < x && x < 30000000 && -30000000 < z && z < 30000000

  override def readFromNBT(nbt: NBTTagCompound): Unit = {
    super.readFromNBT(nbt)
    val tag = nbt.getCompoundTag("FormulaBuilder")
    if (tag != null) {
      formula = tag.getString("Formula")
      activated = tag.getBoolean("Activated")
      if (activated) {
        setup
        progress = tag.getInteger("Progress")
      }
    }
  }

  override def writeToNBT(nbt: NBTTagCompound): Unit = {
    super.writeToNBT(nbt)
    val tag = new NBTTagCompound()
    tag.setString("Formula", formula)
    tag.setBoolean("Activated", activated)
    tag.setInteger("Progress", progress)
    nbt.setTag("FormulaBuilder", tag)
  }

  override def getDescriptionPacket: Packet = {
    val nbt = new NBTTagCompound()
    writeToNBT(nbt)
    new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt)
  }

  override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity): Unit = readFromNBT(pkt.func_148857_g())
}
