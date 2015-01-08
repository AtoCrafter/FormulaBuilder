package ato.formulabuilder.gui

import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import cpw.mods.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}

class MessageHandler extends IMessageHandler[Message, IMessage] {
  override def onMessage(message: Message, ctx: MessageContext): IMessage = {
    val world = ctx.getServerHandler.playerEntity.worldObj
    world.getTileEntity(message.x, message.y, message.z) match {
      case tile: TileEntityFormulaBuilder => tile.formula = message.formula
      case _ =>
    }
    null
  }
}
