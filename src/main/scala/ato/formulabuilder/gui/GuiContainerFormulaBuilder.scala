package ato.formulabuilder.gui

import ato.formulabuilder.FormulaBuilder
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.{GuiButton, GuiTextField}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

class GuiContainerFormulaBuilder(container: ContainerFormulaBuilder) extends GuiContainer(container) {

  var textField: GuiTextField = _
  var buttonSubmit: GuiButton = _

  override def drawGuiContainerBackgroundLayer(p_146976_1_ : Float, p_146976_2_ : Int, p_146976_3_ : Int): Unit = {}

  override def initGui(): Unit = {
    super.initGui()

    Keyboard.enableRepeatEvents(true)
    val i = (this.width - this.xSize) / 2
    val j = (this.height - this.ySize) / 2
    textField = new GuiTextField(fontRendererObj, i + 62, j + 24, 103, 12)
    textField.setFocused(true)
    textField.setMaxStringLength(1024)
    textField.setText(container.tileentity.formula)
    buttonSubmit = new GuiButton(0, 0, 0, "Submit")
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(buttonSubmit)
  }

  override def drawScreen(i: Int, j: Int, f: Float): Unit = {
    super.drawScreen(i, j, f)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_BLEND)
    textField.drawTextBox()
  }

  override def keyTyped(c: Char, code: Int): Unit = {
    if (!textField.textboxKeyTyped(c, code)) {
      super.keyTyped(c, code)
    }
  }

  override def actionPerformed(button: GuiButton): Unit = button match {
    case buttonSubmit => {
      syncFormula
      container.tileentity.formula = textField.getText
      container.tileentity.setup
    }
    case _ =>
  }

  def syncFormula: Unit = {
    val message = new Message()
    message.x = container.tileentity.xCoord
    message.y = container.tileentity.yCoord
    message.z = container.tileentity.zCoord
    message.formula = textField.getText
    FormulaBuilder.packetHandler.sendToServer(message)
  }
}
