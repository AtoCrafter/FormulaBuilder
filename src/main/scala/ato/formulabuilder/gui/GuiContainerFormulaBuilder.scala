package ato.formulabuilder.gui

import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.inventory.GuiContainer
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

class GuiContainerFormulaBuilder(container: ContainerFormulaBuilder) extends GuiContainer(container) {

  var textField: GuiTextField = _

  override def drawGuiContainerBackgroundLayer(p_146976_1_ : Float, p_146976_2_ : Int, p_146976_3_ : Int): Unit = {}

  override def initGui(): Unit = {
    super.initGui()

    Keyboard.enableRepeatEvents(true)
    val i = (this.width - this.xSize) / 2
    val j = (this.height - this.ySize) / 2
    textField = new GuiTextField(fontRendererObj, i + 62, j + 24, 103, 12)
    textField.setFocused(true)
  }

  override def drawScreen(i: Int, j: Int, f: Float): Unit = {
    super.drawScreen(i, j, f)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_BLEND)
    textField.drawTextBox()
  }

  override def keyTyped(c: Char, code: Int): Unit = {
    if (textField.textboxKeyTyped(c, code)) {
      syncFormula
    } else {
      super.keyTyped(c, code)
    }
  }

  def syncFormula: Unit = {
    val formula = textField.getText
  }
}
