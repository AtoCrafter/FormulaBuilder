package ato.formulabuilder.gui

import ato.formulabuilder.FormulaBuilder
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.{GuiButton, GuiTextField}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

class GuiContainerFormulaBuilder(container: ContainerFormulaBuilder) extends GuiContainer(container) {

  val boxHeight = 12
  val rowHeight = 16

  var ranges: List[GuiTextField] = _
  var formulas: List[GuiTextField] = _
  var buttonSubmit: GuiButton = _

  override def drawGuiContainerBackgroundLayer(p_146976_1_ : Float, p_146976_2_ : Int, p_146976_3_ : Int): Unit = {}

  override def initGui(): Unit = {
    super.initGui()

    Keyboard.enableRepeatEvents(true)
    val i = (this.width - this.xSize) / 2
    val j = (this.height - this.ySize) / 2

    ranges = (0 to 5).map(n => new GuiTextField(fontRendererObj, i + xSize * (n % 2) * 3 / 4, j + rowHeight * (n / 2), xSize / 4, boxHeight)).toList

    formulas = (0 to 5).map(n => {
      val formula = new GuiTextField(fontRendererObj, i, j + rowHeight * (3 + n), xSize, boxHeight)
      formula.setMaxStringLength(1024)
      formula
    }).toList
    setGuiFormulas(container.tileentity.formula)

    buttonSubmit = new GuiButton(0, i, j + ySize - 20, xSize, 20, "Formula Update")
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(buttonSubmit)
  }

  override def drawScreen(i: Int, j: Int, f: Float): Unit = {
    super.drawScreen(i, j, f)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_BLEND)

    val k = (this.width - this.xSize) / 2
    val l = (this.height - this.ySize) / 2
    drawCenteredString(fontRendererObj, "<= x <=", k + xSize / 2, l + rowHeight * 0 + (rowHeight - boxHeight) / 2, 0xFFFFFF)
    drawCenteredString(fontRendererObj, "<= y <=", k + xSize / 2, l + rowHeight * 1 + (rowHeight - boxHeight) / 2, 0xFFFFFF)
    drawCenteredString(fontRendererObj, "<= z <=", k + xSize / 2, l + rowHeight * 2 + (rowHeight - boxHeight) / 2, 0xFFFFFF)

    (ranges ++ formulas).foreach(_.drawTextBox())
  }

  override def mouseClicked(x: Int, y: Int, code: Int): Unit = {
    super.mouseClicked(x, y, code)
    (formulas ++ ranges).foreach(_.mouseClicked(x, y, code))
  }

  override def keyTyped(c: Char, code: Int): Unit = {
    if (!(formulas ++ ranges).exists(_.textboxKeyTyped(c, code))) super.keyTyped(c, code)
  }

  override def actionPerformed(button: GuiButton): Unit = button match {
    case buttonSubmit => {
      syncFormula
      container.tileentity.formula = getFormula
      container.tileentity.setup
    }
    case _ =>
  }

  def syncFormula: Unit = {
    val message = new Message()
    message.x = container.tileentity.xCoord
    message.y = container.tileentity.yCoord
    message.z = container.tileentity.zCoord
    message.formula = getFormula
    FormulaBuilder.packetHandler.sendToServer(message)
  }

  def getFormula: String = {
    val fs = formulas.map(_.getText).filter(!_.replaceAll(" ", "").isEmpty).map("<___" + _ + "___>")
    if (fs.size > 0) {
      fs.tail.fold(fs.head)((a, b) => a + " && " + b)
    } else {
      ""
    }
  }

  def setGuiFormulas(formula: String): Unit = {
    val fs = formula.split("___> && <___").map(_.replaceAll("<___", "").replaceAll("___>", ""))
    (0 to math.min(fs.size, formulas.size) - 1).foreach(i => formulas(i).setText(fs(i)))
  }
}
