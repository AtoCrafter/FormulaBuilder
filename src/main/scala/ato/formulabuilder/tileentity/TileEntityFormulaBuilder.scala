package ato.formulabuilder.tileentity

import ato.formulabuilder.util.ParserFormula
import net.minecraft.inventory.IInventory
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity

class TileEntityFormulaBuilder extends TileEntity {

  val parser = new ParserFormula()

  var activated = false
  var progress = 0
  var xMin = 0
  var xMax = 0
  var yMin = 0
  var yMax = 0
  var zMin = 0
  var zMax = 0
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
      dx <- xMin to xMax
      dy <- yMin to yMax
      dz <- zMin to zMax
      if isValidCoord(xCoord + dx, yCoord + dy, zCoord + dz) && evaluate(dx, dy, dz)
    } yield (dx, dy, dz)
  }

  def evaluate(dx: Int, dy: Int, dz: Int): Boolean = func(dx, dy, dz)

  private def work: Unit = if (activated) {
    if (progress < list.length) {
      val (dx, dy, dz) = list.apply(progress)
      val itemstack = getBuildingBlock
      if (itemstack != null) {
        progress += 1
        val (x, y, z) = (xCoord + dx, yCoord + dy, zCoord + dz)
        if (worldObj.isAirBlock(x, y, z)) {
          worldObj.setBlock(x, y, z, itemstack.getItem.asInstanceOf[ItemBlock].field_150939_a)
        } else {
          work
        }
      }
    } else {
      activated = false
    }
  }

  private def getBuildingBlock: ItemStack = {
    worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) match {
      case tile: IInventory =>
        (0 to tile.getSizeInventory - 1).find(n =>
          tile.getStackInSlot(n) != null && tile.getStackInSlot(n).getItem.isInstanceOf[ItemBlock]) match {
          case Some(i) => tile.decrStackSize(i, 1)
          case None => null
        }
      case _ => null
    }
  }

  private def isValidCoord(x: Int, y: Int, z: Int): Boolean =
    0 <= y && y < 256 &&
      -30000000 < x && x < 30000000 && -30000000 < z && z < 30000000

  override def readFromNBT(nbt: NBTTagCompound): Unit = {
    super.readFromNBT(nbt)
    val tag = nbt.getCompoundTag("FormulaBuilder")
    if (tag != null) {
      val range = tag.getIntArray("Range")
      xMin = range(0)
      xMax = range(1)
      yMin = range(2)
      yMax = range(3)
      zMin = range(4)
      zMax = range(5)
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
    tag.setIntArray("Range", Array(xMin, xMax, yMin, yMax, zMin, zMax))
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
