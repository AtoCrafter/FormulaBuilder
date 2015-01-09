package ato.formulabuilder.gui

import ato.formulabuilder.tileentity.TileEntityFormulaBuilder
import cpw.mods.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}

class MessageHandler extends IMessageHandler[Message, IMessage] {
  override def onMessage(message: Message, ctx: MessageContext): IMessage = {
    val world = ctx.getServerHandler.playerEntity.worldObj
    world.getTileEntity(message.x, message.y, message.z) match {
      case tile: TileEntityFormulaBuilder => {
        tile.xMin = message.xMin
        tile.xMax = message.xMax
        tile.yMin = message.yMin
        tile.yMax = message.yMax
        tile.zMin = message.zMin
        tile.zMax = message.zMax
        tile.formula = message.formula
        tile.setup
      }
      case _ =>
    }
    null
  }
}
