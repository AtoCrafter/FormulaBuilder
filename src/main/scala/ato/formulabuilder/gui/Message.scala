package ato.formulabuilder.gui

import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf

class Message extends IMessage {

  var x = 0
  var y = 0
  var z = 0
  var formula: String = _

  override def fromBytes(buf: ByteBuf): Unit = {
    x = buf.readInt()
    y = buf.readInt()
    z = buf.readInt()
    val bytes = new Array[Byte](buf.readableBytes())
    buf.readBytes(bytes)
    formula = new String(bytes, "UTF-8")
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
    buf.writeBytes(formula.getBytes("UTF-8"))
  }
}
