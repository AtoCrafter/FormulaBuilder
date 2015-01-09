package ato.formulabuilder.gui

import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf

class Message extends IMessage {

  var x = 0
  var y = 0
  var z = 0
  var xMin = 0
  var xMax = 0
  var yMin = 0
  var yMax = 0
  var zMin = 0
  var zMax = 0
  var formula: String = _

  override def fromBytes(buf: ByteBuf): Unit = {
    x = buf.readInt()
    y = buf.readInt()
    z = buf.readInt()
    xMin = buf.readInt()
    xMax = buf.readInt()
    yMin = buf.readInt()
    yMax = buf.readInt()
    zMin = buf.readInt()
    zMax = buf.readInt()
    val bytes = new Array[Byte](buf.readableBytes())
    buf.readBytes(bytes)
    formula = new String(bytes, "UTF-8")
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
    buf.writeInt(xMin)
    buf.writeInt(xMax)
    buf.writeInt(yMin)
    buf.writeInt(yMax)
    buf.writeInt(zMin)
    buf.writeInt(zMax)
    buf.writeBytes(formula.getBytes("UTF-8"))
  }
}
