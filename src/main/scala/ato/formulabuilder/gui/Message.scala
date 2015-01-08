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
    formula = String.valueOf(buf.readBytes(buf.readableBytes()))
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeInt(x)
    buf.writeInt(y)
    buf.writeInt(z)
    buf.writeBytes(formula.getBytes())
  }
}
